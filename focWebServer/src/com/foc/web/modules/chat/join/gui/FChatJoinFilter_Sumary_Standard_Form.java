package com.foc.web.modules.chat.join.gui;

import com.foc.list.filter.FocListFilterBindedToList;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.gui.components.FVButtonClickEvent;
import com.foc.vaadin.gui.layouts.FVTableWrapperLayout;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.xmlForm.IValidationListener;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;

@SuppressWarnings("serial")
public class FChatJoinFilter_Sumary_Standard_Form extends FChatJoinFilter_Main_Standard_Form {

	public void button_CONFIGURE_Clicked(FVButtonClickEvent evt) {
		FocListFilterBindedToList filter = (FocListFilterBindedToList) getFocObject();

		FVTableWrapperLayout tableWrapper = getTableWrapperLayout(); 

		XMLViewKey key = new XMLViewKey(filter.getThisFocDesc().getStorageName(), XMLViewKey.TYPE_FORM);
		FChatJoinFilter_Main_Standard_Form form = (FChatJoinFilter_Main_Standard_Form) XMLViewDictionary.getInstance().newCentralPanel(getMainWindow(), key, filter);
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
					// TODO Auto-generated method stub
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
