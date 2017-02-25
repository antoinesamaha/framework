package com.fab.model.filter;

import com.fab.FilterFocDescDeclaration;
import com.foc.Globals;
import com.foc.list.filter.FilterDesc;
import com.foc.list.filter.FocDescForFilter;

public class FocDescForUserDefinedFilter extends FocDescForFilter {
	
	public FocDescForUserDefinedFilter(Class focObjectClass, boolean dbResident, String storageName, boolean isKeyUnique){
		super(focObjectClass, dbResident, storageName, isKeyUnique);
	}

	@Override
	public FilterDesc getFilterDesc() {
		if(this.filterDesc == null){
			FilterFocDescDeclaration filterFocDescDeclaration = (FilterFocDescDeclaration)Globals.getApp().getIFocDescDeclarationByName(getStorageName());
			if(filterFocDescDeclaration != null){
				this.filterDesc = filterFocDescDeclaration.getFilterDesc_CreateIfNeeded();
			}
		}
		return this.filterDesc;
	}

}
