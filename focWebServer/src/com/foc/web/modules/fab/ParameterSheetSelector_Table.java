package com.foc.web.modules.fab;

import com.fab.parameterSheet.ParameterSheetSelectorDesc;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;

@SuppressWarnings("serial")
public class ParameterSheetSelector_Table extends FocXMLLayout{
	
	@Override
	public boolean validationCommit(FVValidationLayout validationLayout) {
		boolean error = super.validationCommit(validationLayout);
		ParameterSheetSelectorDesc.refreshAllParamSetFieldChoices();
		return error;
	};

}
