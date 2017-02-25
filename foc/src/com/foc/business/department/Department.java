package com.foc.business.department;

import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;

public class Department extends FocObject {
  public Department(FocConstructor constr){
    super(constr);
    newFocProperties();
  }
  
  public boolean isEndDepartment(){
  	return getPropertyBoolean(DepartmentDesc.FLD_END_DEPARTMENT);
  }
}
