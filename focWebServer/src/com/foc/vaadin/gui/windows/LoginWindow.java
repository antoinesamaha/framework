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
