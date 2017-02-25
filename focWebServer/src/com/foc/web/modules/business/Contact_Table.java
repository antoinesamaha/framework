package com.foc.web.modules.business;

import com.foc.business.adrBook.ContactDesc;
import com.foc.list.FocList;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLView;

@SuppressWarnings("serial")
public class Contact_Table extends FocXMLLayout{

	@Override
	public void init(INavigationWindow window, XMLView xmlView, IFocData focData) {
		if(focData == null){
			focData = ContactDesc.getList(FocList.LOAD_IF_NEEDED);
		}
		super.init(window, xmlView, focData);
	}
}
