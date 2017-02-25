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