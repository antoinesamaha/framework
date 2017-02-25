package com.foc.menuStructure;

import com.foc.desc.field.FBoolField;
import com.foc.menuStructure.autoGen.AutoGen_FocMenuItemDesc;

public class FocMenuItemDesc extends AutoGen_FocMenuItemDesc {
	
	 public static final int FLD_HAS_ACCESS              =  20;
	 
	
	public FocMenuItemDesc(){
		super();
		setWithObjectTree();
		setDbResident(NOT_DB_RESIDENT);
		
		FBoolField bField = new FBoolField("HAS_ACCESS", "Has access", FLD_HAS_ACCESS, false);
		bField.setDefaultStringValue("true");
		addField(bField);
	}
	
}
