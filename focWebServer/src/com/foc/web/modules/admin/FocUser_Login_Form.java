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
package com.foc.web.modules.admin;

import com.foc.ConfigInfo;
import com.foc.Globals;
import com.foc.IFocEnvironment;
import com.foc.OptionDialog;
import com.foc.admin.FocGroup;
import com.foc.admin.FocLoginAccess;
import com.foc.admin.FocUser;
import com.foc.admin.FocUserDesc;
import com.foc.admin.GrpWebModuleRightsDesc;
import com.foc.desc.FocConstructor;
import com.foc.shared.dataStore.IFocData;
import com.foc.util.Encryptor;
import com.foc.util.Utils;
import com.foc.vaadin.FocWebApplication;
import com.foc.vaadin.FocWebVaadinWindow;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.vaadin.gui.components.FVCheckBox;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.FocWebServer;
import com.foc.web.server.xmlViewDictionary.XMLView;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.JavaScript;

@SuppressWarnings("serial")
public class FocUser_Login_Form extends FocXMLLayout {
	private FocWebApplication anotherApplicationAlreadyRunning = null;
  private FVButton          login           = null;
  private FocUser           approvedFocUser = null;
  private String loginErrorMessage = "";
  
  protected void afterLayoutConstruction(){
    login = (FVButton) getComponentByName("login");
    if(login != null){
    	login.setClickShortcut(KeyCode.ENTER);
	    login.addClickListener(new ClickListener() {
	
	      @Override
	      public void buttonClick(ClickEvent event) {
	      	loginClicked();
	      }
	    });
    }
    initLoginModeField();
  }
  
  public void loginClicked(){
    validateLogin();
    if(Globals.getApp() == null || !Globals.getApp().isUnitTest){
    	addBrowserBackClick_JavaScript();
    }
  }
  
  public void setLoginButtonVisible(boolean visible){
  	if(login != null){
  		login.setVisible(visible);
  	}
  }
  
  public void setLoginErrorMessage(String message) {
  	loginErrorMessage = message;
  }
  
  private void addBrowserBackClick_JavaScript(){
  	JavaScript.getCurrent().execute("window.onbeforeunload = function leaveCurrentPage() { return 'Your work will be lost.;' };");
	}
  
  private FVCheckBox getLoginModeCheckBox(){
  	FVCheckBox loginModeField = (FVCheckBox) getComponentByName("LOGIN_MODE");
  	if(loginModeField != null){
  		loginModeField.setImmediate(true);
  	}
  	return loginModeField;
  }

  private void initLoginModeField(){
  	FVCheckBox loginModeField = getLoginModeCheckBox();
  	if(loginModeField != null){
  		loginModeField.setValue(FocWebApplication.getInstanceForThread().isMobile());
  		loginModeField.setImmediate(true);
  		loginModeField.addValueChangeListener(new ValueChangeListener() {
				@Override
				public void valueChange(ValueChangeEvent event) {
					FocWebApplication.getInstanceForThread().setIsMobile(FocWebApplication.getInstanceForThread().isMobile());
				}
			});

//  		loginModeField.setCaption("Mobile device browsing");
//  		if(FocWebApplication.getInstanceForThread().isMobile()){
//  			loginModeField.setCaption("Login as web browsing");
//  		}
  	}
  }
  
  @Override
  public void init(INavigationWindow window, XMLView xmlView, IFocData focData) {
  	if(focData == null){
		  FocConstructor constr = new FocConstructor(FocUserDesc.getInstance(), null);
		  FocUser user = new FocUser(constr);
		  user.setDbResident(false);
		
		  if(ConfigInfo.isDevMode() /*&& Globals.getApp().getUser() != null*/){
		    user.setName(ConfigInfo.getUsername());
		    String password = ConfigInfo.getPassword();
		    if(password != null) user.setPassword(password);
		  }
		  focData = user;
  	}
  	
    super.init(window, xmlView, focData);
    setEnableRightsApplicationToGuiFields(false);
  }
  
