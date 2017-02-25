package com.foc.link;

import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class FocLinkInRights extends FocObject {

	public FocLinkInRights(FocConstructor constr) {
		super(constr);
		newFocProperties();
	}

	public FocList getDetailsList(){
		FocList focList = getPropertyList(FocLinkInRightsDesc.FLD_DETAILS_LIST);
		if(focList != null){
			focList.setDirectImpactOnDatabase(true);
			focList.setDirectlyEditable(false);
		}
		return focList;
	}
}
