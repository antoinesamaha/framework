package com.foc.business.division;

import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;

@SuppressWarnings("serial")
public class Division extends FocObject{

	public Division(FocConstructor constr){
    super(constr);
    newFocProperties();
  }
  
  public boolean isEndDivision(){
  	return getPropertyBoolean(DivisionDesc.FLD_END_DIVISION);
  }
}
