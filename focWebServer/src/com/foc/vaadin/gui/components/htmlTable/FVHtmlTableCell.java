package com.foc.vaadin.gui.components.htmlTable;

import com.foc.desc.FocObject;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.components.FVTableColumn;

public class FVHtmlTableCell {

	private FVHtmlTableRow htmlTableRow = null;
	private FVTableColumn tableColumn = null;
	
	public FVHtmlTableCell(FVHtmlTableRow htmlTableRow, FVTableColumn tableColumn) {
		this.htmlTableRow = htmlTableRow;
		this.tableColumn = tableColumn;
	}
	
	public void dispose(){
		htmlTableRow = null;
		tableColumn = null;
	}

	public void drawCell(){
		if(getHtmlTableTags() != null){
			getHtmlTableTags().createCell(getValue());
		}
	}
	
	public void drawCellAsHeader(String colWidth){
		if(getHtmlTableTags() != null){
			getHtmlTableTags().createHeader(getColumnCaption(), colWidth);
		}
	}
	
	private FVHtmlTableTags getHtmlTableTags(){
		return getHtmlTableRow() != null && getHtmlTableRow().getHtmlTable() != null ? getHtmlTableRow().getHtmlTable().getHtmlTableTags() : null;
	}
	
	private Object getValue(){
		Object   value    = "";
		FocObject focObject = getHtmlTableRow().getFocObject();
		if(focObject != null){
			IFocData iFocData = focObject.iFocData_getDataByPath(getDataPath());
			value    = iFocData != null ? iFocData.iFocData_getValue() : "";
		}
		return value != null ? value : "";
	}
	
	private String getDataPath(){
		return getTableColumn() != null ? getTableColumn().getDataPath() : "";
	}
	
	private String getColumnCaption(){
		return getTableColumn() != null ? getTableColumn().getCaption() : "";
	}
	
	public FVHtmlTableRow getHtmlTableRow() {
		return htmlTableRow;
	}

	public void setHtmlTableRow(FVHtmlTableRow htmlTableRow) {
		this.htmlTableRow = htmlTableRow;
	}

	public FVTableColumn getTableColumn() {
		return tableColumn;
	}
}