  private void validateLogin() {
    validationCheckData(null);
    FocUser user = (FocUser) getFocData();

    String username = user.getName();
    String password = user.getPassword();
//    if(Globals.getApp().getCurrentCompany() != null && Globals.getApp().getDataSource().isEmptyDatabaseJustCreated()){
//    	user.setCurrentCompany(Globals.getApp().getCurrentCompany());
//    }
    
    String encryptedPassword = Encryptor.encrypt_MD5(String.valueOf(password));
    FocLoginAccess loginAccess = new FocLoginAccess();
    
    Globals.logString("Username "+username+" Password "+encryptedPassword);
    
    int status = loginAccess.checkUserPassword(username, encryptedPassword, false);
    if (status != com.foc.Application.LOGIN_VALID) {
    	status = loginAccess.checkUserPassword(username, encryptedPassword, false, true);
    }
    
    if (status == com.foc.Application.LOGIN_VALID && !Utils.isStringEmpty(ConfigInfo.getAllowedUrlsForAdmin())) {
    	boolean verifyUrls = false;
    	if(!(getMainWindow() instanceof FocWebVaadinWindow)) {
    		//Globals.showNotification("", "", notificationType);
    		status = com.foc.Application.LOGIN_WRONG;
    	} else {
        FocWebVaadinWindow window = (FocWebVaadinWindow) getMainWindow();
        if (window != null) {
    			FocGroup group = loginAccess.getUser() != null ? loginAccess.getUser().getGroup() : null;
    			if(group != null && group.getWebModuleRights(AdminWebModule.MODULE_NAME) != GrpWebModuleRightsDesc.ACCESS_NONE) {
    				verifyUrls = true;
    			}
        }
    	}
    	if (verifyUrls) {
    		String url = Globals.getApp().getURL();
    		if (Utils.isStringEmpty(url) || !url.startsWith(ConfigInfo.getAllowedUrlsForAdmin())) {
      		status = com.foc.Application.LOGIN_WRONG;    			
    		}
    	}
    }
    
    if(status == com.foc.Application.LOGIN_VALID){
    	approvedFocUser = loginAccess.getUser();
      
    	anotherApplicationAlreadyRunning = null;
      for(int i=0; i<FocWebServer.getInstance().getApplicationCount(); i++){
      	FocWebApplication app = FocWebServer.getInstance().getApplicationAt(i);
      	if(app != null && app != getUI() && app.getFocWebSession() != null && app.getFocWebSession().getFocUser() != null){
      		if(app.getFocWebSession().getFocUser().equalsRef(approvedFocUser) && !app.isClosing()){
      			anotherApplicationAlreadyRunning = app;
      		}
      	}
      }
      
      if(anotherApplicationAlreadyRunning != null){
      	OptionDialog optionDialog = new OptionDialog("Previous Session Still Opened", "User Already has an opened session.\n Do you wish to neglect the previous sessions and open a new one now?") {
					
					@Override
					public boolean executeOption(String optionName) {
						if(optionName.equals("USER_ALREADY_CONNECTED")){
							Globals.logString("DEBUG_SESSION_NOT_VALID FocUser_Login_Form.validateLogin.executeOption() calling Session Logout");
							anotherApplicationAlreadyRunning.logout(null);
							loginWithUserAlreadyApproved_Internal(approvedFocUser);
						}
						return false;
					}
				};
				optionDialog.addOption("USER_ALREADY_CONNECTED", "Yes. Neglect previous sessions");
				optionDialog.addOption("CANCEL", "Cancel");
				Globals.popupDialog(optionDialog);
      	
//      	OptionDialogWindow optionWindow = new OptionDialogWindow("User Already connected.", anotherApplicationAlreadyRunning);
//      	optionWindow.setWidth("500px");
//      	optionWindow.setHeight("200px");
//      	
//      	optionWindow.addOption("Close the other connection", new IOption() {
//					@Override
//					public void optionSelected(Object contextObject) {
//						FocWebApplication anotherApplicationAlreadyRunning = (FocWebApplication) contextObject;
//						anotherApplicationAlreadyRunning.logout();
//						loginWithUserAlreadyApproved(approvedFocUser);
//					}
//				});
//
//      	optionWindow.addOption("Back to login", new IOption() {
//					@Override
//					public void optionSelected(Object contextObject) {
//					}
//				});
//      	
//    		getUI().addWindow(optionWindow);
      }else{
      	loginWithUserAlreadyApproved_Internal(approvedFocUser);
      }
    } else {
    	Globals.showNotification("LOGIN CREDENTIALS ARE INCORRECT", loginErrorMessage, IFocEnvironment.TYPE_WARNING_MESSAGE);
    }
    
    loginAccess.dispose();
    loginAccess = null;
  }

  private void loginWithUserAlreadyApproved_Internal(FocUser focUser){
    FocWebVaadinWindow window = (FocWebVaadinWindow) getMainWindow();
    window.loginWithUserAlreadyApproved(focUser);
    goBack(null);
  }
}
