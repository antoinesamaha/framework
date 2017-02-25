package com.fab.parameterSheet;

import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;

public class ParameterSheetSelector extends FocObject{
	
	public ParameterSheetSelector(FocConstructor constr){
		super(constr);
		newFocProperties();
	}
		
	public int getParameterSetID(){
		return getReference().getInteger();
	}
	
	public void setParameterSetName(String name){
		setPropertyString(ParameterSheetSelectorDesc.FLD_PARAM_SET_NAME, name);
	}
	
	public String getParameterSetName(){
		return getPropertyString(ParameterSheetSelectorDesc.FLD_PARAM_SET_NAME);
	}
	
	public void setTableName(String tableName){
		setPropertyString(ParameterSheetSelectorDesc.FLD_TABLE_NAME, tableName);
	}
	
	public String getTableName(){
		return getPropertyString(ParameterSheetSelectorDesc.FLD_TABLE_NAME);
	}
}
