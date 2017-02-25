package com.fab.model.table;

import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;

public class FabDictionaryGroup extends FocObject {
	
	public FabDictionaryGroup(FocConstructor constr){
		super(constr);
		newFocProperties();
	}
	
	public String getName(){
		return getPropertyString(FabDictionaryGroupDesc.FLD_NAME);
	}
}
