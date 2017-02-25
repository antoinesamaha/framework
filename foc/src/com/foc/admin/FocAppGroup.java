package com.foc.admin;

import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;

@SuppressWarnings("serial")
public abstract class FocAppGroup extends FocObject {
	public abstract String getTitle();
	
  public static final int FLD_FOC_GROUP = 1;
  
	public FocAppGroup(FocConstructor constr) {
		super(constr);
	}
	
  public FocGroup getFocGroup(){
  	return (FocGroup) getPropertyObject(FLD_FOC_GROUP);
  }
}
