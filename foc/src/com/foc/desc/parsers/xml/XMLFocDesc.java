package com.foc.desc.parsers.xml;

import com.foc.desc.FocModule;
import com.foc.desc.parsers.ParsedFocDesc;
import com.foc.list.filter.IFocDescForFilter;

public class XMLFocDesc extends ParsedFocDesc implements IFocDescForFilter {

	private XMLFocDescParser focDescParser = null;
	
	public XMLFocDesc(FocModule module, String storageName, String xmlFullFileName, Class<XMLFocObject> focObjectClass){
		this(focObjectClass, DB_RESIDENT, storageName, false, false);
	}
	
	public XMLFocDesc(Class focObjectClass, boolean dbResident, String storageName, boolean isKeyUnique, boolean withWorkfllow) {
		super(focObjectClass, dbResident, storageName, isKeyUnique, withWorkfllow);
	}
	
	public void dispose(){
		super.dispose();
	}

	public XMLFocDescParser getFocDescParser() {
		return focDescParser;
	}

	public void setFocDescParser(XMLFocDescParser focDescParser) {
		this.focDescParser = focDescParser;
	}
}
