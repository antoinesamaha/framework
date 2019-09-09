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
package com.foc.web.server.xmlViewDictionary;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import com.fab.gui.xmlView.IAddClickSpecialHandler;
import com.fab.gui.xmlView.IXMLViewDictionary;
import com.fab.gui.xmlView.UserXMLView;
import com.fab.gui.xmlView.UserXMLViewDesc;
import com.fab.gui.xmlView.XMLViewDefinition;
import com.fab.gui.xmlView.XMLViewDefinitionDesc;
import com.foc.Globals;
import com.foc.admin.FocUser;
import com.foc.admin.GroupXMLView;
import com.foc.admin.GroupXMLViewDesc;
import com.foc.business.workflow.implementation.WFLogDesc;
import com.foc.business.workflow.implementation.Workflow;
import com.foc.dataDictionary.FocDataDictionary;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.shared.dataStore.IFocData;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.util.Utils;
import com.foc.vaadin.FocCentralPanel;
import com.foc.vaadin.FocWebApplication;
import com.foc.vaadin.FocWebEnvironment;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.XMLBuilder;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.modules.workflow.WorkflowWebModule;
import com.foc.web.server.FocWebServer;

public class XMLViewDictionary implements IXMLViewDictionary {
  
  private Map<String, XMLView> xmlViewDicMap = null;
  private Map<String, IAddClickSpecialHandler> addClickSpecialHandlersMap = null;
  
  public XMLViewDictionary(){
  }
  
  public void dispose(){
  	
  }
 
  public static XMLViewDictionary getInstance(){
  	return FocWebServer.getInstance().getXMLViewDictionary();
  }
  
  public boolean isXMLViewFound(XMLViewKey xmlViewKey){
  	String key = xmlViewKey.builStringKey();
  	return getXmlViewDicMap().get(key) != null;
  }
  
  private Map<String, XMLView> getXmlViewDicMap(){
    if(xmlViewDicMap == null)
      xmlViewDicMap = new HashMap<String, XMLView>();
    return xmlViewDicMap;
  }

  public XMLView put(XMLView view){
    return getXmlViewDicMap().put(view.getXmlViewKey().getStringKey(), view); 
  }
  
  private Map<String, IAddClickSpecialHandler> getAddClickSpecialHandlersMap(){
    if(addClickSpecialHandlersMap == null) {
    	addClickSpecialHandlersMap = new HashMap<String, IAddClickSpecialHandler>();
    }
    return addClickSpecialHandlersMap;
  }
  
  public IAddClickSpecialHandler getAddClickSpecialHandler(String storageName){
  	return getAddClickSpecialHandlersMap().get(storageName);
  }

  public void putAddClickSpecialHandler(String storageName, IAddClickSpecialHandler handler){
    getAddClickSpecialHandlersMap().put(storageName, handler);
  }
  
  public XMLView put(String storageName, int type, String xmlFileName, int xmlReference, String javaClassName){
  	return put(storageName, type, XMLViewKey.CONTEXT_DEFAULT, XMLViewKey.VIEW_DEFAULT, xmlFileName, xmlReference, javaClassName);
  }

  public XMLView put(String storageName, int type, String context, String userView, String xmlFileName, int xmlReference, String javaClassName){
  	return put(storageName, type, context, userView, false, xmlFileName, xmlReference, javaClassName);  	
  }
  	
  public XMLView put(String storageName, int type, String context, String userView, boolean forNewObjectOnly, String xmlFileName, int xmlReference, String javaClassName){
  	return put(storageName, type, context, userView, forNewObjectOnly, null, xmlFileName, xmlReference, javaClassName);
  }
  	
  public XMLView put(String storageName, int type, String context, String userView, boolean forNewObjectOnly, String language, String xmlFileName, int xmlReference, String javaClassName){    
  	XMLViewKey xmlViewkey = new XMLViewKey(storageName, type, context, userView);
  	xmlViewkey.setForNewObjectOnly(forNewObjectOnly);
  	xmlViewkey.setLanguage(language);
    XMLView xmlViewClass = new XMLView(xmlViewkey, xmlFileName, javaClassName);
    
    String key = xmlViewkey.builStringKey();
    
    getXmlViewDicMap().put(key, xmlViewClass);
    return xmlViewClass;
  }
  
  public XMLView putXmlViewDefinition(XMLViewDefinition viewDefinition) {
    XMLView view = new XMLView(viewDefinition);
    put(view);
    
    return view;
  }
  
  public XMLView get(String storageName, int type, String context, String userView){
    XMLViewKey xmlViewkey = new XMLViewKey(storageName, type, context, userView);
    return get(xmlViewkey);
  }
  
  private void adjustKey_ToHaveTheLastSelectedViewByTheUser(XMLViewKey xmlViewKey){
    FocUser user = FocWebApplication.getFocUser();
    if(user != null){
      UserXMLView userView = userViewSelected_findViewForUserAndKey(user, xmlViewKey);
      if(userView != null){
        xmlViewKey.setUserView(userView.getView());
      }
    }
  }

