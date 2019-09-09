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
package com.foc.vaadin.gui.menuTree;

import com.foc.Globals;
import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.menuStructure.FocMenuItem;
import com.foc.menuStructure.IFocMenuItemAction;
import com.foc.util.Utils;

public abstract class FocMenuItemAbstractAction_WithAddCommand implements IFocMenuItemAction { 
  public abstract FocList getFocList();
  
  private boolean isPopup = false;
  
  private boolean withAdd = false;
  
  private String  storage = null;
  private int     type    = -1;
  private String  context = null;
  private String  view    = null;
  
  public FocMenuItemAbstractAction_WithAddCommand(FocMenuItem menuItem) {
    this(menuItem, "Add");
  }

  public FocMenuItemAbstractAction_WithAddCommand(FocMenuItem menuItem, String addActionTitle) {
  	if(!Utils.isStringEmpty(addActionTitle)){
  		withAdd = true;
  		menuItem.setExtraAction0(addActionTitle);
  	}
  }

  public FocMenuItemAbstractAction_WithAddCommand(FocMenuItem menuItem, String addActionTitle, String context) {
  	if(!Utils.isStringEmpty(addActionTitle)){
  		withAdd = true;
  		menuItem.setExtraAction0(addActionTitle);
  	}
    setContext(context);
  }

  public FocMenuItemAbstractAction_WithAddCommand(FocMenuItem menuItem, String addActionTitle, String storage, int type, String context, String view) {
  	if(!Utils.isStringEmpty(addActionTitle)){
  		withAdd = true;
  		menuItem.setExtraAction0(addActionTitle);
  	}
  	setStorage(storage);
  	setType(type);
  	setContext(context);
  	setView(view);
  }

  /**
   * 
   * @param navigationWindow
   * @param menuItem
   * @param extraActionIndex
   */
  public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
    FocList focList = getFocList();
    if (focList != null) {
      focList.loadIfNotLoadedFromDB();

      if (extraActionIndex == 1 && withAdd) {
        FocObject focObject = (FocObject) focList.newEmptyItem();
        Globals.popup(focObject, isPopup(), getStorage(), getType(), getContext(), getView());
      } else {
      	Globals.popup(focList, false, getStorage(), -1, getContext(), getView());
      }
    }
  }

  public boolean isPopup(){
    return isPopup;
  }
  
  public void setPopup(boolean bool){
    isPopup = bool;
  }
  
	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

	public String getStorage() {
		return storage;
	}

	public void setStorage(String storage) {
		this.storage = storage;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isWithAdd() {
		return withAdd;
	}

	public void setWithAdd(boolean withAdd) {
		this.withAdd = withAdd;
	}
}
