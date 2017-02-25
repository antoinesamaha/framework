package com.foc.plugin;

import com.foc.desc.FocObject;
import com.foc.desc.field.FField;

public abstract class AbstractFocObjectPlugIn implements IFocObjectPlugIn {

	private FocObject focObject = null;
	
	public void setFocObject(FocObject focObject) {
		this.focObject = focObject;
	}

	public FocObject getFocObject() {
		return focObject;
	}

	@Override
	public void dispose() {
		focObject = null;
	}
	
	@Override
	public String getTransactionCodePrefix(String areaPrefix, String transactionPrefix){
		return "";
	}
	
	@Override
	public int getTransactionCodeNumberOfDigits(){
		return -1;//<=0 if you want the standard Everpro behaviour coming from the Transaction Configuration 
	}
	
	@Override
  public int getMandatoryFieldCount(int count_FromFocObject){
		return count_FromFocObject;
	}
	
	@Override
  public FField getMandatoryFieldAt(int i, FField field_FromFocObject){
		return field_FromFocObject;
	}
}
