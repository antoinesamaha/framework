package com.foc.vaadin.gui.components.tableAndTree;

import com.foc.desc.FocObject;

public interface ITableListener<GenTableItem extends FocObject> {
  public void openItem(GenTableItem focObject);
  public void addItem(GenTableItem fatherObject, GenTableItem focObject);
  public void deleteItem(GenTableItem focObject);
  public void dispose();
}