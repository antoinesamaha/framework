package com.foc.vaadin.gui.windows;

import com.foc.ConfigInfo;
import com.foc.admin.FocUserDesc;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.FocCentralPanel;
import com.foc.vaadin.ICentralPanel;
import com.foc.web.modules.admin.AdminWebModule;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.ui.Component;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class LoginWindow extends Window {

  public LoginWindow() {
    setWidth("400px");
    setHeight("300px");
    setModal(true);
    setCaption("Login");
  }
  
  public void init(FocCentralPanel focWindow){
    ICentralPanel centralPanel = getLoginCentralPanel(focWindow);
    setContent((Component) centralPanel);
  }
  
  public static ICentralPanel getLoginCentralPanel(FocCentralPanel focWindow){
    return getLoginCentralPanel(focWindow, XMLViewKey.VIEW_DEFAULT);
  }
  
  public static ICentralPanel getLoginCentralPanel(FocCentralPanel focWindow, String view){
	  XMLViewKey xmlViewKey = new XMLViewKey(FocUserDesc.getInstance().getStorageName(), XMLViewKey.TYPE_FORM, AdminWebModule.CTXT_LOGIN, view);
//	  if(ConfigInfo.isArabic()) {
//	  	xmlViewKey.setContext(AdminWebModule.CTXT_LOGIN_AR);
//	  }
	  ICentralPanel centralPanel = XMLViewDictionary.getInstance().newCentralPanel(focWindow, xmlViewKey, null);
	  
	  return centralPanel;
  }
}
