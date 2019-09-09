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
package com.foc.vaadin.gui.components.validator;

import com.foc.Globals;
import com.vaadin.data.Validator;

@SuppressWarnings("serial")
public class FVStringValidator implements Validator{

	private boolean capital  = false;
	private boolean noSpaces = false;
	
	public FVStringValidator(){
		
	}
	
	@Override
	public void validate(Object value) throws InvalidValueException {
		try{
			String str = (String) value;
			if(isCapital())  str = str.toUpperCase();
			if(isNoSpaces()) str.replaceAll(" ", "_");
		}catch(Exception e){
			Globals.logException(e);
		}
	}

	public boolean isCapital() {
		return capital;
	}

	public void setCapital(boolean capital) {
		this.capital = capital;
	}

	public boolean isNoSpaces() {
		return noSpaces;
	}

	public void setNoSpaces(boolean noSpaces) {
		this.noSpaces = noSpaces;
	}
	
}
