/*******************************************************************************
 * Copyright 2016 Antoine Nicolas SAMAHA
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.foc.web.modules.notifier.gui;

import com.foc.business.notifier.FNotifTrigReport;
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
import com.foc.vaadin.gui.components.FVButtonClickEvent;
import com.foc.vaadin.gui.components.FVMultipleChoiceStringField;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.vaadin.gui.xmlForm.IValidationListener;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLView;

public class FNotifTrigger_Form extends FocXMLLayout {

	private FPropertyListener listener = null; 
	
	@Override
	public void init(INavigationWindow window, XMLView xmlView, IFocData focData) {
		super.init(window, xmlView, focData);
		
		FNotifTrigger trigger = getNotifTrigger();
		if(trigger != null) {
			FocList list = trigger.getReportList();
			for(int i=0; i<list.size(); i++) {
				FNotifTrigReport rep = (FNotifTrigReport) list.getFocObject(i);
				if(rep != null) {
					rep.copyReportConfig_Ref2Object();
				}
			}
			
			trigger.copyReportConfig_Ref2Object();
			
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
				FNotifTrigger trigger = getNotifTrigger();
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
	
	public FNotifTrigger getNotifTrigger(){
		return (FNotifTrigger) getFocObject();
	}
	
	public void button_TEST_Clicked(FVButtonClickEvent evt) {
		copyGuiToMemory();
		FNotifTrigger trigger = getNotifTrigger();
		if(trigger != null) {
			trigger.copyReportConfig_Object2Ref();
			trigger.execute(null);
		}
	}
}
