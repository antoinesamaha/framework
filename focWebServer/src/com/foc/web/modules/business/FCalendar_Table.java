package com.foc.web.modules.business;

import com.foc.desc.FocObject;
import com.foc.vaadin.gui.components.ITableTree;
import com.foc.vaadin.gui.components.TableTreeDelegate;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;

public class FCalendar_Table extends FocXMLLayout {

	@Override
	public FocObject table_AddItem(String tableName, ITableTree table, FocObject fatherObject) {
		int editModeBackup = table.getTableTreeDelegate().getEditingMode();
		table.getTableTreeDelegate().setEditingMode(TableTreeDelegate.MODE_NOT_EDITABLE);
		FocObject obj = super.table_AddItem(tableName, table, fatherObject);
		table.getTableTreeDelegate().setEditingMode(editModeBackup);
		return obj;
	}
}
