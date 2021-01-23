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
package com.foc.web.server.session;

import java.io.Serializable;
import java.util.HashMap;

import jakarta.servlet.http.HttpSession;

import com.foc.admin.FocUser;
import com.foc.admin.UserSession;
import com.foc.business.company.Company;
import com.foc.business.workflow.WFSite;
import com.foc.dataDictionary.FocDataDictionary;
import com.foc.shared.dataStore.IFocData;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.menuTree.FVMenuTree;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.WrappedSession;

@SuppressWarnings("serial")
public class FocWebSession implements Serializable {
	private UserSession userSession = null;
	private HttpSession httpSession = null;
	
	private IFocData          dataToPrint            = null;
	private XMLViewKey        viewKeyToPrint         = null;
	private FocDataDictionary dataDictionaly_ToPrint = null;
	private boolean           exitAfterPrint         = false;
	private boolean           focDataOwner_ToPrint   = false;
	
	private boolean           notificationEnabled    = true;
	private ICentralPanel     initialContectForm     = null;
	
	private HashMap<String, Object> parameterMap = null;
	
	public static final String PARAM_KEY_IP = "IP";
	
	public FocWebSession(HttpSession session){
		this.httpSession = session;
		userSession = new UserSession();
	}
	
	public void dispose(){
		if(userSession != null){
		  userSession.dispose();
		  userSession = null;
		}
		viewKeyToPrint = null;
		dataToPrint = null;
		dataDictionaly_ToPrint = null;
	}

	public void close(){
		if(userSession != null){
//			Globals.logString("DEBUG_SESSION_NOT_VALID in FocWebSession.close() we are closing the session and clearing the userSession user = null");
		  userSession.clear();
			FVMenuTree.resetMenuTree_ForThisSession();
		}
  }

	public String getId(){
		return httpSession != null ? httpSession.getId() : "";
	}

	public FocUser getFocUser() {
		return userSession != null ? userSession.getUser() : null;
	}

	public void setFocUser(FocUser user) {
	  userSession.setUser(user);
	}
	
	public WFSite getSite(){
	  return userSession.getSite();
	}
	
	public Company getCompany(){
	  return userSession.getCompany();
	}

  public UserSession getUserSession() {
    return userSession;
  }
  
  public WrappedSession getHttpSession() {
    return VaadinSession.getCurrent().getSession();
  }
  
  public void setPrintingData(FocDataDictionary dictionary, XMLViewKey viewKeyToPrint, IFocData dataToPrint, boolean exitAfterPrint){
  	this.dataToPrint             = dataToPrint;
  	this.viewKeyToPrint          = viewKeyToPrint;
  	this.dataDictionaly_ToPrint  = dictionary;
  	this.exitAfterPrint          = exitAfterPrint;
  }

  public void removePrintingData(){
  	this.dataToPrint             = null;
  	this.viewKeyToPrint          = null;
  	this.dataDictionaly_ToPrint  = null;
  	this.focDataOwner_ToPrint    = false;
  }

	public IFocData getDataToPrint() {
		return dataToPrint;
	}

	public XMLViewKey getViewKeyToPrint() {
		return viewKeyToPrint;
	}
	
	public void putParameter(String key, Object value){
		if(parameterMap == null){
			parameterMap = new HashMap<String, Object>();
		}
		parameterMap.put(key, value);
	}
	
	public Object getParameter(String key){
		Object value = null;
		if(parameterMap != null){
			value = parameterMap.put(key, value);
		}
		return value;
	}

	public boolean isExitAfterPrint() {
		return exitAfterPrint;
	}

	public void setExitAfterPrint(boolean exitAfterPrint) {
		this.exitAfterPrint = exitAfterPrint;
	}

	public FocDataDictionary getDataDictionaly_ToPrint() {
		return dataDictionaly_ToPrint;
	}

	public void setDataDictionaly_ToPrint(FocDataDictionary dataDictionaly_ToPrint) {
		this.dataDictionaly_ToPrint = dataDictionaly_ToPrint;
	}

	public boolean isNotificationEnabled() {
		return notificationEnabled;
	}

	public void setNotificationEnabled(boolean allowPopupException) {
		this.notificationEnabled = allowPopupException;
	}

	public ICentralPanel getInitialContectForm() {
		return initialContectForm;
	}

	public void setInitialContectForm(ICentralPanel initialContectForm) {
		this.initialContectForm = initialContectForm;
	}

	public boolean isFocDataOwner_ToPrint() {
		return focDataOwner_ToPrint;
	}

	public void setFocDataOwner_ToPrint(boolean focDataOwner_ToPrint) {
		this.focDataOwner_ToPrint = focDataOwner_ToPrint;
	}
}
