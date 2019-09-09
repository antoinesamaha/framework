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
package com.foc.web.modules.fab;

import com.fab.model.table.FieldDefinition;
import com.foc.Globals;
import com.foc.desc.FocDesc;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLView;

@SuppressWarnings("serial")
public class FieldDefinition_Form extends FocXMLLayout {
	
	public FieldDefinition_Form(){
	}
	
	public FieldDefinition getFieldDefinition(){
		return (FieldDefinition) getFocData();
	}
	
  public void afterApplyClick(){
  	/*
    FieldDefinition fieldDef = (FieldDefinition) getFocData();

    FocDesc focDescToAdapt = Globals.getApp().getFocDescByName(fieldDef.getTableDefinition().getName());
    if(focDescToAdapt != null){
	    fieldDef.addToFocDesc(focDescToAdapt);
	    focDescToAdapt.adaptTableAlone();
	    //getRightPanel().refresh();
	    //focDescToAdapt.allFocObjectArray_AdjustPropertyArray();
    }
    */
  }

	@Override
	protected void afterLayoutConstruction() {
		super.afterLayoutConstruction();		
		FieldDefinition fieldDefinition = getFieldDefinition();
		if(fieldDefinition.isCreated()){
			fieldDefinition.setID_ToMax();
		}
	}
  
}
