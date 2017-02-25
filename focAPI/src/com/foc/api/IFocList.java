package com.foc.api;

import java.util.Iterator;

public interface IFocList {
	public Iterator<IFocObject> iFocList_newIterator();
	public IFocObject           iFocList_newFocObject();
	public void                 iFocList_addFocObject(IFocObject iFocObject);
	public void                 iFocList_newAddedFocObject();
	public IFocObject           iFocList_searchByPropertyValue(String fieldName, Object value);
	public IFocObject           iFocList_searchByPropertiesValues(String[] fieldNames, Object[] values);
	
	public boolean              iFocList_validate();
	public void                 iFocList_cancel();
	public void                 iFocList_dispose();
}
