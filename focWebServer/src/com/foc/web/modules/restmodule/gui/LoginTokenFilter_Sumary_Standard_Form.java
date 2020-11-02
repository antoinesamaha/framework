package com.foc.web.modules.restmodule.gui;

import com.foc.list.filter.FocListFilterBindedToList;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.gui.components.FVButtonClickEvent;
import com.foc.vaadin.gui.layouts.FVTableWrapperLayout;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.vaadin.gui.xmlForm.IValidationListener;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;

@SuppressWarnings("serial")
public class LoginTokenFilter_Sumary_Standard_Form extends LoginTokenFilter_Form {

	public void button_CONFIGURE_Clicked(FVButtonClickEvent evt) {
		FocListFilterBindedToList filter = (FocListFilterBindedToList) getFocObject();

		FVTableWrapperLayout tableWrapper = getTableWrapperLayout(); 

		XMLViewKey key = new XMLViewKey(filter.getThisFocDesc().getStorageName(), XMLViewKey.TYPE_FORM);
		LoginTokenFilter_Form form = (LoginTokenFilter_Form) XMLViewDictionary.getInstance().newCentralPanel(getMainWindow(), key, filter);
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
					FocXMLLayout layout = getParentLayout();
					if(layout != null) {
						FVTableWrapperLayout tableWrapper = (FVTableWrapperLayout) layout.getComponentByName("GuestToken");
						if(tableWrapper != null) {
							tableWrapper.refresh();
						}
					}
					return false;
				}
			});
		}
	}

	@Override
	public boolean validationCheckData(FVValidationLayout validationLayout) {
		boolean validation = super.validationCheckData_NoApplyFilter(validationLayout);
		return validation;
	}

	
	public FocListFilterBindedToList getBindedListFilter() {
		FocListFilterBindedToList filter = (FocListFilterBindedToList) getFocObject();
		return filter;
	}
} 
