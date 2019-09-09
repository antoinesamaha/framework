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
package com.foc.vaadin.gui.components.tableAndTree;

import com.foc.dataWrapper.FocDataWrapper;
import com.foc.util.ASCII;
import com.foc.vaadin.gui.components.FVTableColumn;
import com.foc.vaadin.gui.components.ITableTree;
import com.foc.vaadin.gui.components.TableTreeDelegate;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;

@SuppressWarnings("serial")
public class FVColumnGenerator implements ColumnGenerator {

	private FVTableColumn tableColumn = null;
	private String        tableName   = null;
	
	public FVColumnGenerator(FVTableColumn tableColumn){
		this.tableColumn = tableColumn;
	}
	
	public void dispose(){
		tableColumn = null;
	}

	public FVTableColumn getTableColumn() {
		return tableColumn;
	}
	
	public Table getTable(){
		return (Table)getTreeOrTable();
	}
	
	public ITableTree getTreeOrTable(){
		ITableTree tableTree = null;
		FVTableColumn tableColumn = getTableColumn();
		if(tableColumn != null){
			tableTree = tableColumn.getTableOrTree();
		}
		return tableTree;
	}
	
	public TableTreeDelegate getTableTreeDelegate(){
		return getTreeOrTable() != null ? getTreeOrTable().getTableTreeDelegate() : null;		
	}
	
	public FocXMLLayout getFocXMLLayout(){
		FocXMLLayout focXMLLayout = null;
		TableTreeDelegate tableTreeDelegate = getTableTreeDelegate();
		if(tableTreeDelegate != null){
			focXMLLayout = tableTreeDelegate.getFocXMLLayout();
		}
		return focXMLLayout;
	}
	
	public FocDataWrapper getFocDataWrapper(){
		FocDataWrapper focDataWrapper =null;
		ITableTree tableTree = getTreeOrTable();
		if(tableTree != null){
			focDataWrapper = tableTree.getFocDataWrapper();
		}
		return focDataWrapper;
	}
	
	public FocXMLAttributes getTableTreeAttributes(){
		FocXMLAttributes attributes = null;
		TableTreeDelegate tableTreeDelegate = getTableTreeDelegate();
		if(tableTreeDelegate != null){
			attributes = tableTreeDelegate.getAttributes();
		}
		return attributes;
	}
	
	public String getTableName() {
		if(tableName == null){
			FocXMLAttributes attributes = getTableTreeAttributes();
			tableName = attributes != null ? attributes.getValue(FXML.ATT_NAME) : "";
			if(tableName == null){
				tableName = ASCII.generateRandomString(10);
			}
		}
		return tableName;
	}
	
	@Override
	public Object generateCell(Table source, Object itemId, Object columnId) {
		Object value = null;
		
		return value;
	}
}
