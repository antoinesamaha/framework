package com.foc.web.modules.workflow;

import com.foc.vaadin.gui.components.FVTreeTable;
import com.foc.vaadin.gui.components.TableTreeDelegate;
import com.foc.vaadin.gui.layouts.FVTableWrapperLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;

@SuppressWarnings("serial")
public class Department_Selection_Tree extends FocXMLLayout{

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
