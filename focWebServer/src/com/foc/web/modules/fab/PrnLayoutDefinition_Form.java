package com.foc.web.modules.fab;

import com.foc.business.printing.PrnLayoutDefinition;
import com.foc.business.printing.ReportFactory;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;

@SuppressWarnings("serial")
public class PrnLayoutDefinition_Form extends FocXMLLayout{
	
	public PrnLayoutDefinition getPrnLayoutDefinition(){
		return (PrnLayoutDefinition) getFocObject();
	}
	
	@Override
	public boolean validationCommit(FVValidationLayout validationLayout) {
//		getPrnLayoutDefinition().setFileName(getPrnLayoutDefinition())
		boolean error =  super.validationCommit(validationLayout);
		ReportFactory.getInstance().loadReportsFromTable();
		return error;
	}
}
