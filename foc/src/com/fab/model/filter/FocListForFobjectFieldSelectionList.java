package com.fab.model.filter;

import com.foc.Globals;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocDesc;
import com.foc.list.FocLink;
import com.foc.list.FocListWithFilter;
import com.foc.list.filter.FocListFilterBindedToList;

public class FocListForFobjectFieldSelectionList extends FocListWithFilter {
	private int filterRef = -1;

	public FocListForFobjectFieldSelectionList(int filterRef, FocLink focLink) {
		super(null, focLink);
		if(filterRef > 0){
			setFilterRef(filterRef);
		}
		setListOrder();
	}
	
	private void setFilterRef(int filterRef){
		this.filterRef = filterRef;
	}
	
	private int getFilterRef(){
		return this.filterRef;
	}
	
	private void createFilterIfNull(){
		UserDefinedFilter filter = (UserDefinedFilter)getFocListFilter();
		if(filter == null){
			FocDesc focDesc = getFocDesc();
			String filterFocDescName = FocListFilterBindedToList.getFilterTableName(focDesc.getStorageName());
			if(filterFocDescName != null){
				FocDesc filterFocDesc = Globals.getApp().getFocDescByName(filterFocDescName);
				FocConstructor constr = new FocConstructor(filterFocDesc, null);
				filter = (UserDefinedFilter)constr.newItem();
				setFocListFilter(filter);
			}
		}
	}
	
	public void reloadFromDB() {
		UserDefinedFilter filter = (UserDefinedFilter)getFocListFilter();
		if(filter == null){
			loadFilterByReference(getFilterRef());
		}
  	super.reloadFromDB();
  }
	
	public FocListFilterBindedToList loadFilterByReference(int filterRef){
		createFilterIfNull();
		return super.loadFilterByReference(filterRef);
  }
}
