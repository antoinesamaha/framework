package com.foc.link;

import com.foc.desc.FocDesc;
import com.foc.desc.field.FField;
import com.foc.list.FocList;

public class FocLinkOutRightsDesc extends FocDesc {

	public static final String DB_TABLE_NAME  = "FOC_LINK_OUT_RIGHTS";
	
	public static final int    FLD_LABEL        = FField.FLD_NAME;
	public static final int    FLD_DETAILS_LIST = 2;
	
	public FocLinkOutRightsDesc() {
		super(FocLinkOutRights.class, FocDesc.DB_RESIDENT, DB_TABLE_NAME, true);
		
		addReferenceField();
		addNameField(true);
	}
	
	public FocList newFocList(){
    FocList list = super.newFocList();
    list.setDirectlyEditable(false);
    list.setDirectImpactOnDatabase(true);
    return list;
  }	
	
	public static FocLinkOutRightsDesc getInstance() {
		return (FocLinkOutRightsDesc) getInstance(DB_TABLE_NAME, FocLinkOutRightsDesc.class);    
	}
}
