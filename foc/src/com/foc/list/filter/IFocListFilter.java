package com.foc.list.filter;

import java.util.ArrayList;

import com.foc.desc.FocObject;
import com.foc.list.FocListWithFilter;

public interface IFocListFilter {
	public boolean includeObject(FocObject focObject);
  public boolean isActive();
	public void setActive(boolean active);
	public void setActive(FocListWithFilter focList, boolean active);
	public FilterDesc getThisFilterDesc();
	public ArrayList<Integer> getVisibleArray();

	public boolean isObjectExist(int objectIndex);
	public void addListener(FocListFilterListener listener);
	public void removeListener(FocListFilterListener listener);
}
