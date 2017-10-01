package com.foc.desc.parsers.xml;

import com.foc.desc.FocConstructor;
import com.foc.list.filter.FocListFilterBindedToList;

public class XMLFocObjectFilterBindedToList extends FocListFilterBindedToList {

	public XMLFocObjectFilterBindedToList(FocConstructor constr) {
		super(constr);
    newFocProperties();
    
    XMLFocDesc focDesc = (XMLFocDesc) getThisFocDesc();
    int dbLevel = FocListFilterBindedToList.LEVEL_DATABASE;
    if(focDesc != null){
    	dbLevel = focDesc.getParsedFilter().getFilterLevel();
    }
    setFilterLevel(dbLevel);
	}

}
