package com.foc.web.modules.admin;

import com.foc.Globals;
import com.foc.OptionDialog;
import com.foc.vaadin.FocWebApplication;
import com.foc.vaadin.FocWebVaadinWindow;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.FVIconFactory;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.vaadin.gui.layouts.FVAbsoluteLayout;
import com.foc.vaadin.gui.layouts.FVHorizontalLayout;
import com.foc.vaadin.gui.windows.LoginWindow;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.themes.BaseTheme;

@SuppressWarnings("serial")
public class HomePage_BeforeLogin_Form extends FocXMLLayout {
	private FocUser_Login_Form loginPanel = null; 
			
  @Override
  public void init(com.foc.web.gui.INavigationWindow window, com.foc.web.server.xmlViewDictionary.XMLView xmlView, com.foc.shared.dataStore.IFocData focData) {
    super.init(window, xmlView, focData);
  }
  
  public void dispose(){
  	super.dispose();
  	loginPanel = null;
  }
  
  @Override
  public void showValidationLayout(boolean showBackButton) {
  }
  
  protected void afterLayoutConstruction() {
  	FVAbsoluteLayout horLayout = (FVAbsoluteLayout) getComponentByName("_COVERBANNER");
  	loginPanel = (FocUser_Login_Form) LoginWindow.getLoginCentralPanel((FocWebVaadinWindow) getMainWindow());
  	FocXMLAttributes xmlAttrib = new FocXMLAttributes();
  	xmlAttrib.addAttribute(FXML.ATT_TOP, "150px");
  	xmlAttrib.addAttribute(FXML.ATT_LEFT, "300px");
  	loginPanel.setLoginButtonVisible(false);
  	horLayout.addComponent((Component) loginPanel, xmlAttrib);
  	
  	loginPanel.setParentLayout(this);
  	
  	FVButton loginButton   = (FVButton) getComponentByName("LOGIN");
    if(loginButton != null){
    	if(Globals.isValo()){
	    	loginButton.setIcon(FVIconFactory.getInstance().getFVIcon_24(FVIconFactory.ICON_LOGIN));  		    		
    	}else{
	    	loginButton.setStyleName(BaseTheme.BUTTON_LINK);
	    	loginButton.setIcon(FVIconFactory.getInstance().getFVIcon_48(FVIconFactory.ICON_LOGIN));  		
    	}
    	
    	loginButton.addClickListener(new ClickListener() {
        
        @Override
        public void buttonClick(ClickEvent event) {
        	if(FocWebApplication.getInstanceForThread().isMobile()){
        		ICentralPanel loginPanel = LoginWindow.getLoginCentralPanel((FocWebVaadinWindow) getMainWindow());
        		getMainWindow().changeCentralPanelContent(loginPanel, true);
        	}else{
        		if(loginPanel != null){
        			loginPanel.loginClicked();
        		}else{
			      	LoginWindow loginWindow = new LoginWindow();
			      	loginWindow.init((FocWebVaadinWindow) getMainWindow());
			        getUI().addWindow(loginWindow);
        		}
        	}
        }
      });
    }
    
  	FVButton contactButton = (FVButton) getComponentByName("CONTACT");
    if(contactButton != null){
    	if(Globals.isValo()){
    		contactButton.setIcon(FVIconFactory.getInstance().getFVIcon_24(FVIconFactory.ICON_CONTACT));  		    		
    	}else{
	    	contactButton.setStyleName(BaseTheme.BUTTON_LINK);
	    	contactButton.setIcon(FVIconFactory.getInstance().getFVIcon_48(FVIconFactory.ICON_CONTACT));
    	}
    	
    	contactButton.addClickListener(new ClickListener() {
        
        @Override
        public void buttonClick(ClickEvent event) {
        	OptionDialog dialog = new OptionDialog("Contact", "<b>01Barmaja</b><br>Berytech technological Pole<br>Mar Roukoz - Mkalles - Lebanon<br><br>" +
        			"<a href=\"http://www.01barmaja.com\">www.01barmaja.com</a><br>" +
        			"<a href=\"mailto:01barmaja@01barmaja.com\" target=\"_blank\">01barmaja@01barmaja.com</a><br>"+
        			"(+961) 4 533 040 ext.4021<br><br>"+
        			"<b>Contact:</b><br>"+
        			"Antoine SAMAHA<br>"+
        			"(+961) 3 144 554<br>"+
        			"<a href=\"mailto:antoine.samaha@01barmaja.com\" target=\"_top\">antoine.samaha@01barmaja.com</a>") {
						
						@Override
						public boolean executeOption(String optionName) {
							
							return false;
						}
					};
					dialog.addOption("OK", "Exit");
					dialog.setWidth("400px");
					dialog.setHeight("400px");
					Globals.popupDialog(dialog);
        }
      });
    }
  	
  }

}
