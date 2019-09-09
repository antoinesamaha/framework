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
package com.foc.vaadin.gui.components.treeGrid;

import com.foc.vaadin.gui.components.FVTableColumn;
import com.vaadin.ui.TextField;

public class FVEditorField_Numeric extends TextField {

	private FVTableColumn tableColumn = null;
	
	public FVEditorField_Numeric(FVTableColumn tableColumn){
		this.tableColumn = tableColumn;
	}
	
	public void dispose(){
		tableColumn = null;
	}

	public boolean isColumnEditable(){
		return tableColumn == null || tableColumn.isColumnEditable();
	}
	
	@Override
	public void setReadOnly(boolean readOnly) {
		//The read only parameter takes into  account what FProperty says about isReadOnly() which takes into account 
		//1- The Property.isValueLocked()
		//2- The FField.isEditable()
		//3- The FocObject.isPropertyLocked()
		
		//We want the set enabled to take into account the readOnly + the GuiColumn if it says editable="false"
		//Even for the the read only coming from properies the setEnabled(false) is more elegant because it shows that the 
		//component is dull.
		setEnabled(isColumnEditable() && !readOnly);
		
		super.setReadOnly(readOnly);
	}
	
	/*
	private FProperty getFProperty(){
		FProperty fProp = null;
		
		Property prop = getPropertyDataSource();
		if(prop instanceof TransactionalPropertyWrapper){
			TransactionalPropertyWrapper trnsactional = (TransactionalPropertyWrapper) prop;
			fProp = (FProperty) trnsactional.getWrappedProperty();
		}
		return fProp;
	}
	*/
}
