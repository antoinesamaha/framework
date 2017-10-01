package com.foc.desc.parsers.pojo;

import com.foc.desc.FocConstructor;
import com.foc.list.filter.FocListFilterBindedToList;

public class PojoFocObjectFilterBindedToList extends FocListFilterBindedToList {

	public PojoFocObjectFilterBindedToList(FocConstructor constr) {
		super(constr);
    newFocProperties();
    
    PojoFocDesc focDesc = (PojoFocDesc) getThisFocDesc();
    int dbLevel = FocListFilterBindedToList.LEVEL_DATABASE;
    if(focDesc != null){
    	dbLevel = focDesc.getParsedFilter().getFilterLevel();
    }
    setFilterLevel(dbLevel);
	}

}
