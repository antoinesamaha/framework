package com.foc.vaadin.gui.xmlForm;

import com.foc.list.filter.FocListFilter;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.gui.components.FVButtonClickEvent;
import com.foc.vaadin.gui.layouts.FVTableWrapperLayout;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;

public class FocXMLLayout_FilterSumary<F extends FocListFilter> extends FocXMLLayout_Filter<F> {
	
	public void button_CONFIGURE_Clicked(FVButtonClickEvent evt) {
		F filter = (F) getFilter();

		FVTableWrapperLayout tableWrapper = getTableWrapperLayout(); 

		XMLViewKey key = new XMLViewKey(filter.getThisFocDesc().getStorageName(), XMLViewKey.TYPE_FORM);
		FocXMLLayout_Filter form = (FocXMLLayout_Filter) XMLViewDictionary.getInstance().newCentralPanel(getMainWindow(), key, filter);
		form.setTableWrapperLayout(tableWrapper);
		form.popupInDialog();

		if(form.getValidationLayout() != null){
			form.getValidationLayout().addValidationListener(new IValidationListener() {
				@Override
				public void validationDiscard(FVValidationLayout validationLayout) {
				}
				
				@Override
				public boolean validationCheckData(FVValidationLayout validationLayout) {
					return false;
				}
				
				@Override
				public void validationAfter(FVValidationLayout validationLayout, boolean commited) {
					copyMemoryToGui_Local();
				}

				@Override
				public boolean validationCommit(FVValidationLayout validationLayout) {
					return false;
				}
			});
		}
	}

	@Override
	public boolean validationCheckData(FVValidationLayout validationLayout) {
		//the simple super method would apply the filer and reload the selected <0 ref 
		//unmatchable
		boolean validation = super.validationCheckData_NoApplyFilter(validationLayout);
		return validation;
	}
}
