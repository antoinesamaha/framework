package com.foc.vaadin.gui.components.tableAndTree;

import com.foc.desc.FocObject;

public abstract class TableListenerAbstract<GenTableItem extends FocObject> implements ITableListener<GenTableItem> {

  @Override
  public void openItem(GenTableItem focObject) {
    
  }

  @Override
  public void addItem(GenTableItem fatherObject, GenTableItem focObject) {
    
  }

  @Override
  public void deleteItem(GenTableItem focObject) {
    
  }
  
  @Override
  public void dispose(){
  	
  }
}