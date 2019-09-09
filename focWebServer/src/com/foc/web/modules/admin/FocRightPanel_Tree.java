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
package com.foc.web.modules.admin;

import com.foc.Globals;
import com.foc.vaadin.gui.components.FVTreeTable;
import com.foc.vaadin.gui.layouts.FVTableWrapperLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.vaadin.ui.Table.TableDragMode;

@SuppressWarnings("serial")
public class FocRightPanel_Tree extends FocXMLLayout {


	@Override
	protected void afterLayoutConstruction() {
		super.afterLayoutConstruction();
		
		
		FVTableWrapperLayout tableWrapperLayout = (FVTableWrapperLayout) getComponentByName("RIGHT_PANEL_TREE");
		
		if(tableWrapperLayout != null){
			FVTreeTable treeTable = (FVTreeTable) tableWrapperLayout.getTableOrTree();
			
			if(treeTable != null){
				treeTable.setDragMode(TableDragMode.ROW);
			}
		}
		else{
			Globals.logString("Tree not found in right-side panel. Check GuiTree name in FocRightPanel_Tree.xml");
		}

	}
	
}
