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
package com.foc.vaadin.gui.windows;

import com.fab.model.table.FieldDefinition;
import com.fab.model.table.TableDefinition;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.FocWebVaadinWindow;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class CreateFieldWindow extends Window {
  
  public CreateFieldWindow(FocDesc focDesc) {
    setCaption("Create Field");
    setModal(true);
    setWidth("300px");
    setHeight("300px");
    center();
    
    TableDefinition td = TableDefinition.getTableDefinitionForFocDesc(focDesc);
    
    FocList flist = td.getFieldDefinitionList();
    flist.loadIfNotLoadedFromDB();
    
    int maxID = 1;
    
    FocObject obj = flist.newEmptyItem();
    FocList list = (FocList) ((FieldDefinition) obj).getFatherSubject();
    
    for(int i=0; i<list.size(); i++){
      FieldDefinition fieldDef = (FieldDefinition) list.getFocObject(i);
      
      if(fieldDef.getID() >= maxID){
        maxID = fieldDef.getID() + 1;
      }
    }
    
    ((FieldDefinition) obj).setID(maxID);
    ((FieldDefinition) obj).setLength(20);
    flist.add(obj);
    
    XMLViewKey xmlViewKey = new XMLViewKey(obj.getThisFocDesc().getStorageName(), XMLViewKey.TYPE_FORM);
    
    FocXMLLayout focXMLForm = (FocXMLLayout) XMLViewDictionary.getInstance().newCentralPanel((FocWebVaadinWindow)getParent(), xmlViewKey, obj);
    focXMLForm.setHeight("200px");
    
    setContent(focXMLForm);
  }
}
