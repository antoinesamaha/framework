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
package com.foc.vaadin.gui.components;

import org.xml.sax.Attributes;

import com.foc.desc.field.FField;
import com.foc.desc.field.FXMLViewSelectorField;
import com.foc.property.FProperty;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;

@SuppressWarnings("serial")
public class FVXMLViewSelector extends FVMultipleChoiceStringField {

  public FVXMLViewSelector(FProperty property, Attributes attributes) {
    super(property, attributes);
    FField fld = property != null ? property.getFocField() : null;
    if(fld != null && fld instanceof FXMLViewSelectorField){
    	XMLViewKey key = ((FXMLViewSelectorField) fld).getXmlViewKey();
    	String[] viewsArray = XMLViewDictionary.getInstance().getXmlViews(key);
    	if(viewsArray != null){
	    	for(int i=0; i<viewsArray.length; i++){
					addItem(viewsArray[i]);
				}
    	}
    }
  }
}
