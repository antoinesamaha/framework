package com.foc.web.modules.admin;

import com.foc.admin.FocAppGroup;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;

@SuppressWarnings("serial")
public class FocAppGroup_Form extends FocXMLLayout {

	private FocAppGroup getAppGroup(){
		return (FocAppGroup) getFocData();
	}
	
	@Override
	public boolean validationCommit(FVValidationLayout validationLayout) {
		boolean bool = super.validationCommit(validationLayout);
		if(getAppGroup() != null && getAppGroup().getFocGroup() != null){
			getAppGroup().getFocGroup().validate(true);
		}
		return bool;
	}
}
