package com.foc.web.modules.notifier.gui;

import com.foc.business.notifier.FNotifTrigger;
import com.foc.business.printing.PrnContext;
import com.foc.business.printing.PrnLayout;
import com.foc.business.printing.ReportFactory;
import com.foc.desc.FocDesc;
import com.foc.list.FocList;
import com.foc.property.FProperty;
import com.foc.property.FPropertyListener;
import com.foc.shared.dataStore.IFocData;
import com.foc.util.Utils;
import com.foc.vaadin.fields.FVMultipleChoiceString;
import com.foc.vaadin.gui.components.FVButtonClickEvent;
import com.foc.vaadin.gui.components.FVMultipleChoiceStringField;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLView;

public class FNotifTrigger_Form extends FocXMLLayout {

	private FPropertyListener listener = null; 
	
	@Override
	public void init(INavigationWindow window, XMLView xmlView, IFocData focData) {
		super.init(window, xmlView, focData);
		
		FNotifTrigger trigger = getNotifTrigger();
		listener = new FPropertyListener() {
			@Override
			public void propertyModified(FProperty property) {
				if(property != null) {
					FVMultipleChoiceStringField multiChoice = (FVMultipleChoiceStringField) getComponentByName(FNotifTrigger.FIELD_ReportLayout);
					
					if(multiChoice != null) {
						multiChoice.removeAllItems();
						
						FNotifTrigger trigger = (FNotifTrigger) getNotifTrigger();
						FocDesc focDesc = trigger != null ? trigger.getReportConfigFocDesc() : null;
						String contextName = focDesc != null ? focDesc.getReportContext() : null;
						if(!Utils.isStringEmpty(contextName)) {
							PrnContext context = ReportFactory.getInstance().findContext(contextName);
							FocList list = context != null ? context.getLayoutList() : null;
							if(list != null) {
								
								for(int i=0; i<list.size(); i++) {
									PrnLayout layout = (PrnLayout) list.getFocObject(i);
									if(layout != null && !Utils.isStringEmpty(layout.getName())){
										multiChoice.addItem(layout.getName());
									}
								}
							}
						}
					}
				}
			}
		
			@Override
			public void dispose() {
			}
		};
		FProperty prop = trigger.getFocPropertyByName(FNotifTrigger.FIELD_ReportConfiguration);
		prop.addListener(listener);
	}
	
	public void dispose() {
		if(listener != null) {
			FNotifTrigger trigger = getNotifTrigger();
			FProperty prop = trigger != null ? trigger.getFocPropertyByName(FNotifTrigger.FIELD_ReportConfiguration) : null;
			if(prop != null) prop.removeListener(listener);
			listener.dispose();
			listener = null;
		}
		
		super.dispose();
	}
	
	public FNotifTrigger getNotifTrigger(){
		return (FNotifTrigger) getFocObject();
	}
	
	public void button_TEST_Clicked(FVButtonClickEvent evt) {
		getNotifTrigger().execute(null);
	}
}
