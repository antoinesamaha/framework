package com.foc.vaadin.gui.components;

import com.foc.dataWrapper.FocDataWrapper;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;
import com.vaadin.event.ItemClickEvent.ItemClickListener;

public interface ITableTree {
	public static final int VIEW_CONTAINER_NOT_SET      = -1;
	public static final int VIEW_CONTAINER_SAME_WINDOW  =  0;
	public static final int VIEW_CONTAINER_NONE         =  1;
	public static final int VIEW_CONTAINER_POPUP        =  2;
	public static final int VIEW_CONTAINER_INNER_LAYOUT =  3;
	
  //public FVTableColumn addColumn(Attributes attribute);
  public FVCheckBox        getEditableCheckBox();
  public void              setEditable(boolean editable);
  public FocDesc           getFocDesc();
  public FocList           getFocList();
  public FocDataWrapper    getFocDataWrapper();
  public void              open(FocObject focObject);
  public void              delete(int ref);
  public FVTableColumn     addColumn(FocXMLAttributes attributes);
  public TableTreeDelegate getTableTreeDelegate();
  public void              applyFocListAsContainer();
  public void              computeFooter(FVTableColumn col);
  public void              refreshRowCache_Foc();
  public void              afterAddItem(FocObject fatherObject, FocObject newObject);
  public boolean           setRefreshGuiDisabled(boolean disabled);
  public FocObject         getSelectedObject();
  public void              addItemClickListener(ItemClickListener itemClickListener);
}