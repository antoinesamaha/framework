package com.foc.link;

import com.foc.desc.FocConstructor;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.desc.field.FField;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class FocLinkOutRights extends FocObject {

	public FocLinkOutRights(FocConstructor constr) {
		super(constr);
		newFocProperties();
	}
	
	public String getLabel(){
		return getPropertyString(FocLinkOutRightsDesc.FLD_LABEL);
	}
	
	public void setLabel(String label){
		setPropertyString(FocLinkOutRightsDesc.FLD_LABEL, label);
	}
	
	public FocList getDetailsList(){
		FocList focList = getPropertyList(FocLinkOutRightsDesc.FLD_DETAILS_LIST);
		if(focList != null){
			focList.setDirectImpactOnDatabase(true);
			focList.setDirectlyEditable(false);
		}
		return focList;
	}
	
	public boolean hasRightsForTableDesc(FocDesc desc){
		boolean hasRights = false;
		
		if(getDetailsList() != null && desc != null && desc.getStorageName() != null){
			for(int i=0; i<getDetailsList().size(); i++){
				FocLinkOutRightsDetails detail = (FocLinkOutRightsDetails) getDetailsList().getFocObject(i);
				
				if(detail != null && detail.getTableDesc() != null && detail.getTableDesc().getStorageName() != null){
					if(detail.getTableDesc().getStorageName() == desc.getStorageName()){
						hasRights = true;
						break;
					}
				}
			}
		}
		
		return hasRights;
	}
	
	public boolean hasRightsForFieldInTableDesc(FField field, FocDesc desc){
		boolean hasRights = false;
		
		if(getDetailsList() != null){
			for(int i=0; i<getDetailsList().size(); i++){
				FocLinkOutRightsDetails detail = (FocLinkOutRightsDetails) getDetailsList().getFocObject(i);
				
				if(detail != null){
					if(detail.getTableDesc().getStorageName() == desc.getStorageName() && detail.getFieldName().equals(field.getName())){
						hasRights = true;
						break;
					}
				}
			}
		}
		
		return hasRights;
	}

}
