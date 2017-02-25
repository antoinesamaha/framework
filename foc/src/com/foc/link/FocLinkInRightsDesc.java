package com.foc.link;

import com.foc.desc.FocDesc;
import com.foc.list.FocList;

public class FocLinkInRightsDesc extends FocDesc {

	public static final String DB_TABLE_NAME = "FOC_LINK_IN_RIGHTS";

	public static final int    FLD_DETAILS_LIST = 1;
	
	public FocLinkInRightsDesc() {
		super(FocLinkInRights.class, FocDesc.DB_RESIDENT, DB_TABLE_NAME, false);

		addReferenceField();
		addNameField(true);
	}

	public FocList newFocList(){
    FocList list = super.newFocList();
    list.setDirectlyEditable(false);
    list.setDirectImpactOnDatabase(true);
    return list;
  }
	
	public static FocLinkInRightsDesc getInstance() {
		return (FocLinkInRightsDesc) getInstance(DB_TABLE_NAME, FocLinkInRightsDesc.class);    
	}
}
