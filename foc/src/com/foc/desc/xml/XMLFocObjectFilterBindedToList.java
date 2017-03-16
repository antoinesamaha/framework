package com.foc.desc.xml;

import com.foc.desc.FocConstructor;
import com.foc.list.filter.FocListFilterBindedToList;

public class XMLFocObjectFilterBindedToList extends FocListFilterBindedToList {

	public XMLFocObjectFilterBindedToList(FocConstructor constr) {
		super(constr);
    newFocProperties();
    
    XMLFocDesc focDesc = (XMLFocDesc) getThisFocDesc();
    XMLFocDescParser parser = focDesc != null ? focDesc.getFocDescParser() : null;
    int dbLevel = FocListFilterBindedToList.LEVEL_DATABASE;
    if(parser != null){
    	dbLevel = parser.getXmlFilter().getFilterLevel();
    }
    setFilterLevel(dbLevel);
	}

}
