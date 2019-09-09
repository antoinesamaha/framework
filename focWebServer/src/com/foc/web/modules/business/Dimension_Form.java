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
package com.foc.web.modules.business;

import com.foc.desc.FocObject;
import com.foc.vaadin.gui.components.FVObjectComboBox;
import com.foc.vaadin.gui.components.ITableTree;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;

@SuppressWarnings("serial")
public class Dimension_Form extends FocXMLLayout {
	
	private FocObject unitObject = null;
	
	@Override
	public void dispose() {
		if(unitObject != null){
			unitObject.dispose();
			unitObject = null;
		}
	}
	
	@Override
	public FocObject table_AddItem(String tableName, ITableTree table, FocObject fatherObject) {
		FocObject focObject =  super.table_AddItem(tableName, table, fatherObject);
		Dimension_Table dimension_Table = (Dimension_Table) findAncestor(FocXMLLayout.class);
		dimension_Table.setUnitObject(focObject);
		return focObject;
	}
	

	public FocObject getUnitObject() {
		return unitObject;
	}

	public void setUnitObject(FocObject focObject) {
		this.unitObject = focObject;
	}
}
