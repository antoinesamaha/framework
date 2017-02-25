package com.foc.vaadin.gui.components.tableAndTree;

import com.foc.dataWrapper.FocDataWrapper;
import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.vaadin.gui.components.FVTableColumn;
import com.vaadin.ui.Table;

@SuppressWarnings("serial")
public class FVColGen_LineNumber extends FVColumnGenerator {
	
	public FVColGen_LineNumber(FVTableColumn tableColumn) {
		super(tableColumn);
	}
	
	@Override
	public Object generateCell(Table source, Object itemId, Object columnId) {
		Object value = null;
		FocDataWrapper focDataWrapper = getFocDataWrapper();
		if(focDataWrapper != null && getTreeOrTable() != null){
			FocList list = getTreeOrTable().getFocList();
			FocObject focObject = list != null ? list.searchByReference((Integer) itemId) : null;
			value = focDataWrapper.getLineNumberForFocObject(focObject);
		}
		return value;
	}
}
