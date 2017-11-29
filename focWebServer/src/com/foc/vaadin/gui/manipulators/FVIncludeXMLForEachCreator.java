package com.foc.vaadin.gui.manipulators;

import org.xml.sax.Attributes;

import com.foc.access.FocDataMap;
import com.foc.dataWrapper.FocListWrapper;
import com.foc.list.FocList;
import com.foc.shared.dataStore.IFocData;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.layouts.FVForEachLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;

public class FVIncludeXMLForEachCreator extends FVIncludeXMLCreator {

	@Override
	public FocXMLGuiComponent newGuiComponent(FocXMLLayout xmlLayout, IFocData focData, Attributes attributes, IFocData rootFocData, String dataPathFromRootFocData) {
    XMLViewKey   xmlViewKey   = newXMLViewKey(focData, attributes);

  	if(focData instanceof FocDataMap){
  		focData = ((FocDataMap)focData).getMainFocData();
  	}
    
    FVForEachLayout forEach = null;
    if(focData instanceof FocList || focData instanceof FocListWrapper){
    	forEach = new FVForEachLayout(xmlLayout, focData, xmlViewKey, attributes);
    }
    FocXMLGuiComponentStatic.setRootFocDataWithDataPath(forEach, rootFocData, dataPathFromRootFocData);
    return forEach;
	}
	
}