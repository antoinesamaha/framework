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
package com.foc.vaadin.gui.xmlForm;

import com.foc.business.dateShifter.DateShifter;
import com.foc.list.filter.DateCondition;
import com.foc.list.filter.DateTimeCondition;
import com.foc.list.filter.FilterCondition;
import com.foc.list.filter.FilterDesc;
import com.foc.list.filter.FocListFilter;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.vaadin.gui.components.FVButtonClickEvent;
import com.foc.vaadin.gui.layouts.FVTableWrapperLayout;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.web.modules.business.DateShifter_Form;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;

public abstract class FocXMLLayout_Filter<F extends FocListFilter> extends FocXMLLayout {

	private FVTableWrapperLayout tableWrapperLayout   = null;
	
	protected String getFilteredTableGuiName() {
		return "_TABLE";
	}
	
	@Override
	protected void afterLayoutConstruction() {
		super.afterLayoutConstruction();
		adjustPopupButtonsListeners();
	}
	
	protected void adjustPopupButtonsListeners() {
  	if(getFilter() != null && getFilter().isDbResident() && getFilter().getThisFocDesc().isDbResident()) {
			//Hiding the Dates shifters when the filter is not db residen 
	    FilterDesc filterDesc = getFilter().getThisFilterDesc();
	    if(filterDesc != null){
		  	for(int i=0; i<filterDesc.getConditionCount(); i++){
		      FilterCondition cond = filterDesc.getConditionAt(i);
		      if(			cond != null 
		      		&& 	(   cond instanceof DateCondition
		      		     || cond instanceof DateTimeCondition)){
		      	
		      	DateShifter firstDateShifter = null; 
		      	DateShifter lastDateShifter = null;
		      	
		      	if(cond instanceof DateCondition) {
			      	DateCondition dateCondition = (DateCondition) cond;
			      	firstDateShifter = getFilter().getDateShifter(dateCondition.getFirstDateShifterDesc().getFieldsShift());
			      	lastDateShifter = getFilter().getDateShifter(dateCondition.getLastDateShifterDesc().getFieldsShift());
		      	} else {
			      	DateTimeCondition dateCondition = (DateTimeCondition) cond;
			      	firstDateShifter = getFilter().getDateShifter(dateCondition.getFirstDateShifterDesc().getFieldsShift());
			      	lastDateShifter = getFilter().getDateShifter(dateCondition.getLastDateShifterDesc().getFieldsShift());
		      	}
		      	
		      	String buttonName = FocXMLFilterConditionBuilder.getButtonName_ForFirstDateShifterButton(cond);
		      	Component comp = getComponentByName(buttonName);
		      	if(comp != null && comp instanceof FVButton) {
		      		FVButton button = (FVButton) comp;
		        	if(button != null) {
		        		button.addClickListener(new DateButtonCickListener(firstDateShifter));
		        	}
		      	}
		      	
		      	buttonName = FocXMLFilterConditionBuilder.getButtonName_ForLastDateShifterButton(cond);
		      	comp = getComponentByName(buttonName);
		      	if(comp != null && comp instanceof FVButton) {
		      		FVButton button = (FVButton) comp;
		        	if(button != null) {
		        		button.addClickListener(new DateButtonCickListener(lastDateShifter));
		        	}
		      	}
		      }
		    }
	    }
  	}
	}

	@Override
	public void dispose() {
		super.dispose();
		tableWrapperLayout = null;
	}

	public F getFilter() {
		return (F) getFocObject();
	}
	
	public void initializeDefaultFilter() {
		F filter = getFilter();
		if (!filter.isActive()) {
			initializeDefaultFilter(filter);
			filter.setActive(true);
		}
	}
	
	public void initializeDefaultFilter(F filter) {
	}

	public void setTableWrapperLayout(FVTableWrapperLayout tableWrapperLayout) {
		this.tableWrapperLayout = tableWrapperLayout;
	}

	public FVTableWrapperLayout getTableWrapperLayout() {
		FVTableWrapperLayout localTableWrapper = tableWrapperLayout;
		if (localTableWrapper == null) {
			FocXMLLayout lay = this;
			while (lay.getParentLayout() != null) {
				lay = lay.getParentLayout();
			}
			if (lay != null) {
				localTableWrapper = (FVTableWrapperLayout) lay.getComponentByName(getFilteredTableGuiName());
			}
		}
		return localTableWrapper;
	}

	@Override
	public boolean validationCheckData(FVValidationLayout validationLayout) {
		boolean validation = super.validationCheckData(validationLayout);
		applyFilter();
		return validation;
	}

	public boolean validationCheckData_NoApplyFilter(FVValidationLayout validationLayout) {
		boolean validation = super.validationCheckData(validationLayout);
		return validation;
	}

	public void button_APPLY_FILTER_Clicked(FVButtonClickEvent evt) {
		applyFilter();
	}

	public void applyFilter() {
		FocListFilter filter = getFilter();
		filter.setActive(true);

		FVTableWrapperLayout tableWrapper = getTableWrapperLayout();
		if (tableWrapper != null) {
			if (tableWrapper.getFocDataWrapper() != null) {
				tableWrapper.getFocDataWrapper().resetVisibleListElements();
			}
			if (tableWrapper.getTableTreeDelegate() != null) {
				tableWrapper.getTableTreeDelegate().refresh_CallContainerItemSetChangeEvent();
			}
		}
	}
	
	public class DateButtonCickListener implements Button.ClickListener {
		private DateShifter dateShifter = null;
		
		public DateButtonCickListener(DateShifter dateShifter) {
			this.dateShifter = dateShifter;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			XMLViewKey xmlViewKey = new XMLViewKey("DateShifter", XMLViewKey.TYPE_FORM, XMLViewKey.CONTEXT_DEFAULT, XMLViewKey.VIEW_DEFAULT);
			DateShifter_Form dateShifterForm = (DateShifter_Form) XMLViewDictionary.getInstance().newCentralPanel(getMainWindow(), xmlViewKey, dateShifter);
			dateShifterForm.popupInDialog();
		}
	}
	
}