  public String getUserPrintingView_PriorityTo_Key_UserSelection_Printing(XMLViewKey xmlViewKey){
  	String userPrintingViewName = null;
		if(xmlViewKey.isPrinterFriendly()){
			userPrintingViewName = xmlViewKey.getUserView(); 
		}else{
	  	//First we try to see if the user has already selected a default printing view
	    UserXMLView userXMLView = XMLViewDictionary.getInstance().userViewSelected_findViewForUserAndKey(FocWebApplication.getFocUser(), xmlViewKey);
	    
	    if(userXMLView != null){
	    	userPrintingViewName = userXMLView.getPrintingView();
	    }
	    
	  	//Second we try to see if the context has a VIEW_PRINTING from the system
	    if(userPrintingViewName == null || userPrintingViewName.isEmpty()){
	    	userPrintingViewName = XMLViewKey.VIEW_PRINTING;
	    }
		}
		return userPrintingViewName;
  }
  
  public UserXMLView userViewSelected_findViewForUserAndKey(FocUser currentUser, XMLViewKey currentKey){
    FocList viewsList = UserXMLViewDesc.getList(FocList.LOAD_IF_NEEDED);
    UserXMLView foundUserXMLView = null;
    for(int i=0; i<viewsList.size() && foundUserXMLView == null; i++){
      UserXMLView userXMLView = (UserXMLView) viewsList.getFocObject(i);
      if(userXMLView != null && userXMLView.getStorageName() != null && currentKey != null && currentKey.getStorageName() != null){
        boolean equal = 	userXMLView != null 
        							&& 	userXMLView.getStorageName().equals(currentKey.getStorageName()) 
        							&&  userXMLView.getContext().equals(currentKey.getContext())
        							&&  userXMLView.getType() == currentKey.getType()
        							&&  currentUser.equalsRef(userXMLView.getUser());
        if(equal){
          foundUserXMLView = userXMLView; 
        }
      }
    }
    return foundUserXMLView;
  }
  
	public void userViewSelected_saveViewForUserAndKey(FocUser user, XMLViewKey xmlViewKey){
		userViewSelected_saveViewForUserAndKey_Internal(user, xmlViewKey, false);
	}
	
	public void userViewSelected_saveViewForUserPrintingView(FocUser user, XMLViewKey xmlViewKey){
		userViewSelected_saveViewForUserAndKey_Internal(user, xmlViewKey, true);
	}
	
	private void userViewSelected_saveViewForUserAndKey_Internal(FocUser user, XMLViewKey xmlViewKey, boolean forPrinting){
		if(!FocWebApplication.getInstanceForThread().isMobile()){
	    if(user != null && xmlViewKey != null){
			  FocList focList = UserXMLViewDesc.getList(FocList.LOAD_IF_NEEDED);
			  if(focList != null){
	        UserXMLView userView = XMLViewDictionary.getInstance().userViewSelected_findViewForUserAndKey(user, xmlViewKey);
	        if(userView == null){
	          userView = (UserXMLView) focList.newEmptyItem();
	          userView.setStorageName(xmlViewKey.getStorageName());
	          userView.setContext(xmlViewKey.getContext());
	          userView.setType(xmlViewKey.getType());
	          userView.setUser(user);
	          focList.add(userView);
	        }
	    	  String selectedUserView = xmlViewKey.getUserView();
	    	  if(forPrinting){
	    	  	userView.setPrintingView(selectedUserView);
	    	  }else{
	    	  	userView.setView(selectedUserView);
	    	  }
	    	  userView.validate(true);
	      }
		  }
		}
	}  
  
	public XMLView get(XMLViewDefinition xmlViewDefinition){
		XMLView foundView = null;
		if(xmlViewDefinition != null && getXmlViewDicMap() != null && getXmlViewDicMap().values() != null){
			Iterator<XMLView> iter = (Iterator<XMLView>) getXmlViewDicMap().values().iterator();
			while(iter != null && iter.hasNext()){
				XMLView view = iter.next();
				if(!view.isSystemView() && FocObject.equal(view.getXmlviewDefinition(), xmlViewDefinition)){
					foundView = view;
				}
			}
		}
		return foundView;
	}
	
	private XMLView get_WithLanguageAttempt(XMLViewKey xmlViewKey){
		XMLView view = null;
		if(xmlViewKey != null) {
	  	String sessionLanguage = Globals.getApp().getLanguageForThisSession();
	  	if(!Utils.isStringEmpty(sessionLanguage) && Utils.isStringEmpty(xmlViewKey.getLanguage())) {
	  		XMLViewKey withLanguageXmlViewKey = new XMLViewKey(xmlViewKey);
	  		withLanguageXmlViewKey.setLanguage(sessionLanguage);
	  		view = getXmlViewDicMap().get(withLanguageXmlViewKey.builStringKey());
			}
	  	if(view == null) {
	  		view = getXmlViewDicMap().get(xmlViewKey.builStringKey());
	  	}
		}
		return view;
	}
	
