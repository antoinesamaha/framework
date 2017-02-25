package com.fab.model.table.underlyingCustomisation;

import com.fab.model.table.FieldDefinition;
import com.foc.desc.FocConstructor;

@SuppressWarnings("serial")
public class UndCustField extends FieldDefinition {
	
	public UndCustField(FocConstructor constr){
		super(constr);
	}

	@Override 
	public int getID(){
		int id = super.getID();
		id += 1000;
		return id;
	}

	public boolean isNotPhysicalDifference(){
		return getPropertyBoolean(UndCustFieldDesc.FLD_NOT_PHYSICAL_DIFFERENCE);
	}

	public void setNotPhysicalDifference(boolean b){
		setPropertyBoolean(UndCustFieldDesc.FLD_NOT_PHYSICAL_DIFFERENCE, b);
	}
	
	public String getIdentificationPrefix(){
		return getPropertyString(UndCustFieldDesc.FLD_IDENTIFICATION_PREFIX);
	}
	
	public String getIdentificationSuffix(){
		return getPropertyString(UndCustFieldDesc.FLD_IDENTIFICATION_SUFFIX);
	}
}
