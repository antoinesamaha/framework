package com.foc.web.modules.chat.join.gui;

import com.foc.Globals;
import com.foc.list.filter.FocListFilterBindedToList;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.components.FVButtonClickEvent;
import com.foc.vaadin.gui.layouts.FVTableWrapperLayout;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.modules.chat.join.FChatJoinFilter;
import com.foc.web.server.xmlViewDictionary.XMLView;

@SuppressWarnings("serial")
public class FChatJoinFilter_Main_Standard_Form extends FocXMLLayout {

	FVTableWrapperLayout tableWrapperLayout = null;

	@Override
	public void init(INavigationWindow window, XMLView xmlView, IFocData focData) {
		super.init(window, xmlView, focData);
	}

	@Override
	public void dispose() {
		super.dispose();
		tableWrapperLayout = null;
	}

	public void initializeDefaultFilter() {
		FChatJoinFilter filter = (FChatJoinFilter) getFocObject();
		if (!filter.isActive()) {			
			filter.filterReceiver(Globals.getApp().getUser_ForThisSession());
			filter.filterUnread();
			filter.setActive(true);
		}
	}

	public void setTableWrapperLayout(FVTableWrapperLayout tableWrapperLayout) {
		this.tableWrapperLayout = tableWrapperLayout;
	}

	public FVTableWrapperLayout getTableWrapperLayout() {
		FVTableWrapperLayout localTableWrapper = tableWrapperLayout;
		if (localTableWrapper == null) {
			FocXMLLayout lay = this;
			while (lay.getParentLayout() != null) {
				lay = lay.getParentLayout();
			}
			if (lay != null) {
				localTableWrapper = (FVTableWrapperLayout) lay.getComponentByName("_InProcedure");
			}
		}
		return localTableWrapper;
	}
	
	@Override
	public boolean validationCheckData(FVValidationLayout validationLayout) {
		boolean validation = super.validationCheckData(validationLayout);
		applyFilter();
		return validation;
	}
	
	public boolean validationCheckData_NoApplyFilter(FVValidationLayout validationLayout) {
		boolean validation = super.validationCheckData(validationLayout);
//		applyFilter();
		return validation;
	}

	public void button_APPLY_FILTER_Clicked(FVButtonClickEvent evt) {
		applyFilter();
	}

	public void applyFilter() {
		FocListFilterBindedToList filter = (FocListFilterBindedToList) getFocObject();
		filter.setActive(true);

		FVTableWrapperLayout tableWrapper = getTableWrapperLayout();
		if (tableWrapper != null) {
			if (tableWrapper.getFocDataWrapper() != null) {
				tableWrapper.getFocDataWrapper().resetVisibleListElements();
			}
			if (tableWrapper.getTableTreeDelegate() != null) {
				tableWrapper.getTableTreeDelegate().refresh_CallContainerItemSetChangeEvent();
			}
		}
	}
}
