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
package com.foc.web.modules.fab;

import com.fab.model.table.TableDefinition;
import com.foc.Globals;
import com.foc.list.FocList;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.gui.RightPanel;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.vaadin.gui.components.FVTableColumn;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.server.xmlViewDictionary.XMLView;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;

@SuppressWarnings("serial")
public class FabTableDefinition_Table extends FocXMLLayout {

  @Override
  public ColumnGenerator table_getGeneratedColumn(String tableName, FVTableColumn tableColumn) {
  	ColumnGenerator colGenerator = super.table_getGeneratedColumn(tableName, tableColumn);
		if(tableColumn.getName().equals("FORM_XML")) {
			colGenerator = new ColumnGenerator() {
				@Override
				public Object generateCell(Table source, Object itemId, Object columnId) {
					long objId = (Long) itemId;
					FocList list = getFocList();
					if (list != null) {
						TableDefinition tableDefinition = (TableDefinition) list.searchByReference(objId);

						if (tableDefinition != null) {
							return new OpenXMLButton(tableDefinition, true);
						}
					}
					return null;
				}
			};
		}else if(tableColumn.getName().equals("TABLE_XML")) {
			colGenerator = new ColumnGenerator() {
				@Override
				public Object generateCell(Table source, Object itemId, Object columnId) {
					long objId = (Long) itemId;
					FocList list = getFocList();
					if (list != null) {
						TableDefinition tableDefinition = (TableDefinition) list.searchByReference(objId);

						if (tableDefinition != null) {
							return new OpenXMLButton(tableDefinition, false);
						}
					}
					return null;
				}
			};			
		} 
  		
  	return colGenerator;
  }
	
	public class OpenXMLButton extends FVButton {
		
		private TableDefinition tableDef = null;
		private boolean isForm = false;;
		
		public OpenXMLButton(TableDefinition tableDef, boolean isForm) {
			super(isForm ? " Form XML " : "Table XML");
			this.tableDef = tableDef;
			this.isForm = isForm;
			
			addClickListener(new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					XMLViewKey xmlViewKey = new XMLViewKey(tableDef.getName(), isForm ? XMLViewKey.TYPE_FORM : XMLViewKey.TYPE_TABLE);
					XMLView view = XMLViewDictionary.getInstance().get_CreateIfNeeded_WithValidationSettings(xmlViewKey, tableDef);

					String xmlContent = view.getXMLString();
					if(xmlContent != null){
						Globals.logString("XML Before popup0"+xmlContent);
						RightPanel.popupXmlEditor(view, xmlContent);
					}
				}
			});
		}
	}
  
}
