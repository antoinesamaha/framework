package com.foc.web.modules.business;

import com.foc.business.config.BusinessConfig;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLView;

public class BusinessConfig_Form extends FocXMLLayout {

	@Override
	public void init(INavigationWindow window, XMLView xmlView, IFocData focData) {
		if(focData == null){
			focData = BusinessConfig.getInstance();
		}
		super.init(window, xmlView, focData);
	}
}
