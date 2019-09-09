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
