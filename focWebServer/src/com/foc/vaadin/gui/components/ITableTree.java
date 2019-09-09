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

import com.foc.dataWrapper.FocDataWrapper;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;
import com.vaadin.event.ItemClickEvent.ItemClickListener;

public interface ITableTree {
	public static final int VIEW_CONTAINER_NOT_SET         = -1;
	public static final int VIEW_CONTAINER_SAME_WINDOW     =  0;
	public static final int VIEW_CONTAINER_NONE            =  1;
	public static final int VIEW_CONTAINER_POPUP           =  2;
	public static final int VIEW_CONTAINER_INNER_LAYOUT    =  3;
	public static final int VIEW_CONTAINER_NEW_BROWSER_TAB =  4;
	
  //public FVTableColumn addColumn(Attributes attribute);
  public FVCheckBox        getEditableCheckBox();
  public void              setEditable(boolean editable);
  public FocDesc           getFocDesc();
  public FocList           getFocList();
  public FocDataWrapper    getFocDataWrapper();
  public void              open(FocObject focObject);
  public void              delete(long ref);
  public FVTableColumn     addColumn(FocXMLAttributes attributes);
  public TableTreeDelegate getTableTreeDelegate();
  public void              applyFocListAsContainer();
  public void              computeFooter(FVTableColumn col);
  public void              refreshRowCache_Foc();
  public void              afterAddItem(FocObject fatherObject, FocObject newObject);
  public boolean           setRefreshGuiDisabled(boolean disabled);
  public FocObject         getSelectedObject();
  public void              setSelectedObject(FocObject selectedObject);
  public void              addItemClickListener(ItemClickListener itemClickListener);
}