  public XMLView get(XMLViewKey xmlViewKey){
  	return get_Internal(xmlViewKey, true, false);//Do not display errors
  }
  
  public XMLView get_WithoutPopupMessage_ForNotFoundAndDoNotRevertToDefault(XMLViewKey xmlViewKey){
  	return get_Internal(xmlViewKey, false, false);
  }

  public XMLView get_WithoutPopupMessage_ForNotFoundAndRevertToDefault(XMLViewKey xmlViewKey){
  	return get_Internal(xmlViewKey, true, false);
  }

  public XMLView get_WithoutAdjustToLastSelection(XMLViewKey xmlViewKey){
  	return get_Internal(xmlViewKey, false, false);//Do not display errors
  }
  
  private XMLView get_WithoutAdjustToLastSelection(XMLViewKey xmlViewKey, boolean display){
  	return get_Internal(xmlViewKey, false, false);//Do not display errors
  }
  
  public XMLView get_Strictly(XMLViewKey xmlViewKey){
  	return getXmlViewDicMap() != null ? getXmlViewDicMap().get(xmlViewKey) : null;
  }
  
  private XMLView get_Internal(XMLViewKey xmlViewKey, boolean adjustToLastViewSelected, boolean popupMessageIfNotFound){
  	if(adjustToLastViewSelected){
  		if(FocWebApplication.getInstanceForThread().isMobile()){
//  			xmlViewKey.setUserView(XMLViewKey.VIEW_MOBILE);
  			xmlViewKey.setMobileFriendly(true);
  			popupMessageIfNotFound = false;
  		}else{
  			adjustKey_ToHaveTheLastSelectedViewByTheUser(xmlViewKey);	
  		}
  	}
  	
  	XMLView view = get_WithLanguageAttempt(xmlViewKey);
    if(view == null){
//    	if(popupMessageIfNotFound && displayMessage){
//    		Globals.showNotification("View "+xmlViewKey.getStringKey(), "not found reverting to default view", FocWebEnvironment.TYPE_HUMANIZED_MESSAGE);
//    	}
    	Globals.logString("View "+xmlViewKey.getStringKey()+" not found reverting to default view");
      xmlViewKey.setUserView(XMLViewKey.VIEW_DEFAULT);
      xmlViewKey.setMobileFriendly(false);
      view = get_WithLanguageAttempt(xmlViewKey);
      
      if(view == null){
        xmlViewKey.setUserView(XMLViewKey.VIEW_PRINTING);
        view = get_WithLanguageAttempt(xmlViewKey);
      }
      if(view == null){
      	if(!xmlViewKey.isForNewObjectOnly()){
	      	Globals.logString("ERROR : View Not Found for key:"+xmlViewKey.getStringKey());
	      	if(popupMessageIfNotFound){
	      		Globals.showNotification("View Not Found", xmlViewKey.getStringKey(), FocWebEnvironment.TYPE_ERROR_MESSAGE);
	      	}
      	}
      }
    }
    return view;
  }
  
  public XMLView get_CreateIfNeeded_WithValidationSettings(XMLViewKey xmlViewKey) {
  	return get_CreateIfNeeded(xmlViewKey, true);
  }
  
  public XMLView get_CreateIfNeeded(XMLViewKey xmlViewKey) {
  	return get_CreateIfNeeded(xmlViewKey, false);
  }
  
  private XMLView get_CreateIfNeeded(XMLViewKey xmlViewKey, boolean withTableValidationSettings) {
    String key = xmlViewKey.builStringKey();
    XMLView view = getXmlViewDicMap().get(key);
    
    if (view == null) {
      view = putXmlViewDefinition(newViewDefinition(xmlViewKey, withTableValidationSettings));
    }
    
    return view;
  }
  
  public boolean delete(XMLViewKey key){
  	boolean error = true;
  	XMLView view = get(key);
  	if(view != null){
  		if(!view.isSystemView()){
    		if(view.getXmlviewDefinition() != null){
    			view.getXmlviewDefinition().setDeleted(true);
//    			Object valueDeleted = getXmlViewDicMap().get(key.getStringKey());
    			/*Object valueDeleted = */
    			getXmlViewDicMap().remove(key.getStringKey());
    			error = false;
    		}
  		}
  	}
  	return error;
  }

  public ICentralPanel newCentralPanel_NoParsing(INavigationWindow mainWindow, XMLViewKey xmlViewKey, IFocData focData){
  	return newCentralPanel(mainWindow, xmlViewKey, focData, false, true, true);
  }
  
  public ICentralPanel newCentralPanel(INavigationWindow mainWindow, XMLViewKey xmlViewKey, IFocData focData){
  	return newCentralPanel(mainWindow, xmlViewKey, focData, true, true, true);
  }

  public ICentralPanel newCentralPanel_NoAdjustmentToLastSelectedView(INavigationWindow mainWindow, XMLViewKey xmlViewKey, IFocData focData){
  	return newCentralPanel(mainWindow, xmlViewKey, focData, true, false, true);
  }

