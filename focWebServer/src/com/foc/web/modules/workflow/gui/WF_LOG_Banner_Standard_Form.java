package com.foc.web.modules.workflow.gui;

import com.foc.business.workflow.implementation.WFLog;
import com.foc.dataDictionary.FocDataDictionary;
import com.foc.dataDictionary.FocDataResolver_StringConstant;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.components.FVLabel;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLView;
import com.vaadin.ui.Component;

@SuppressWarnings("serial")
public class WF_LOG_Banner_Standard_Form extends FocXMLLayout {
	@Override
	public void init(INavigationWindow window, XMLView xmlView, IFocData focData) {
		super.init(window, xmlView, focData);

		WFLog log = (WFLog) focData;
		
		FocDataDictionary dictionary = getFocDataDictionary(true);
		dictionary.putParameter("EVENT_TYPE_TITLE", new FocDataResolver_StringConstant(log.getEventTypeTitle()));
	}
	
	public WFLog getLog() {
		return ((WFLog) getFocData());
	}
	
	@Override
	protected void afterLayoutConstruction() {
		super.afterLayoutConstruction();
		FVLabel label = (FVLabel) getComponentByName("SENDER");
		if(label != null) {
			String senderTxt = ""; 
			WFLog log = getLog();
			if(log != null) {
				senderTxt  = log.getUser() != null ? log.getUser().getFullName() : "";
				senderTxt += " - " + log.getDateTime() + " - " + log.getEventTypeTitle(); 
				if(log.getEventUndone()) {
					senderTxt = "<s>" + senderTxt + "</s>";
				}
			}
			label.setValue(senderTxt);
		}
		
//		Component comp = getComponentByName("MESSAGE");
//		if(comp != null) {
//			comp.addStyleName("fenix-them-bubble");
//		}
	}
}
