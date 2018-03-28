package com.foc.web.modules.business;

import com.foc.business.dateShifter.DateShifter;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout_Filter;
import com.foc.vaadin.gui.xmlForm.IValidationListener;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLView;

public class DateShifter_Form extends FocXMLLayout {
	
	private FocXMLLayout_Filter filterLayout = null;

	@Override
	public void init(INavigationWindow window, XMLView xmlView, IFocData focData) {
		super.init(window, xmlView, focData);
		if(getDateShifter() != null) getDateShifter().adjustDate();
	}
	
	public DateShifter getDateShifter(){
		return (DateShifter) getFocData();
	}
	
	public FocXMLLayout_Filter getFilterLayout() {
		return filterLayout;
	}

	public void setFilterLayout(FocXMLLayout_Filter filterLayout) {
		this.filterLayout = filterLayout;
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
				if(getFilterLayout() != null) {
				  if(getFilterLayout().getFilter() != null) {
				  	getFilterLayout().getFilter().adjustDatesAccordingToShifters();
				  }
				  getFilterLayout().copyMemoryToGui();
				}
//				if(getDateShifter() != null) getDateShifter().adjustDate();
			}
		});
	}

}
