package com.foc.vaadin.gui.components.htmlTable;

import java.util.ArrayList;

import com.foc.desc.FocObject;
import com.foc.vaadin.gui.components.FVTableColumn;

public class FVHtmlTableRow {

	private FocObject focObject = null;
	private FVHtmlTable htmlTable = null; 
	private String dataPath = null; 
	private int index = -1;
	private ArrayList<FVHtmlTableCell> htmlTableCellsList = null;
	
	public FVHtmlTableRow(FocObject focObject, FVHtmlTable htmlTable, int index) {
		this.focObject = focObject;
		this.htmlTable = htmlTable;
		this.index = index;
		openRowTag();
	}
	
	public void dispose(){
		htmlTable = null;
		dataPath = null;
		index = -1;
		if(htmlTableCellsList != null){
			for(int i=0; i<htmlTableCellsList.size(); i++){
				FVHtmlTableCell htmlTableCell = htmlTableCellsList.get(i);
				if(htmlTableCell != null){
					htmlTableCell.dispose();
					htmlTableCell = null;
				}
			}
			htmlTableCellsList.clear();
			htmlTableCellsList = null;
		}
	}
	
	public void openRowTag(){
		if(getHtmlTable() != null && getHtmlTable().getHtmlTableTags() != null){
			getHtmlTable().getHtmlTableTags().openRowTag();
		}
	}
	
	public void closeRowTag(){
		if(getHtmlTable() != null && getHtmlTable().getHtmlTableTags() != null){
			getHtmlTable().getHtmlTableTags().closeRowTag();
		}
	}
	
	public void newHeaderCell(FVTableColumn tableColumn){
		FVHtmlTableCell htmlTableCell = new FVHtmlTableCell(this, tableColumn);
		getHtmlTableCellsList().add(htmlTableCell);
		htmlTableCell.drawCellAsHeader(tableColumn.getWidth()+"px");
	}
	
	public FVHtmlTableCell newCell(FVTableColumn tableColumn){
		FVHtmlTableCell htmlTableCell = new FVHtmlTableCell(this, tableColumn);
		getHtmlTableCellsList().add(htmlTableCell);
		htmlTableCell.drawCell();
		return htmlTableCell;
	}
	
	public FVHtmlTable getHtmlTable() {
		return htmlTable;
	}
	
	public void setHtmlTable(FVHtmlTable htmlTable) {
		this.htmlTable = htmlTable;
	}

	public String getDataPath() {
		return dataPath;
	}

	public void setDataPath(String dataPath) {
		this.dataPath = dataPath;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	public ArrayList<FVHtmlTableCell> getHtmlTableCellsList(){
		if(htmlTableCellsList == null){
			htmlTableCellsList = new ArrayList<FVHtmlTableCell>();
		}
		return htmlTableCellsList;
	}

	public FocObject getFocObject() {
		return focObject;
	}

	public void setFocObject(FocObject focObject) {
		this.focObject = focObject;
	}
}
