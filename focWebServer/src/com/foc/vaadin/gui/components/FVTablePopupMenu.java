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