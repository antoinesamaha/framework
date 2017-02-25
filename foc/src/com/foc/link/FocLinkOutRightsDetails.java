package com.foc.link;

import com.foc.desc.FocConstructor;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;

@SuppressWarnings("serial")
public class FocLinkOutRightsDetails extends FocObject {

	public FocLinkOutRightsDetails(FocConstructor constr) {
		super(constr);
		newFocProperties();
	}
	
	public FocDesc getTableDesc(){
		return getPropertyDesc(FocLinkOutRightsDetailsDesc.FLD_TABLE_NAME);
	}
	
	public void setTableDesc(FocDesc desc){
		setPropertyDesc(FocLinkOutRightsDetailsDesc.FLD_TABLE_NAME, desc);
	}
	
	public String getFieldName(){
		return getPropertyString(FocLinkOutRightsDetailsDesc.FLD_FIELD_NAME);
	}
	
	public void setFieldName(String fieldName){
		setPropertyString(FocLinkOutRightsDetailsDesc.FLD_FIELD_NAME, fieldName);
	}
}