  private XMLView getView(XMLViewKey xmlViewKey, 
  		                    boolean    adjustViewToLastSelectedByUser, 
  		                    boolean    allowAdaptViewIfFocObjectIsCreated, 
  		                    IFocData   focData){
  	XMLView view = null;
  	if(xmlViewKey != null){
  		//We start by checking if we are opening a Form with a created FocObject
  		//In this case we need to try to see if there is a specific view for that
  		//And no need to adapt to the previously selected view of the user since it is for a forced view
	  	boolean createdForm = allowAdaptViewIfFocObjectIsCreated && xmlViewKey.getType() == XMLViewKey.TYPE_FORM && focData != null && focData instanceof FocObject && ((FocObject)focData).isCreated();
	  	if(createdForm){
	  	  XMLViewKey newXMLViewKey = new XMLViewKey(xmlViewKey);
	  	  newXMLViewKey.setForNewObjectOnly(true);	  	  
	  		view = get_Internal(newXMLViewKey, false, false);//Do not popup message
	  	}
	  	if(view == null){
			  view = adjustViewToLastSelectedByUser ? get(xmlViewKey) : get_WithoutAdjustToLastSelection(xmlViewKey, false);//No Error if not found / !(xmlViewKey.getUserView() == XMLViewKey.VIEW_PRINTING)
			  //Sometimes the View is null because the object comes from FAB. In this case we need to create the view
			  if(view == null && adjustViewToLastSelectedByUser){
			    FocDesc focDesc = null; 
			    if(focData instanceof FocObject){
			      FocObject focObject = (FocObject) focData;
			      focDesc = focObject.getThisFocDesc();
			    }else if(focData instanceof FocList){
			      FocList list = (FocList) focData;
			      focDesc = list.getFocDesc();
			    }
		      if(focDesc != null && focDesc.getFabTableDefinition() != null && !focDesc.getFabTableDefinition().isAlreadyExisting()){
		        view = get_CreateIfNeeded(xmlViewKey);
		      }
			  }
	  	}
  	}
	  return view;
  }
  
  public ICentralPanel newCentralPanel(INavigationWindow mainWindow, XMLViewKey xmlViewKey, IFocData focData, boolean withXMLParsing, boolean adjustViewToLastSelectedByUser, boolean allowAdaptViewIfFocObjectIsCreated){
  	return newCentralPanel(mainWindow, xmlViewKey, focData, withXMLParsing, adjustViewToLastSelectedByUser, allowAdaptViewIfFocObjectIsCreated, null);  	
  }
  
  public ICentralPanel newCentralPanel(INavigationWindow mainWindow, XMLViewKey xmlViewKey, IFocData focData, boolean withXMLParsing, boolean adjustViewToLastSelectedByUser, boolean allowAdaptViewIfFocObjectIsCreated, String linkSerialization){
    int rightAllowed  = GroupXMLViewDesc.ALLOW_SELECTION;//At that level maybe the user is null so we do not give creation rights
    String mandatoryView = "";
    
    FocUser userForSession = Globals.getApp().getUser_ForThisSession();
    if(userForSession != null){
    	int viewsRight = userForSession.getGroup().getViewsRight();
    	
      GroupXMLView xmlView = userForSession != null ? userForSession.findXMLView(xmlViewKey.getStorageName(), xmlViewKey.getContext(), xmlViewKey.getType()) : null;
      if(xmlView != null){
        rightAllowed  = xmlView.getRight();
        if(rightAllowed < viewsRight ){
        	rightAllowed = viewsRight;
        }
        mandatoryView = xmlView.getView() ;
      }else{
        //In this case we have a user but no vewRights line so we allow all
        rightAllowed  = GroupXMLViewDesc.ALLOW_CREATION;
        if(rightAllowed < viewsRight){
        	rightAllowed = viewsRight;
        }
      }
    }
    
    if(rightAllowed != GroupXMLViewDesc.ALLOW_NOTHING) mandatoryView = null;
    if(mandatoryView != null && mandatoryView.isEmpty()) mandatoryView = null;
    //At that level if mandatoryView != null it is realy the madatory to be applicable
    
    if(mandatoryView != null){
      xmlViewKey = new XMLViewKey(xmlViewKey);
      xmlViewKey.setUserView(mandatoryView);
      adjustViewToLastSelectedByUser = false;
      allowAdaptViewIfFocObjectIsCreated = false;
    }
    
  	XMLView view = getView(xmlViewKey, adjustViewToLastSelectedByUser, allowAdaptViewIfFocObjectIsCreated, focData);
    ICentralPanel centralPanel = null;
    
    //For Debug Only
    //Globals.showNotification("View :"+view.getFullFileName(), ""+xmlViewKey.getStringKey(), IFocEnvironment.TYPE_HUMANIZED_MESSAGE);
    //--------------
    
    if(mandatoryView != null && view != null && view.getXmlViewKey() != null && !view.getXmlViewKey().getUserView().equals(mandatoryView)){
//      Globals.showNotification("View "+mandatoryView+" not found in dictionary", ""+xmlViewKey.getStringKey(), IFocEnvironment.TYPE_ERROR_MESSAGE);
    	Globals.logString("ERROR : View "+mandatoryView+" not found in dictionary"+xmlViewKey.getStringKey());
    }else{
      if(view != null){
  		  centralPanel = view.newJavaClassInstance();
  		  if(centralPanel != null){
  		  	centralPanel.setLinkSerialisation(linkSerialization);
  		  	centralPanel.init(mainWindow, view, focData);
  		  	if(view.getXmlViewKey() == null || view.getXmlViewKey().getStorageName() == null || !view.getXmlViewKey().getStorageName().equals(WFLogDesc.WF_LOG_VIEW_KEY)) {
  		  		FocCentralPanel.logFormOpened(focData);
  		  	}
  		  	centralPanel.setViewRights(rightAllowed);
  		  	if(withXMLParsing){
  		  		centralPanel.parseXMLAndBuildGui();
  		  	}
  		  }
  	  }else{
//  	  	Globals.showNotification("View not found in dictionary", ""+xmlViewKey.getStringKey(), IFocEnvironment.TYPE_ERROR_MESSAGE);
      	Globals.logString("ERROR : View not found in dictionary "+xmlViewKey.getStringKey());
  	  }
    }
	  return centralPanel;
  }
  
