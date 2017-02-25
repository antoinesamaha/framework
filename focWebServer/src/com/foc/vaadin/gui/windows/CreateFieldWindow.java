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
