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
