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
