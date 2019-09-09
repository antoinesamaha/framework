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

import com.foc.Globals;
import com.foc.vaadin.gui.components.FVTableColumn;
import com.vaadin.ui.Table;

@SuppressWarnings("serial")
public class FVColGen_TreeNodeTitle extends FVColumnGenerator {
	
	public FVColGen_TreeNodeTitle(FVTableColumn tableColumn) {
		super(tableColumn);
	}

	@Override
	public Object generateCell(Table source, Object itemId, Object columnId) {
		String propertyString = null;
		try{
//			FTree tree = null;
//			tree = ((FVTreeTable) getTreeOrTable()).getFTree();
//			FNode node = tree.vaadin_FindNode(itemId);
//			FProperty property = tree != null && node != null ? tree.getTreeSpecialProperty(node) : null;
//			if(property != null){
//				propertyString = (String) property.vaadin_TableDisplayObject(null, null);
//			}else if(node != null){
//				propertyString = node.getDisplayTitle();
//			}
			propertyString = getTableTreeDelegate() != null ? getTableTreeDelegate().getNodeTitleDisplayStringForObjectRef(itemId) : null;
		}catch (Exception e){
			Globals.logException(e);
		}
		return propertyString;
	}
}
