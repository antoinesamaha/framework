package com.foc.web.modules.notifier;

import com.foc.business.config.BusinessConfig;
import com.foc.shared.dataStore.IFocData;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLView;


@SuppressWarnings("serial")
public class FocNotificationEmail_EmailPdf_Form extends Email_Form {
	@Override
	public void init(INavigationWindow window, XMLView xmlView, IFocData focData) {
		super.init(window, xmlView, focData);
	}
	
	@Override
	protected void afterLayoutConstruction() {
		super.afterLayoutConstruction();
		getEmail().setTemplate(BusinessConfig.getInstance().getGeneralEmailTemplate());
	}
}
