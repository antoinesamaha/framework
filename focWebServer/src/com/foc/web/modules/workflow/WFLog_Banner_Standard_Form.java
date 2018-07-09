package com.foc.web.modules.workflow;

import com.foc.business.workflow.implementation.WFLog;
import com.foc.dataDictionary.FocDataDictionary;
import com.foc.dataDictionary.FocDataResolver_StringConstant;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLView;
import com.vaadin.ui.Component;

@SuppressWarnings("serial")
public class WFLog_Banner_Standard_Form extends FocXMLLayout {
	@Override
	public void init(INavigationWindow window, XMLView xmlView, IFocData focData) {
		super.init(window, xmlView, focData);

		WFLog log = (WFLog) focData;
		
		FocDataDictionary dictionary = getFocDataDictionary(true);
		dictionary.putParameter("EVENT_TYPE_TITLE", new FocDataResolver_StringConstant(log.getEventTypeTitle()));
	}
	
	@Override
	protected void afterLayoutConstruction() {
		super.afterLayoutConstruction();
//		Component comp = getComponentByName("MESSAGE");
//		if(comp != null) {
//			comp.addStyleName("fenix-them-bubble");
//		}
	}
}
