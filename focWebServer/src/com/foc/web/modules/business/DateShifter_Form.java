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
package com.foc.web.modules.business;

import com.foc.business.dateShifter.DateShifter;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout_Filter;
import com.foc.vaadin.gui.xmlForm.IValidationListener;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLView;

public class DateShifter_Form extends FocXMLLayout {
	
	private FocXMLLayout_Filter filterLayout = null;

	@Override
	public void init(INavigationWindow window, XMLView xmlView, IFocData focData) {
		super.init(window, xmlView, focData);
		if(getDateShifter() != null) getDateShifter().adjustDate();
	}
	
	public DateShifter getDateShifter(){
		return (DateShifter) getFocData();
	}
	
	public FocXMLLayout_Filter getFilterLayout() {
		return filterLayout;
	}

	public void setFilterLayout(FocXMLLayout_Filter filterLayout) {
		this.filterLayout = filterLayout;
	}

	@Override
	public void showValidationLayout(boolean showBackButton, int position) {
		// TODO Auto-generated method stub
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
				return false;
			}
			
			@Override
			public void validationAfter(FVValidationLayout validationLayout, boolean commited) {
				if(getFilterLayout() != null) {
				  if(getFilterLayout().getFilter() != null) {
				  	getFilterLayout().getFilter().adjustDatesAccordingToShifters();
				  }
				  getFilterLayout().copyMemoryToGui();
				}
//				if(getDateShifter() != null) getDateShifter().adjustDate();
			}
		});
	}

}
