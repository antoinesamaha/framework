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
