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

import com.foc.Globals;
import com.foc.desc.FocObject;
import com.foc.desc.field.FField;
import com.foc.desc.field.IPropertyStringConverter;
import com.foc.property.FProperty;
import com.foc.shared.dataStore.IFocData;

@SuppressWarnings("serial")
public class FVLabelInTable extends FVLabel {

	private FVTableColumn column  = null;
	private FProperty     focData = null;
	
  public FVLabelInTable(FProperty property, FocObject focObj, FVTableColumn column) {
  	super("");
  	focData = property;
  	this.column = column;
		getDelegate().setDataPathWithRoot(focObj, column.getDataPath());
  }

  public void dispose(){
  	super.dispose();
    focData = null;
    column = null;
  }
  
  @Override
  public FProperty getFocData() {
    return (FProperty) focData;
  }
  
  @Override
  public void setFocData(IFocData focData) {
  	if(focData instanceof FProperty){
  		this.focData = (FProperty) focData;
  	}
    copyMemoryToGui();
    setAttributes(getAttributes());
  }

	@Override
	public void copyMemoryToGui() {
//		super.copyMemoryToGui();
		String objReturned = "";
		if(focData != null && column != null){
			try{
				objReturned = (String) focData.vaadin_TableDisplayObject(column.getFormat(), column.getCaptionProp());
				
				if(focData instanceof FProperty) {
					FProperty property = focData;
					FField field = property.getFocField();
					
					//If we have a special converter like when we mix Arabic letters...
					if(field != null && objReturned instanceof String) {
						IPropertyStringConverter stringConverter = field.getStringConverter();
						if(stringConverter != null) {
							objReturned = stringConverter.getGuiStringFromMemory(property);
						}
					}
				}
				//-----------------------------------------------------------------
				
			}catch(Exception e){
				Globals.logException(e);
			}
		}
		setValue(objReturned);		
	}
}
