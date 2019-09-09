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
package com.foc.web.modules.workflow.gui;

import com.foc.vaadin.gui.components.FVTreeTable;
import com.foc.vaadin.gui.components.TableTreeDelegate;
import com.foc.vaadin.gui.layouts.FVTableWrapperLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;

@SuppressWarnings("serial")
public class ACC_DEPARTMENT_Selection_Standard_Tree extends FocXMLLayout{

	@Override
	protected void afterLayoutConstruction() {
		super.afterLayoutConstruction();
	}
	
	public FVTreeTable getDepartmentTree(){
		FVTreeTable treeTable = null;
		FVTableWrapperLayout fvTableWrapperLayout = (FVTableWrapperLayout) getComponentByName("ACC_DEPARTMENT_SELECTION_TREE");
		if(fvTableWrapperLayout != null){
			treeTable = (FVTreeTable) fvTableWrapperLayout.getTableOrTree();
		}
		return treeTable;
	}
	
	public TableTreeDelegate getDepartmentSelectionTableTreeDelegate(){
		return getDepartmentTree() != null ? getDepartmentTree().getTableTreeDelegate() : null;
	}
}
