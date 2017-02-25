package com.foc.gui.table;

import com.foc.desc.FocObject;
import com.foc.property.FProperty;

public interface FTableCellStartEditingListener {
  public void dispose();
  public boolean requestToEditCell(FocObject focObject, FProperty property);
}
