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
package com.foc.web.modules.admin;

import com.foc.db.migration.MigrationSource;
import com.foc.desc.FocObject;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.components.ITableTree;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;

@SuppressWarnings("serial")
public class MigrationSource_Table extends FocXMLLayout{

	@Override
	public ICentralPanel table_OpenItem(String tableName, ITableTree table, FocObject focObject, int viewContainer_Open) {
		super.table_OpenItem(tableName, table, focObject, viewContainer_Open);
		
		if(focObject != null){
			MigrationSource migrationSource = (MigrationSource) focObject;
			migrationSource.getMapFieldList(true);
		}
		/*
		if(focObject != null){
			MigrationSource migrationSource = (MigrationSource) focObject;
			
			FocList list = migrationSource.getMapFieldList();
			
			XMLViewKey xmlViewKey = new XMLViewKey(MigrationSourceDesc.getInstance().getStorageName(), XMLViewKey.TYPE_FORM, AdminWebModule.CTXT_MIGRATION_SOURCE_SET, XMLViewKey.VIEW_DEFAULT);
			ICentralPanel centralPanel = XMLViewDictionary.getInstance().newCentralPanel(getMainWindow(), xmlViewKey, focObject);
			getMainWindow().changeCentralPanelContent(centralPanel, false);
		}
		*/
		return null;
	}
}
