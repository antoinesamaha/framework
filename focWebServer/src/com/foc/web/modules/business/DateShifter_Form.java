package com.foc.web.modules.business;

import com.foc.business.dateShifter.DateShifter;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.vaadin.gui.xmlForm.IValidationListener;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLView;

public class DateShifter_Form extends FocXMLLayout {

	@Override
	public void init(INavigationWindow window, XMLView xmlView, IFocData focData) {
		super.init(window, xmlView, focData);
		if(getDateShifter() != null) getDateShifter().adjustDate();
	}
	
	public DateShifter getDateShifter(){
		return (DateShifter) getFocData();
	}
	
	@Override
	public void showValidationLayout(boolean showBackButton, int position) {
		// TODO Auto-generated method stub
		super.showValidationLayout(showBackButton, position);
		FVValidationLayout vLay = getValidationLayout();
		vLay.addValidationListener(new IValidationListener() {
			@Override
			public void validationDiscard(FVValidationLayout validationLayout) {
				
			}
			
			@Override
			public boolean validationCommit(FVValidationLayout validationLayout) {
				return false;
			}
			
			@Override
			public boolean validationCheckData(FVValidationLayout validationLayout) {
				return false;
			}
			
			@Override
			public void validationAfter(FVValidationLayout validationLayout, boolean commited) {
				if(getDateShifter() != null) getDateShifter().adjustDate();
			}
		});
	}
}
