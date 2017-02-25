package com.foc.plugin;

import com.foc.desc.field.FField;

public interface IFocObjectPlugIn {
	public void   dispose();
	public String getTransactionCodePrefix(String areaPrefix, String transactionPrefix);
	public int    getTransactionCodeNumberOfDigits();
  public int    getMandatoryFieldCount(int count_FromFocObject);
  public FField getMandatoryFieldAt(int i, FField field_FromFocObject);
  
}
