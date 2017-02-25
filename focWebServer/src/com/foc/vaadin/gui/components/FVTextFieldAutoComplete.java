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
  			ArrayList<String> arrayList = Globals.getApp().getDataSource().command_Select(focDesc, field.getID(), true, null);
  			for(int i=0; i<arrayList.size(); i++){
  				addItem((String)arrayList.get(i));
  			}
      }
    }
  }
}