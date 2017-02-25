package com.foc.vaadin;

import com.foc.menuStructure.IFocMenuItemAction;

public interface IApplicationConfigurator extends IFocMenuItemAction {
	public String getCode();
	public String getTitle();
	public void   run();
	public void   dispose();
}
