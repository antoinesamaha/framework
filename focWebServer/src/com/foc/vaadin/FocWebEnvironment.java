/*******************************************************************************
 * Copyright 2016 Antoine Nicolas SAMAHA
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.foc.vaadin;

import java.util.Iterator;

import com.fab.gui.xmlView.IXMLViewDictionary;
import com.foc.Application;
import com.foc.Globals;
import com.foc.IFocEnvironment;
import com.foc.OptionDialog;
import com.foc.access.FocLogger;
import com.foc.admin.UserSession;
import com.foc.dataDictionary.FocDataDictionary;
import com.foc.desc.FocObject;
import com.foc.focVaadinTheme.FocVaadinTheme;
import com.foc.list.FocList;
import com.foc.shared.IFocMobileModule;
import com.foc.shared.IFocWebModuleShared;
import com.foc.shared.dataStore.IFocData;
import com.foc.shared.dataStore.IFocDataDictionary;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.util.Utils;
import com.foc.vaadin.gui.windows.optionWindow.IOption;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.modules.admin.AdminWebModule;
import com.foc.web.modules.admin.OptionDialog_Form;
import com.foc.web.server.FocWebServer;
import com.foc.web.server.session.FocWebSession;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.foc.web.unitTesting.FocUnitDictionary;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Window;

public class FocWebEnvironment implements IFocEnvironment {
	
	private String themeName = FocVaadinTheme.THEME_NAME;
	
	public void setThemeName(String theme) {
		themeName = theme;
	}
	
	@Override
	public String getThemeName() {
		return themeName;
	}	

  @Override
  public void showNotification(String notificationMessage, String description, int notificationType) {
  	showNotification(notificationMessage, description, notificationType, -1, null);
  }
  
//  @Override
//  public void showNotification(String notificationMessage, String description, int notificationType, int delay) {
//  	showNotification(notificationMessage, description, notificationType, delay, null);  	
//  }
  
  @Override
  public void showNotification(String notificationMessage, String description, int notificationType, int delay, String styleName) {
  	try{
	  	if(Globals.getApp() != null && Globals.getApp().isUnitTest() && FocUnitDictionary.getInstance() != null){
	  		FocUnitDictionary.getInstance().expectedNotification_Occured(notificationMessage, description, notificationType);
	  	}else{
	  		FocWebSession focWebSession = FocWebApplication.getFocWebSession_Static();
	  		if(focWebSession == null || focWebSession.isNotificationEnabled()){
	  			Notification.Type type = Notification.Type.ERROR_MESSAGE;
	  			if(notificationType == IFocEnvironment.TYPE_ERROR_MESSAGE){
	  				FocLogger.getInstance().addError("Error Popup: "+notificationMessage+" "+description);
	  			}else if(notificationType == IFocEnvironment.TYPE_WARNING_MESSAGE){
	  				FocLogger.getInstance().addWarning("Warning Popup: "+notificationMessage+" "+description);
	  				type = Notification.Type.WARNING_MESSAGE;
	  			}else if(notificationType == IFocEnvironment.TYPE_HUMANIZED_MESSAGE){
	  				FocLogger.getInstance().addInfo("Info Popup: "+notificationMessage+" "+description);
	  				type = Notification.Type.HUMANIZED_MESSAGE;
	  			}else if(notificationType == IFocEnvironment.TYPE_TRAY_NOTIFICATION){
	  				FocLogger.getInstance().addInfo("Tray Popup: "+notificationMessage+" "+description);
	  				type = Notification.Type.TRAY_NOTIFICATION;
	  			}
	  			Notification notification = new Notification(notificationMessage, description, type);
	  			notification.setDelayMsec(delay);
	  			
	  			if(!Utils.isStringEmpty(styleName)) notification.setStyleName(styleName);
	  			if(Page.getCurrent() != null){
	  				notification.show(Page.getCurrent());
	  			}
	  			/*
	  			FancyNotifications fancyNotifications = new FancyNotifications();
	        fancyNotifications.setPosition(Position.BOTTOM_RIGHT);
	        
	  			FancyNotification fancyNotification = new FancyNotification(null, notificationMessage, description);
          fancyNotification.getTitleLabel().setContentMode(ContentMode.HTML);
          fancyNotification.getDescriptionLabel().setContentMode(ContentMode.HTML);
          fancyNotification.addLayoutClickListener(notificationClickEvent -> Page.getCurrent().setLocation("https://github.com/alump/FancyLayouts"));
          fancyNotifications.showNotification(fancyNotification);
          */
	  		}
	  	}
  	}catch(Exception e){
  		Globals.logExceptionWithoutPopup(e);
  	}
