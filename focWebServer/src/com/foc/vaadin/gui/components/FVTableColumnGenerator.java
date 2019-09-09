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

import com.foc.vaadin.gui.xmlForm.FXML;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;

public abstract class FVTableColumnGenerator implements ColumnGenerator {

	public abstract Object focGenerateCell(Table source, Object itemId, Object columnId);
	
	private FVTableColumn tableColumn = null;
	
	public FVTableColumnGenerator(FVTableColumn tableColumn){
		this.tableColumn = tableColumn;
	}
	
	public void dispose(){
		tableColumn = null;
	}

	public FVTableColumn getTableColumn() {
		return tableColumn;
	}
	
	@Override
	public Object generateCell(Table source, Object itemId, Object columnId) {
		Object value = focGenerateCell(source, itemId, columnId) ;
		if(value instanceof String && getTableColumn() != null && getTableColumn().getAttributes() != null){
			FVLabel lbl = null;
			String styleAttrib = getTableColumn().getAttributes().getValue(FXML.ATT_STYLE);
			if(styleAttrib != null && !styleAttrib.isEmpty()){
				lbl = new FVLabel((String)value);
				lbl.parseStyleAttributeValue(styleAttrib);
				value = lbl;
			}
		}
		
		return value;
	}
	
}
