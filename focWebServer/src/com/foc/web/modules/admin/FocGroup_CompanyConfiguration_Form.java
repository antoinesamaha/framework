package com.foc.web.modules.admin;

import com.foc.admin.FocGroup;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLView;

@SuppressWarnings("serial")
public class FocGroup_CompanyConfiguration_Form extends FocXMLLayout {
	
	public FocGroup getFocGroup(){
		return (FocGroup) getFocObject();
	}
	
	@Override
	public void init(INavigationWindow window, XMLView xmlView, IFocData focData) {
		super.init(window, xmlView, focData);
	}
}
