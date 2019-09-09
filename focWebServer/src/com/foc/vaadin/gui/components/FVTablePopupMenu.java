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

import com.foc.desc.FocObject;
import com.vaadin.event.Action;
import com.vaadin.server.Resource;

@SuppressWarnings("serial")
public abstract class FVTablePopupMenu extends Action {
  
  public abstract void actionPerformed(FocObject focObject);
  
  private boolean allowThisMenu = false;
  private int actionId = TableTreeDelegate.ACTION_NONE;
  
  public FVTablePopupMenu(String caption){
  	this(TableTreeDelegate.ACTION_NONE, caption);
  }
  
  public FVTablePopupMenu(int actionId, String caption){
    super(caption);
    this.actionId = actionId;
  }
  
  public FVTablePopupMenu(String caption, boolean allowThisMenu){
    super(caption);
    this.allowThisMenu = allowThisMenu;
  }
  
  public FVTablePopupMenu(String caption, Resource icon) {
    super(caption, icon);
  }
  
  public void dispose(){
  	
  }
  
  public boolean isVisible(FocObject focObject){
    return true;
  }
  
  public boolean isAllowThisMenu(){
  	return allowThisMenu;
  }

	public int getActionId() {
		return actionId;
	}
}
