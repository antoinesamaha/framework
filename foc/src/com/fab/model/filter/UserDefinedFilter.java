package com.fab.model.filter;

import com.foc.desc.FocConstructor;
import com.foc.list.filter.FocListFilter;
import com.foc.list.filter.FocListFilterBindedToList;

public class UserDefinedFilter extends FocListFilterBindedToList {
	public static final int VIEW_FOR_FILTER_CREATION = 1;
	
	public UserDefinedFilter(FocConstructor constr){
		super(constr);
		newFocProperties();
		setFilterLevel(FocListFilter.LEVEL_DATABASE);
	}
	
	public void dispose(){
		super.dispose();
	}
}
