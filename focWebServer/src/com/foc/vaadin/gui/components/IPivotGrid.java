package com.foc.vaadin.gui.components;

import com.foc.list.FocList;
import com.foc.pivot.FPivotTable;

public interface IPivotGrid extends ITableTree{

	public FPivotTable getPivotTable();
	public void setDataFocList(FocList focList);
	public void setSizeFull();
	public void setSelectable(boolean selectable);
}
