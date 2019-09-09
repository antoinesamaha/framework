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

import com.foc.property.FProperty;
import com.foc.property.FTime;

@SuppressWarnings({ "serial"})
public class FVTimeField extends FVTextField {

	public FVTimeField(FProperty property, Attributes attributes) {
		super(property, attributes);
	}
	
	@Override
  public boolean copyGuiToMemory() {
		String value = getValue();
	  if(getFocData() instanceof FTime){
	  	if(!value.contains(":")){
	  		 if(value.length() == 4){
	  			 value = value.substring(0,2) + ":" + value.substring(2,4);
	  		 }else if(value.length() == 3){
	  			 value = "0" + value.substring(0,1) + ":" + value.substring(1,3);
	  		 }
	  	}
	  }
  	((FProperty)getFocData()).setString(value);
	  return false;
  }  
}
