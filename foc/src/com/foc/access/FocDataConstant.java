package com.foc.access;

import com.foc.shared.dataStore.IFocData;

public class FocDataConstant implements IFocData {
  private String value = "";

  public FocDataConstant(String value){
    this.value = value;
  }
  
  @Override
  public boolean iFocData_isValid() {
    return true;
  }

  @Override
  public boolean iFocData_validate() {
    return false;
  }

  @Override
  public void iFocData_cancel() {
  }

  @Override
  public IFocData iFocData_getDataByPath(String path) {
    return null;
  }

  @Override
  public Object iFocData_getValue() {
    return value;
  }

  @Override
  public void dispose() {
  }
  
}
