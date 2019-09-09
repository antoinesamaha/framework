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
package com.foc.vaadin.fields;

import org.xml.sax.Attributes;

import com.foc.property.FProperty;
import com.foc.shared.dataStore.IFocData;
import com.foc.util.Utils;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.components.FVMultipleChoiceComboBox;
import com.foc.vaadin.gui.components.FVMultipleChoiceOptionGroup;
import com.foc.vaadin.gui.components.FVMultipleChoiceOptionGroupPopupView;
import com.foc.vaadin.gui.components.multipleChoiceOptionGroupHorizontal.FVMultipleChoiceOptionGroupHorizontal;
import com.foc.vaadin.gui.layouts.FVWrapperLayout;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;

public class FVMultipleChoice implements FocXMLGuiComponentCreator {

  @Override
  public FocXMLGuiComponent newGuiComponent(FocXMLLayout xmlLayout, IFocData focData, Attributes attributes, IFocData rootFocData, String dataPathFromRootFocData) {
  	FProperty property = (FProperty) focData;
  	FocXMLGuiComponent comp = null;
  	
  	boolean usePopupView = attributes != null && attributes.getValue(FXML.ATT_USE_POPUP_VIEW) != null && 
                                                 attributes.getValue(FXML.ATT_USE_POPUP_VIEW).equalsIgnoreCase("true");

		if(usePopupView){
			comp = new FVMultipleChoiceOptionGroupPopupView(property, attributes);
		}else{
	  	boolean optionGroup = false;
	  	if(attributes != null){
	  		String optGroup = attributes.getValue(FXML.ATT_OPTION_GROUP);
	  		if(optGroup != null && optGroup.toLowerCase().equals("true")){
	  			optionGroup = true;
	  		}
	  	}
	  	
	  	if(optionGroup){
	  		String direction = attributes.getValue(FXML.ATT_DIRECTION);
	  		if(!Utils.isStringEmpty(direction) && direction.toLowerCase().equals(FXML.VAL_DIRECTION_HORIZONTAL)){
	  			comp = new FVMultipleChoiceOptionGroupHorizontal(property, attributes);
	  		}else{
	  			comp = new FVMultipleChoiceOptionGroup(property, attributes);
	  		}
	  	}else{
	  		comp = new FVMultipleChoiceComboBox(property, attributes);
	  	}
		}
		FocXMLGuiComponentStatic.setRootFocDataWithDataPath(comp, rootFocData, dataPathFromRootFocData);
    return FVWrapperLayout.wrapIfNecessary(comp);
  }
  
}
