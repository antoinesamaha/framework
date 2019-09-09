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

import com.fab.gui.xmlView.XMLViewDefinition;
import com.foc.list.FocList;
import com.foc.vaadin.gui.RightPanel;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.vaadin.gui.components.FVTableColumn;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.server.xmlViewDictionary.XMLView;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;

@SuppressWarnings("serial")
public class XMLViewDefinition_Table extends FocXMLLayout{

	private FocList getXMLViewDefinitionList(){
		return getFocList();
	}
	
  @Override
  public ColumnGenerator table_getGeneratedColumn(String tableName, final FVTableColumn tableColumn) {
  	ColumnGenerator columnGenerator = null;
  	if(tableColumn != null && tableColumn.getName() != null && tableColumn.getName().equals("OPEN_XML_EDITOR")){
  		columnGenerator = new ColumnGenerator() {

  			public Object generateCell(Table source, Object itemId, Object columnId) {
  				FVButton button = null;
  				if(itemId != null){
	  				final long objId = (Long) itemId;
	  				button = new FVButton(tableColumn.getCaption(), new Button.ClickListener() {
							
							@Override
							public void buttonClick(ClickEvent event) {
								FocList xmlViewDefinitionList = getXMLViewDefinitionList();
								if(xmlViewDefinitionList != null){
									XMLViewDefinition xmlViewDefinition = (XMLViewDefinition) xmlViewDefinitionList.searchByReference(objId);
									if(xmlViewDefinition != null){
										RightPanel rightPanel = (RightPanel) getRightPanel(false);
										rightPanel = (RightPanel) (rightPanel == null ? getRightPanel(true) : rightPanel);
										if(rightPanel != null){
											XMLView view = XMLViewDictionary.getInstance().get(xmlViewDefinition);
											if(view != null){
												rightPanel.popupXmlEditor(view, xmlViewDefinition.getXML());
											}
										}
									}
								}
							}
						});
  				}
  				return button;
  			}
  		};
  	}

  	return columnGenerator;
  }
}
