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

import com.foc.ConfigInfo;
import com.foc.Globals;
import com.foc.property.FDouble;
import com.foc.property.FProperty;

@SuppressWarnings({ "serial"})
public class FVNumField extends FVTextField {

	public FVNumField(FProperty property, Attributes attributes) {
		super(property, attributes);
		addStyleName("numerical");
	}
	
	@Override
  protected boolean isWithSelectAllListenerOnFocus(){
		//DANGER - Keep
		//We stop selecting all characters in NumField and we will settle with the fact
		//That users can double click for that effect because:
		//1- This is causing the focus to jump to a specific unexpected cell
		//2- It is causing a focus call to the server for nothing
//  	return true;
		return false;
  }	
	
	@Override
	public void copyMemoryToGui() {
		try {
			if (getFocData() instanceof FDouble) {
				FProperty property = (FProperty) getFocData();
				if (property != null) {
					if (property.isValueNull() && ConfigInfo.isAllowNullProperties()) {
						setValue("");
					} else {
						setValue((String) ((FDouble) getFocData()).getTableDisplayObject());
					}
				}
			}
		} catch (Exception e) {
			Globals.logException(e);
		}
	}
	
}
