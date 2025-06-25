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
package com.foc.vaadin.gui.components;

import java.util.ArrayList;

import org.xml.sax.Attributes;

import com.foc.Globals;
import com.foc.desc.FocDesc;
import com.foc.desc.field.FField;
import com.foc.property.FProperty;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractSelect;

@SuppressWarnings("serial")
public class FVTextFieldAutoComplete extends FVMultipleChoiceComboBox implements FocXMLGuiComponent, AbstractSelect.NewItemHandler{

  public FVTextFieldAutoComplete(FProperty property, Attributes attributes) {
    super(property, attributes);
   	setNewItemsAllowed(true);
    setNewItemHandler(this);
    setImmediate(true);
    setFilteringMode(FilteringMode.CONTAINS);
  }
	
  @Override
  public void dispose() {
  	setNewItemHandler(null);
  	super.dispose();
  }
  
	@Override
  public void addNewItem(String newItemCaption) {
    if(newItemCaption != null){
      addItem(newItemCaption);
      setValue(newItemCaption);
    }
  }
    
  @Override
  protected void fillMultipleChoice(FProperty property){
    if(property != null){
      FField field = (FField) property.getFocField();
      FocDesc focDesc = property.getFocObject() != null ? property.getFocObject().getThisFocDesc() : null;
      if(field != null && focDesc != null){
  			ArrayList<String> arrayList = Globals.getApp().getDataSource().command_Select(focDesc, field.getID(), true, null); // adapt_proofread
  			for(int i=0; i<arrayList.size(); i++){
  				addItem((String)arrayList.get(i));
  			}
      }
    }
  }
}
