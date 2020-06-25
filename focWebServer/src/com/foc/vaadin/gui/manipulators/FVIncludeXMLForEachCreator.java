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
package com.foc.vaadin.gui.manipulators;

import org.xml.sax.Attributes;

import com.foc.Globals;
import com.foc.access.FocDataMap;
import com.foc.dataWrapper.FocListWrapper;
import com.foc.list.FocList;
import com.foc.shared.dataStore.IFocData;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.util.Utils;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.layouts.FVForEachLayout;
import com.foc.vaadin.gui.xmlForm.FXML;
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
    	String  isPaginated = attributes.getValue(FXML.ATT_FOR_EACH_PAGINATED);
    	if(!Utils.isStringEmpty(isPaginated)) {
    		try {
    			boolean paginated = Boolean.parseBoolean(isPaginated);
    			if(paginated) {
    				String  buttonStyle = attributes.getValue(FXML.ATT_FOR_EACH_MORE_BUTTON_STYLE);
    				String  pageSizeStr = attributes.getValue(FXML.ATT_FOR_EACH_PAGE_SIZE);
    				if(!Utils.isStringEmpty(pageSizeStr)) {
    					int pageSize = Integer.parseInt(pageSizeStr);
    					forEach = new FVForEachLayout(xmlLayout, focData, xmlViewKey, attributes, paginated, pageSize, buttonStyle);
    				} else {
    					forEach = new FVForEachLayout(xmlLayout, focData, xmlViewKey, attributes, paginated, buttonStyle);
    				}
    			} 
    		} catch (Exception e) {
    			Globals.logException(e);
    		}
    	} 
    	if(forEach == null)	forEach = new FVForEachLayout(xmlLayout, focData, xmlViewKey, attributes);
    }
    FocXMLGuiComponentStatic.setRootFocDataWithDataPath(forEach, rootFocData, dataPathFromRootFocData);
    return forEach;
	}
	
}
