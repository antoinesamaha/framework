package com.foc.web.modules.restmodule.gui;

import com.foc.vaadin.gui.xmlForm.FocXMLLayout_Filter;
import com.foc.web.modules.restmodule.LoginTokenFilter;

@SuppressWarnings("serial")
public class LoginTokenFilter_Form extends FocXMLLayout_Filter<LoginTokenFilter> {

	@Override
	protected String getFilteredTableGuiName() {
		return "GuestToken";
	}

	public void initializeDefaultFilter() {
		LoginTokenFilter filter = (LoginTokenFilter) getFocObject();
		if (!filter.isActive()) filter.setActive(true);
	}

}
