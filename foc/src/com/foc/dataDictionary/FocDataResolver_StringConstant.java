package com.foc.dataDictionary;

import java.util.ArrayList;

import com.foc.shared.dataStore.IFocData;

public class FocDataResolver_StringConstant implements IFocDataResolver {
	
	private String value = null;
	
	public FocDataResolver_StringConstant(String value){
		this.value = value; 
	}
	
	public Object getValue(IFocData focData, ArrayList<String> arguments){
		return value;
	}
}
