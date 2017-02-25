package com.foc.shared.dataStore;

public interface IFocData {
  
	public static final String DATA_PATH_SIGN = ">";
	
	public boolean   iFocData_isValid();
  public boolean   iFocData_validate();
  public void      iFocData_cancel();
  public IFocData  iFocData_getDataByPath(String path);
  public Object    iFocData_getValue();
  public void      dispose();
}