  public XMLViewDefinition newViewDefinition(XMLViewKey key, boolean withTableValidationSettings){
    FocList listOfViews = XMLViewDefinitionDesc.getList(FocList.LOAD_IF_NEEDED);
    XMLViewDefinition viewDefinition = (XMLViewDefinition) listOfViews.newEmptyItem(); 
    
    viewDefinition.setStorageName(key.getStorageName());
    viewDefinition.setType(key.getType());
    viewDefinition.setContext(key.getContext());
    viewDefinition.setView(key.getUserView());
    viewDefinition.setJavaClassName("");
    if(key.getType() == XMLViewKey.TYPE_TABLE){
    	if(withTableValidationSettings){
    		StringBuilder builder = new StringBuilder();
    		builder.append("<VerticalLayout width=\"100%\" height=\"700px\">\n");
    		builder.append("\t" + addValidationSettings());
    		builder.append("\t<GuiTable name=\"MAIN_TABLE\" dataPath=\"DATAROOT\" width=\"100%\" height=\"650px\">\n");
    		builder.append("\t</GuiTable>\n");
    		builder.append("</VerticalLayout>");
    		viewDefinition.setXML(builder.toString());
    	}else{
    		viewDefinition.setXML("<VerticalLayout width=\"100%\" height=\"700px\">\n  <GuiTable name=\"MAIN_TABLE\" dataPath=\"DATAROOT\" width=\"100%\" height=\"650px\">\n  </GuiTable>\n</VerticalLayout>");
    	}
    }else{
    	if(withTableValidationSettings){
    		StringBuilder builder = new StringBuilder();
    		builder.append("<VerticalLayout width=\"100%\" height=\"700px\">\n");
    		builder.append("\t" + addValidationSettings());
    		builder.append("</VerticalLayout>");
    		viewDefinition.setXML(builder.toString());
    	}else{
    		viewDefinition.setXML("<VerticalLayout width=\"100%\" height=\"700px\">\n</VerticalLayout>");
    	}
    }
    
    listOfViews.add(viewDefinition);
    viewDefinition.validate(true);
    listOfViews.validate(true);
    
    putXmlViewDefinition(viewDefinition);
    
    return viewDefinition;
  }
  
  public void loadViewsFromTable(){
  	FocList listOfViews = XMLViewDefinitionDesc.getList(FocList.LOAD_IF_NEEDED);
  	for(int i=0; i<listOfViews.size(); i++){
  		XMLViewDefinition viewDefinition = (XMLViewDefinition) listOfViews.getFocObject(i);
  		
  		putXmlViewDefinition(viewDefinition);
  	}
  	insertDocumentHeaderView_IfNeeded();
  }

  public XMLViewKey newXMLViewKey_ForDocumentHeader(String view){
  	XMLViewKey key = new XMLViewKey();
  	
  	key.setStorageName(XMLViewKey.DOCUMENT_HEADER_STORAGE_NAME);
  	key.setType(XMLViewKey.TYPE_FORM);
  	key.setContext(XMLViewKey.CONTEXT_DEFAULT);
  	key.setUserView(view);
  	
  	return key;
  }
  
