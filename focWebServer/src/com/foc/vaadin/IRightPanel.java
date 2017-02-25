package com.foc.vaadin;

public interface IRightPanel {
  public static final int PERMISSION_READ_WRITE = 0;
  public static final int PERMISSION_SELECT     = 1;
  public static final int PERMISSION_NOTHING    = 2;
  
  public void dispose();
  public void refresh();
}
