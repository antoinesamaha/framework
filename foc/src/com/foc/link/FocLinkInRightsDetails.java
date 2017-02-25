package com.foc.link;

import com.foc.desc.FocConstructor;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;

@SuppressWarnings("serial")
public class FocLinkInRightsDetails extends FocObject {

	public FocLinkInRightsDetails(FocConstructor constr) {
		super(constr);
		newFocProperties();
	}
	
	public FocDesc getTableDesc(){
		return getPropertyDesc(FocLinkInRightsDetailsDesc.FLD_TABLE_NAME);
	}
	
	public void setTableDesc(FocDesc desc){
		setPropertyDesc(FocLinkInRightsDetailsDesc.FLD_TABLE_NAME, desc);
	}
	
	public String getFieldName(){
		return getPropertyString(FocLinkInRightsDetailsDesc.FLD_FIELD_NAME);
	}
	
	public void setFieldName(String fieldName){
		setPropertyString(FocLinkInRightsDetailsDesc.FLD_FIELD_NAME, fieldName);
	}
	
	public boolean isInsertAllowed(){
		return getPropertyBoolean(FocLinkInRightsDetailsDesc.FLD_INSERT_ALLOWED);
	}
	
	public void setInsertAllowed(boolean allowed){
		setPropertyBoolean(FocLinkInRightsDetailsDesc.FLD_INSERT_ALLOWED, allowed);
	}
	
	public boolean isDeleteAllowed(){
		return getPropertyBoolean(FocLinkInRightsDetailsDesc.FLD_DELETE_ALLOWED);
	}
	
	public void setDeleteAllowed(boolean allowed){
		setPropertyBoolean(FocLinkInRightsDetailsDesc.FLD_DELETE_ALLOWED, allowed);
	}
	
	public boolean isUpdateAllowed(){
		return getPropertyBoolean(FocLinkInRightsDetailsDesc.FLD_UPDATE_ALLOWED);
	}
	
	public void setUpdateAllowed(boolean allowed){
		setPropertyBoolean(FocLinkInRightsDetailsDesc.FLD_UPDATE_ALLOWED, allowed);
	}

}