//    Notification.show(notificationMessage, description, type);
  }

	@Override
	public void popup(FocObject focObject, boolean dialog) {
		if(focObject != null){
			/*
	    Window localWindow = (Window) FocThreadLocal.getWindow();
	    if(localWindow != null){
	    	if(dialog && localWindow.getParent() != null){
	    		localWindow = (Window) localWindow.getParent();
	    	}
			 */
			INavigationWindow navigationWindow = (INavigationWindow) FocWebApplication.getInstanceForThread().getNavigationWindow();
			XMLViewKey key = new XMLViewKey(focObject.getThisFocDesc().getStorageName(), XMLViewKey.TYPE_FORM, focObject.getThisFocDesc().focDesc_getGuiContext(), XMLViewKey.VIEW_DEFAULT);
			ICentralPanel centralPanel = XMLViewDictionary.getInstance().newCentralPanel((INavigationWindow) navigationWindow, key, focObject);
			if (centralPanel != null) {
				if (dialog) {
					FocCentralPanel focCentralWindow = new FocCentralPanel();
					focCentralWindow.fill();
					focCentralWindow.changeCentralPanelContent(centralPanel, false);
					Window window = focCentralWindow.newWrapperWindow();
					FocWebApplication.getInstanceForThread().addWindow(window);
				} else {
					navigationWindow.changeCentralPanelContent(centralPanel, true);
				}
			}
		}
	}
	
	@Override
	public void popup(IFocData focData, boolean dialog, String storageName, int type, String context, String view) {
		if(focData != null){

			INavigationWindow navigationWindow = (INavigationWindow) FocWebApplication.getInstanceForThread().getNavigationWindow();
			XMLViewKey    key          = new XMLViewKey(storageName, type, context, view);
			ICentralPanel centralPanel = XMLViewDictionary.getInstance().newCentralPanel((INavigationWindow) navigationWindow, key, focData);
			if(centralPanel != null){
				if(dialog){
					FocCentralPanel focCentralWindow = new FocCentralPanel();
					focCentralWindow.fill();
					focCentralWindow.changeCentralPanelContent(centralPanel, false);
					
					Window window = focCentralWindow.newWrapperWindow();
					FocWebApplication.getInstanceForThread().addWindow(window);
				}else{
					navigationWindow.changeCentralPanelContent(centralPanel, true);
				}
			}else{
				Globals.showNotification("Could not find requested page.", "", IFocEnvironment.TYPE_ERROR_MESSAGE);
			}
		}
	}

	@Override
	public Application getFocApplication() {
		return FocWebServer.getInstance() != null ? FocWebServer.getInstance().getFocApplication() : null;
	}
	
	@Override
	public void setFocApplication(Application app) {
		if(FocWebServer.getInstance() != null){
			FocWebServer.getInstance().setFocApplication(app);
		}
	}

  @Override
  public UserSession getUserSession() {
    UserSession userSession = null;
    FocWebSession webSession = FocWebApplication.getFocWebSession_Static();
    if(webSession != null){
      userSession = webSession.getUserSession();    
    }
    return userSession;
  }

	@Override
	public OptionDialog_Form popupOptionDialog(OptionDialog dialog) {
		OptionDialog_Form optionDialogForm = null;
		if(FocWebApplication.getInstanceForThread() != null){
			INavigationWindow navigationWindow = (INavigationWindow) FocWebApplication.getInstanceForThread().getNavigationWindow();
			
			XMLViewKey xmlViewKey = new XMLViewKey(AdminWebModule.OPTION_WINDOW_STORAGE, XMLViewKey.TYPE_FORM, XMLViewKey.CONTEXT_DEFAULT, XMLViewKey.VIEW_DEFAULT);
			optionDialogForm = (OptionDialog_Form) XMLViewDictionary.getInstance().newCentralPanel(navigationWindow, xmlViewKey, null);
			optionDialogForm.setOptionDialog(dialog);
			dialog.setOptionDialog_Form(optionDialogForm);
			
			optionDialogForm.popup();
		}
		if(dialog != null){
			Globals.logString("POPUP DIALOG : "+dialog.getMessage());
		}
		/*
		OptionDialogWindow optionWindow = new OptionDialogWindow(dialog.getMessage(), dialog);
    optionWindow.setWidth(dialog.getWidth());
    optionWindow.setHeight(dialog.getHeight());
    
    for(int i=0; i<dialog.getOptionCount(); i++){
    	String option = dialog.getOptionAt(i);
    	optionWindow.addOption(option, new DialogOption(i));
    }
    FocWebApplication.getInstanceForThread().addWindow(optionWindow);
    */
		return optionDialogForm;
	}
	
	public void popupMobileWindow(ICentralPanel newCentralPanel, boolean keepPrevious){
		FocCentralPanel focCentral = new FocCentralPanel();
		focCentral.changeCentralPanelContent(newCentralPanel, keepPrevious);
	}
	
	private class DialogOption implements IOption {
		private int indexOrder = -1;
		
		public DialogOption(int indexOrder){
			this.indexOrder = indexOrder;
		}
		
		@Override
		public void optionSelected(Object contextObject) {
			OptionDialog dialog = (OptionDialog) contextObject;
			dialog.executeOption(dialog.getOptionCaptionAt(indexOrder));
		}
	}

	@Override
	public IXMLViewDictionary getXMLViewDictionary() {
		return FocWebServer.getInstance() != null ? FocWebServer.getInstance().getXMLViewDictionary() : null;
	}

	@Override
	public FocList getMobileFocList_FromSession(String listSessionId) {
		return getUserSession() != null ? getUserSession().getCustomFocList_ForMobile(listSessionId) : null;
	}

	@Override
	public void setMobileFocList_ForSession(FocList focList, String listSessionId) {
		if(getUserSession() != null){
			getUserSession().setCustomFocList_ForMobile(focList, listSessionId);
		}
	}

	@Override
	public String getSessionID() {
	  FocWebApplication webApp = FocWebApplication.getInstanceForThread();
		return webApp != null ? webApp.getSessionID_Debug() : "NULL";
	}

	@Override
	public void setNotificationsEnabled(boolean enabled) {
		FocWebSession focWebSession = FocWebApplication.getFocWebSession_Static();
		if(focWebSession != null){
			FocWebApplication.getFocWebSession_Static().setNotificationEnabled(enabled);
		}
	}

	@Override
	public void applyUserTheme() {
		FocWebApplication webApp = FocWebApplication.getInstanceForThread();
		if(webApp != null){
			webApp.applyUserThemeSelection();
		}
	}

	@Override
	public Iterator<IFocWebModuleShared> newWebModuleIterator() {
		return FocWebServer.getInstance() != null ? FocWebServer.getInstance().modules_NewIterator() : null;
	}

	@Override
	public Iterator<IFocMobileModule> newMobileModuleIterator(){
		return FocWebServer.getInstance() != null ? FocWebServer.getInstance().mobileModules_NewIterator() : null;
	}
	
	@Override
	public IFocDataDictionary getFocDataDictionary() {
		return FocDataDictionary.getInstance();
	}
}
