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
import com.foc.IFocEnvironment;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;

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

	@Override
	public boolean validateDataBeforeCommit(FVValidationLayout validationLayout) {
		boolean error = super.validateDataBeforeCommit(validationLayout);
		if(!error) {
			FieldDefinition fieldDefinition = getFieldDefinition();
			//Check when size is mandatory
			if (		fieldDefinition.getSQLType() == FieldDefinition.SQL_TYPE_ID_CHAR_FIELD 
					|| 	fieldDefinition.getSQLType() == FieldDefinition.SQL_TYPE_ID_INT
					|| 	fieldDefinition.getSQLType() == FieldDefinition.SQL_TYPE_ID_DOUBLE) {
				if(fieldDefinition.getLength() == 0) {
					error = true;
					Globals.showNotification("Please enter 'Size' value", "", IFocEnvironment.TYPE_WARNING_MESSAGE);
				}
			}
		}
		return error;
	}
}
