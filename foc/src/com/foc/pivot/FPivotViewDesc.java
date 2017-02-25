package com.foc.pivot;

import com.foc.desc.FocDesc;

public class FPivotViewDesc extends FocDesc implements FPivotConst {
  
  public static final String DB_TABLE_NAME = "PIVOT_VIEW";

	public FPivotViewDesc() {
		super(FPivotView.class, DB_RESIDENT, DB_TABLE_NAME, false);
		
		addReferenceField();
	}
	
	public static FocDesc getInstance(){
	  return getInstance(DB_TABLE_NAME, FPivotViewDesc.class);
	}
	
}
