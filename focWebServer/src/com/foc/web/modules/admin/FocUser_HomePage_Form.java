package com.foc.web.modules.admin;

import com.foc.ConfigInfo;
import com.foc.Globals;
import com.foc.admin.FocUserDesc;
import com.foc.admin.UserSession;
import com.foc.shared.dataStore.IFocData;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.FocCentralPanel;
import com.foc.vaadin.FocWebApplication;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLView;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class FocUser_HomePage_Form extends FocXMLLayout {

	@Override
	public void init(INavigationWindow window, XMLView xmlView, IFocData focData) {
		super.init(window, xmlView, UserSession.getInstanceForThread().getUser());
	}

	@Override
	protected void afterLayoutConstruction() {
		super.afterLayoutConstruction();

		Button mobileCheckBox = (Button) getComponentByName("MOBILE_CHECKBOX");
		if(mobileCheckBox != null){
			
		}
		
		if(!Globals.isValo()){
			/*Button userCredentialsButton = (Button) getComponentByName("OPEN_USER_CREDENTIALS");*/
			Object object = getComponentByName("HOME_PAGE_HORIZONTAL_LAYOUT");
			if(object != null && object instanceof HorizontalLayout){
				HorizontalLayout mainPageHorizontalLayout = (HorizontalLayout) object;
				Button userCredentialsButton = new Button("Modify");
				mainPageHorizontalLayout.addComponent(userCredentialsButton);
				
				if(userCredentialsButton != null){
					userCredentialsButton.addClickListener(new ClickListener(){
						@Override
						public void buttonClick(ClickEvent event) {
							popupUserCredintionals(getMainWindow());
						}
					});
				}
			}
		}
	}
	
	public static Window popupUserCredintionals(INavigationWindow iNavigationWindow){
		XMLViewKey key = new XMLViewKey(FocUserDesc.getInstance().getStorageName(), XMLViewKey.TYPE_FORM, AdminWebModule.CONTEXT_COMPANY_SELECTION, XMLViewKey.VIEW_DEFAULT);
		if(ConfigInfo.isArabic()){
			key.setContext(AdminWebModule.CONTEXT_COMPANY_SELECTION_AR);
		}
		ICentralPanel centralPanel = XMLViewDictionary.getInstance().newCentralPanel(iNavigationWindow, key, FocWebApplication.getFocUser());
		Window window = null;
		if(FocWebApplication.getInstanceForThread().isMobile()){
			iNavigationWindow.changeCentralPanelContent(centralPanel, true);
		}else{
			FocCentralPanel centralWindow = new FocCentralPanel();
			centralWindow.fill();
			centralWindow.changeCentralPanelContent(centralPanel, false);
			
			window = centralWindow.newWrapperWindow();
			window.setCaption("User Account");
			window.setPositionX(300);
			window.setPositionY(100);
			FocWebApplication.getInstanceForThread().addWindow(window);
		}
		return window;
	}
	
}  

