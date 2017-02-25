package com.foc.vaadin.gui.manipulators;

import org.xml.sax.Attributes;

import com.foc.property.FObject;
import com.foc.shared.dataStore.IFocData;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.fields.FocXMLGuiComponentCreator;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.ui.Component;

public class FVIncludeXMLCreator implements FocXMLGuiComponentCreator {

	protected XMLViewKey newXMLViewKey(IFocData focData, Attributes attributes){
    String  storageName = attributes.getValue(FXML.ATT_VIEW_KEY_STORAGE_NAME);
    String  context     = attributes.getValue(FXML.ATT_VIEW_KEY_CONTEXT);
    String  userView    = attributes.getValue(FXML.ATT_VIEW_KEY_VIEW);
    if(userView == null){
    	userView = attributes.getValue(FXML.ATT_VIEW_KEY_DEFAULT_VIEW);
    }
    String  type        = attributes.getValue(FXML.ATT_VIEW_KEY_TYPE);
    String  forNewStr   = attributes.getValue(FXML.ATT_VIEW_KEY_FOR_NEW);
    boolean forNew      = false;
    
    if(context  == null || context.isEmpty()) context = XMLViewKey.CONTEXT_DEFAULT;
    if(userView == null || userView.isEmpty()) userView = XMLViewKey.VIEW_DEFAULT;
    if(forNewStr != null){
    	if(forNewStr.toLowerCase().equals("true") || forNewStr.equals("1")) forNew = true;
    }
    
    XMLViewKey xmlViewKey = new XMLViewKey(storageName, XMLViewKey.TYPE_FORM, context, userView);
    xmlViewKey.setForNewObjectOnly(forNew);
    if(type != null && !type.isEmpty()){
      xmlViewKey.setType(type);
    }
    return xmlViewKey;
	}
	
	@Override
	public FocXMLGuiComponent newGuiComponent(FocXMLLayout xmlLayout, IFocData focData, Attributes attributes, IFocData rootFocData, String dataPathFromRootFocData) {
		if(focData instanceof FObject){
			focData = ((FObject)focData).getObject_CreateIfNeeded();
		}
    XMLViewKey   xmlViewKey   = newXMLViewKey(focData, attributes);
    boolean adjustToLastSelected = attributes.getValue(FXML.ATT_VIEW_KEY_VIEW) == null;
    //FocXMLLayout centralPanel = (FocXMLLayout) XMLViewDictionary.getInstance().newCentralPanel_NoParsing(xmlLayout.getMainWindow(), xmlViewKey, focData);
    FocXMLLayout centralPanel = (FocXMLLayout) XMLViewDictionary.getInstance().newCentralPanel(xmlLayout.getMainWindow(), xmlViewKey, focData, false, adjustToLastSelected, true);    
    if(centralPanel != null){
      centralPanel.setParentLayout(xmlLayout);
      FocXMLGuiComponentStatic.setRootFocDataWithDataPath(centralPanel, rootFocData, dataPathFromRootFocData);
      
      centralPanel.setAttributesOfIncludeNode(attributes);
    }
    return centralPanel;
	}
}