  public void updateDocumentHearerView(XMLViewDefinition xmlViewDef){
  	XMLBuilder xmlBuilder = new XMLBuilder(null);
  	
  	xmlBuilder.appendLine("<VerticalLayout name=\"transactionHeader\" width=\"100%\" height=\"-1px\" captionMargin=\"0\">");
  	xmlBuilder.incrementSpaces();
  		xmlBuilder.appendLine("<HorizontalLayout name=\"test\" height=\"-1px\" width=\"100%\" captionMargin=\"0\">");
  		xmlBuilder.incrementSpaces();
  			xmlBuilder.appendLine("<VerticalLayout  spacing=\"true\" width=\"-1px\" alignment=\"left\" height=\"-1px\" captionMargin=\"0\">");
  		  xmlBuilder.incrementSpaces();
  				xmlBuilder.appendLine("<GuiField name=\"COMPANY.LOGO\" width=\"300px\" height=\"150px\" editable=\"false\" />");
  			xmlBuilder.decrementSpaces();	
  			xmlBuilder.appendLine("</VerticalLayout>");
  			xmlBuilder.appendLine("<VerticalLayout  spacing=\"true\" width=\"-1px\" alignment=\"center\" height=\"-1px\" captionMargin=\"0\">");
  			xmlBuilder.incrementSpaces();
  				xmlBuilder.appendLine("<Label name=\"DOC_TITLE\" value=\"$P{DOCUMENT_TITLE}\" style=\"title\" alignment=\"center\" height=\"25px\"/>");
  				xmlBuilder.appendLine("<Label name=\"proposal_status_label\" value=\"Proposal\" style=\"h1\" visibleWhen=\"STATUS=10\" alignment=\"center\" />");
  				xmlBuilder.appendLine("<Label name=\"canceled_status_label\" value=\"Canceled\" style=\"h1\" visibleWhen=\"STATUS=40\" alignment=\"center\" />");
  				xmlBuilder.appendLine("<Label name=\"WF_COMMENT\" value=\"$F{WF_COMMENT}\" style=\"textRed,bold\" visibleWhen=\"NOT(IS_EMPTY($F{WF_COMMENT}))\" alignment=\"center\" />");
  				xmlBuilder.appendLine("<Label name=\"WF_LAST_COMMENT\" value=\"Previous Comment: $F{WF_LAST_COMMENT}\" style=\"textRed,f11\" visibleWhen=\"NOT(IS_EMPTY($F{WF_LAST_COMMENT}))\" alignment=\"center\" />");
  		  xmlBuilder.decrementSpaces();  				
  			xmlBuilder.appendLine("</VerticalLayout>");
  			xmlBuilder.appendLine("<VerticalLayout  spacing=\"true\" width=\"-1px\" alignment=\"right\" height=\"-1px\" captionMargin=\"0\">");
  			xmlBuilder.incrementSpaces();
  				xmlBuilder.appendLine("<GuiField name=\"CODE\" caption=\"Code\" captionPos=\"left\" captionWidth=\"60px\" captionStyle=\"f12,bold\" width=\"130px\" height=\"-1px\" help=\"Transaction Code. The Prefix Is Configurable.\" helpIndex=\"1\"/>");
  				xmlBuilder.appendLine("<GuiField name=\"EXTERNAL_CODE\" caption=\"Ext. Code\" captionPos=\"left\" captionWidth=\"60px\" captionStyle=\"f12,bold\" width=\"130px\" height=\"-1px\" maskDataFoundError=\"true\" help=\"External Code. Optional allow to link the transaction to external software.\" helpIndex=\"2\"/>");
  				xmlBuilder.appendLine("<GuiField name=\"DATE\" caption=\"Date\" captionPos=\"left\" captionWidth=\"60px\" width=\"130px\" captionStyle=\"f12,bold\" height=\"-1px\" immediate=\"true\" help=\"Transaction Date. Initially = Today.\" helpIndex=\"3\" />");
  				xmlBuilder.appendLine("<GuiField name=\"SITE\" caption=\"Site\" captionPos=\"left\" captionWidth=\"60px\" width=\"130px\" captionStyle=\"f12,bold\" height=\"-1px\" help=\"Site or Branch. By default = to the Site the User is logged to\" helpIndex=\"4\"/>");
  			xmlBuilder.decrementSpaces();  				
  			xmlBuilder.appendLine("</VerticalLayout>");
  	  xmlBuilder.decrementSpaces();  			
  		xmlBuilder.appendLine("</HorizontalLayout>");
  		xmlBuilder.appendLine("<Label name=\"null\" value=\" \" bgColor=\"#CCCCCC\" height=\"2px\" width=\"100%\" style=\"line\" />");
    xmlBuilder.decrementSpaces();  		
    xmlBuilder.appendLine("</VerticalLayout>");
    
  	xmlViewDef.setXML(xmlBuilder.getXMLString());
  	xmlViewDef.validate(true);
  }

