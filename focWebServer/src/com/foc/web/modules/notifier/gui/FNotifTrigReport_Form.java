package com.foc.web.modules.notifier.gui;

import com.foc.business.notifier.FNotifTrigReport;
import com.foc.business.printing.PrnContext;
import com.foc.business.printing.PrnLayout;
import com.foc.business.printing.ReportFactory;
import com.foc.desc.FocDesc;
import com.foc.list.FocList;
import com.foc.property.FProperty;
import com.foc.property.FPropertyListener;
import com.foc.shared.dataStore.IFocData;
import com.foc.util.Utils;
import com.foc.vaadin.gui.components.FVMultipleChoiceStringField;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.vaadin.gui.xmlForm.IValidationListener;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLView;

public class FNotifTrigReport_Form extends FocXMLLayout {

	private FPropertyListener listener = null; 
	
	@Override
	public void init(INavigationWindow window, XMLView xmlView, IFocData focData) {
		super.init(window, xmlView, focData);
		
		FNotifTrigReport trigger = getNotifTrigReport();
		if(trigger != null) {
			trigger.copyReportConfig_Ref2Object();
			
			listener = new FPropertyListener() {
				@Override
				public void propertyModified(FProperty property) {
					if(property != null) {
						FVMultipleChoiceStringField multiChoice = (FVMultipleChoiceStringField) getComponentByName(FNotifTrigReport.FIELD_ReportLayout);
						
						if(multiChoice != null) {
							multiChoice.removeAllItems();
							
							FNotifTrigReport trigger = (FNotifTrigReport) getNotifTrigReport();
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
			FProperty prop = trigger.getFocPropertyByName(FNotifTrigReport.FIELD_ReportConfiguration);
			prop.addListener(listener);
		}
	}
	
	public void dispose() {
		if(listener != null) {
			FNotifTrigReport trigger = getNotifTrigReport();
			FProperty prop = trigger != null ? trigger.getFocPropertyByName(FNotifTrigReport.FIELD_ReportConfiguration) : null;
			if(prop != null) prop.removeListener(listener);
			listener.dispose();
			listener = null;
		}
		
		super.dispose();
	}
	
	@Override
	public void showValidationLayout(boolean showBackButton, int position) {
		super.showValidationLayout(showBackButton, position);
		FVValidationLayout vLay = getValidationLayout();
		vLay.addValidationListener(new IValidationListener() {
			
			@Override
			public void validationDiscard(FVValidationLayout validationLayout) {
			}
			
			@Override
			public boolean validationCommit(FVValidationLayout validationLayout) {
				return false;
			}
			
			@Override
			public boolean validationCheckData(FVValidationLayout validationLayout) {
				FNotifTrigReport trigger = getNotifTrigReport();
				if(trigger != null) {
					trigger.copyReportConfig_Object2Ref();
				}
				return false;
			}
			
			@Override
			public void validationAfter(FVValidationLayout validationLayout, boolean commited) {
			}
		});
	}
	
	public FNotifTrigReport getNotifTrigReport(){
		return (FNotifTrigReport) getFocObject();
	}
	
//	public void button_TEST_Clicked(FVButtonClickEvent evt) {
//		copyGuiToMemory();
//		FNotifTrigger trigger = getNotifTrigReport();
//		if(trigger != null) {
//			trigger.copyReportConfig_Object2Ref();
//			trigger.execute(null);
//		}
//	}
}
