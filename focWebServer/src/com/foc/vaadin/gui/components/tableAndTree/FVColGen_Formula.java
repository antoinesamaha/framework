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
package com.foc.vaadin.gui.components.tableAndTree;

import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.vaadin.gui.components.FVLabel;
import com.foc.vaadin.gui.components.FVTableColumn;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Table;

@SuppressWarnings("serial")
public class FVColGen_Formula extends FVColumnGenerator {

	public FVColGen_Formula(FVTableColumn tableColumn) {
		super(tableColumn);
	}

	@Override
	public Object generateCell(Table source, Object itemId, Object columnId) {
		FVTableColumn tableColumn = getTableColumn();
		Object obj = null;
		if(tableColumn != null && getTreeOrTable() != null){
			FocList focList = getTreeOrTable().getFocList();
			FocObject focObject = focList != null ? focList.searchByRealReferenceOnly((Long) itemId) : null;
			if(focObject != null){
				obj = tableColumn.computeFormula_ForFocObject(focObject);
				
				if(obj instanceof Double || obj instanceof Integer){
					FVLabel lbl = new FVLabel("");
					lbl.addStyleName("foc-text-right");
					lbl.setValue(obj+"");
					obj = lbl;
				}
			}
			if(tableColumn.isHtml()){
				obj = new FVLabel(obj+"", ContentMode.HTML);
			}
		}
		return obj;
	}
}