  public void updateDocumentHearerView_Printing(XMLViewDefinition xmlViewDef){
  	XMLBuilder xmlBuilder = new XMLBuilder(null);
  	
  	xmlBuilder.appendLine("<VerticalLayout name=\"transactionHeader\" width=\"100%\" height=\"-1px\" captionMargin=\"0\">");
  	xmlBuilder.incrementSpaces();
  	
    xmlBuilder.appendLine("<HorizontalLayout name=\"test\" height=\"-1px\" width=\"100%\" captionMargin=\"0\">");
    xmlBuilder.incrementSpaces();
  	  xmlBuilder.appendLine("<VerticalLayout  spacing=\"true\" width=\"300px\" alignment=\"left\" height=\"-1px\" captionMargin=\"0\">");
      xmlBuilder.incrementSpaces();
  	    xmlBuilder.appendLine("<GuiField name=\"COMPANY.LOGO\" width=\"300px\" height=\"150px\" alignment=\"left\" editable=\"false\" />");
  	    xmlBuilder.decrementSpaces();
  	  xmlBuilder.appendLine("</VerticalLayout>");
  	  xmlBuilder.appendLine("<VerticalLayout  spacing=\"true\" width=\"100%\" alignment=\"center\" height=\"-1px\" expandRatio=\"1\" captionMargin=\"0\">");
  	  xmlBuilder.incrementSpaces();
  	    xmlBuilder.appendLine("<Label name=\"proposal_status_label\" value=\"Proposal\" style=\"h1\" visibleWhen=\"STATUS=10\" alignment=\"center\" width=\"100%\"/>");
  	    xmlBuilder.appendLine("<Label name=\"canceled_status_label\" value=\"Canceled\" style=\"h1\" visibleWhen=\"STATUS=40\" alignment=\"center\" width=\"100%\"/>");
  	  xmlBuilder.decrementSpaces();
  	  xmlBuilder.appendLine("</VerticalLayout>");
  	  xmlBuilder.appendLine("<VerticalLayout  spacing=\"true\" width=\"250px\" alignment=\"right\" height=\"-1px\" captionMargin=\"0\">");
  	    xmlBuilder.incrementSpaces();
  	      xmlBuilder.appendLine("<Label name=\"DOC_TITLE\" value=\"$P{DOCUMENT_TITLE}\" style=\"title,text-right\" width=\"250px\" height=\"30px\"/>");
  	      xmlBuilder.appendLine("<Label name=\"CODE\" value=\"Code: $F{CODE}\" style=\"bold,f14,text-right\" width=\"250px\"  height=\"-1px\" />");
  	      xmlBuilder.appendLine("<Label name=\"EXTERNAL_CODE\" value=\"Ext. Code: $F{EXTERNAL_CODE}\" style=\"bold,f14,text-right\" width=\"250px\"  height=\"-1px\" maskDataFoundError=\"true\" visibleWhen=\"NOT(IS_EMPTY($F{EXTERNAL_CODE}))\" />");
  	      xmlBuilder.appendLine("<Label name=\"DATE\" value=\"Date: $F{DATE}\" style=\"bold,f14,text-right\" width=\"250px\"  height=\"-1px\" />");
  	    xmlBuilder.decrementSpaces();
  	    xmlBuilder.appendLine("</VerticalLayout>");
  	  xmlBuilder.decrementSpaces();
  	  xmlBuilder.appendLine("</HorizontalLayout>");
  	  xmlBuilder.appendLine("<Label value=\"$F{COMPANY.OFFICIAL_ADDRESS}\" width=\"100%\" height=\"-1px\" style=\"f12\"/>");
  	  xmlBuilder.appendLine("<Line />");
      xmlBuilder.decrementSpaces();
  	xmlBuilder.appendLine("</VerticalLayout>");
    
  	xmlViewDef.setXML(xmlBuilder.getXMLString());
  	xmlViewDef.validate(true);
  }

  private void insertDocumentHeaderView_IfNeeded(){
  	XMLViewKey key = newXMLViewKey_ForDocumentHeader(XMLViewKey.VIEW_DEFAULT);
  	
  	if(getXmlViewDicMap().get(key.getStringKey()) == null){
	  	XMLViewDefinition xmlViewDef = newViewDefinition(key, false);
	  	
	  	updateDocumentHearerView(xmlViewDef);
  	}
  	
  	key = newXMLViewKey_ForDocumentHeader(XMLViewKey.VIEW_PRINTING);
  	
  	if(getXmlViewDicMap().get(key.getStringKey()) == null){
	  	XMLViewDefinition xmlViewDef = newViewDefinition(key, false);
	  	
	  	updateDocumentHearerView_Printing(xmlViewDef);
  	}  	
  }
  
  
//  public String[] getXmlViews(XMLViewKey xmlViewKey){
//    int i = 0;
//    int size = getXmlViewDicMap().values().size();
//    String[] arrayOfViews = new String[size];
//    String key = xmlViewKey.getStringKey();
//    Iterator itr = getXmlViewDicMap().values().iterator();
//    while(itr.hasNext()){
//      arrayOfViews[i] = getXmlViewDicMap().get(key).toString();
//      itr.next();
//      i++;
//    }
//    return arrayOfViews;
//  }
  public String[] getXmlViews(XMLViewKey xmlViewKey){
  	return getXmlViews(xmlViewKey,true);
  }
  
