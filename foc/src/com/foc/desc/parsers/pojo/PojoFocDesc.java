package com.foc.desc.parsers.pojo;

import com.foc.desc.field.FField;
import com.foc.desc.parsers.ParsedFocDesc;
import com.foc.list.filter.IFocDescForFilter;

public class PojoFocDesc extends ParsedFocDesc implements IFocDescForFilter {

	public PojoFocDesc(Class<PojoFocObject> focObjectClass, boolean dbResident, String storageName, boolean isKeyUnique){
		super(focObjectClass, dbResident, storageName, isKeyUnique, false);
		addReferenceField();
	}
	
	public void dispose(){
		super.dispose();
	}
	
	public void setRefFieldNotDBRsident() {
		FField fld = getFieldByID(FField.REF_FIELD_ID);
		fld.setDBResident(false);
	}
}