  public String[] getXmlViews(XMLViewKey xmlViewKey, boolean includeCreationForm){
//    String storageName = xmlViewKey.getStorageName();
//    String context     = xmlViewKey.getContext();
//    int    type        = xmlViewKey.getType();
//
//    FocUser userForSession = Globals.getApp().getUser_ForThisSession();
//    FocList xmlViewRightsList = null;
//
//    if(userForSession != null){
//      FocGroup group = userForSession.getGroup();
//      if(group != null){
//        xmlViewRightsList = group.getXmlViewRightsList();
//      }      
//    }
    ArrayList<String> arrayList = new ArrayList<String>();
  	if(xmlViewKey != null && xmlViewKey.getContext() != null && xmlViewKey.getStorageName() != null){
	    Iterator<XMLView> itr = getXmlViewDicMap().values().iterator();
	
	    while(itr.hasNext()){
	      XMLView view = (XMLView) itr.next();
	      XMLViewKey xmlViewKey2 = view.getXmlViewKey();
	      if(xmlViewKey2 != null && xmlViewKey2.getContext() != null && xmlViewKey2.getStorageName() != null){
		      //view get the key and check if it maches the current key with the 3 fields (StorageName, Type, Context)
		      //The array is an array of String and the value is the View
		      if(xmlViewKey.getStorageName().equals(xmlViewKey2.getStorageName()) 
		          && xmlViewKey.getType() == xmlViewKey2.getType() 
		          && xmlViewKey.getContext().equals(xmlViewKey2.getContext())){
		        if(!xmlViewKey2.isForNewObjectOnly() || includeCreationForm){  
		      	  arrayList.add(xmlViewKey2.getUserView());
		        }
		      }
	      }
	    }
  	}
  	
//    if(xmlViewRightsList != null){
//      GroupXMLView xmlView = userForSession.findXMLView(storageName, context, type);
//
//      if(xmlView != null){
//        int rightLevel = xmlView.getRight();
//
//        if(rightLevel == GroupXMLViewDesc.ALLOW_NOTHING){
//
//          String mandatoryView = xmlView.getView();
//          if(mandatoryView != null && !mandatoryView.equals("") && !mandatoryView.isEmpty()) 
//            arrayList.add(mandatoryView);
//        }
//        else{
//          addXmlViewsFromDictionaryInArrayList(xmlViewKey, arrayList);
//        }
//      }
//      else{
//        addXmlViewsFromDictionaryInArrayList(xmlViewKey, arrayList);
//      }
//    }else{
//      //In case the group has no rights list. Add default policy of group here.
//      addXmlViewsFromDictionaryInArrayList(xmlViewKey, arrayList);
//    }
   
    int size = arrayList.size();
    String[] arrayOfViews = new String[size];
    for(int i=0; i<size; i++){
      arrayOfViews[i] = arrayList.get(i).toString(); 
    }
    return arrayOfViews;
  }

	@Override
	public Iterator<XMLViewKey> newIterator() {
		return new XMLViewKeyIterator(getXmlViewDicMap().values().iterator());
	}
	
	public class XMLViewKeyIterator implements Iterator {

		private Iterator<XMLView> viewITerator = null;
		
		public XMLViewKeyIterator(Iterator<XMLView> viewITerator){
			this.viewITerator = viewITerator;	
		}
		
		@Override
		public boolean hasNext() {
			return viewITerator != null ? viewITerator.hasNext() : false;
		}

		@Override
		public Object next() {
			XMLView xmlView = null;
			if(viewITerator != null){
				xmlView = viewITerator.next();
			}
			return xmlView != null ? xmlView.getXmlViewKey() : null;
		}

		@Override
		public void remove() {
		}
	}
	
	public String addValidationSettings() {
		StringBuilder builder = new StringBuilder();
    builder.append("<" + FXML.TAG_VALIDATION_SETTINGS + " ");
    builder.append(FXML.ATT_WITH_APPLY + "=\"true\" ");
    builder.append(FXML.ATT_WITH_DISCARD + "=\"true\" ");
    builder.append(FXML.ATT_WITH_ATTACH + "=\"true\" ");
    builder.append(FXML.ATT_WITH_PRINT + "=\"true\" ");
    builder.append(FXML.ATT_WITH_EMAIL + "=\"true\" ");
    builder.append("/>\n");
    return builder.toString();
  }
	
  public String pasrseXmlLayout(IFocData focData, XMLView xmlView, XMLViewKey xmlViewKey){
		StringBuilder newXmlContent = new StringBuilder();
		if(xmlViewKey != null && XMLViewDictionary.getInstance().isXMLViewFound(xmlViewKey)){
			InputStream inputStream = xmlView.getXMLStream_ForView();
			Scanner xmlFileLine = new Scanner(inputStream).useDelimiter("\n");
      while(xmlFileLine.hasNext()){
      	String line = xmlFileLine.next();
      	line = FocDataDictionary.getInstance().resolveExpression(focData, line, true);
      	newXmlContent.append(line);
      }
		}
		return newXmlContent.toString();
	}
}
