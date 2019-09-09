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

import com.foc.Globals;
import com.foc.IFocEnvironment;
import com.foc.business.calendar.FCalendar;
import com.foc.business.currency.DateLine;
import com.foc.business.currency.DateLineList;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;

@SuppressWarnings("serial")
public class CurrencyDateLine_Form extends FocXMLLayout {

	@Override
	protected void afterLayoutConstruction() {
		super.afterLayoutConstruction();
	}

	public DateLineList getDateLineList(){
		return (DateLineList) getFocData();	
	}

	@Override
	public boolean validationCheckData(FVValidationLayout validationLayout) {
		DateLine     dateLine     = (DateLine) getFocData();
		DateLineList dateLineList = (DateLineList) dateLine.getFatherSubject();
		
//		DateLine existingDateLine = null;
//		for(int i=0; i<dateLineList.size(); i++){
//			DateLine currDateLine = (DateLine) dateLineList.getFocObject(i);
//			if(currDateLine != null && !currDateLine.equalsRef(dateLine) && FCalendar.compareDatesRegardlessOfTime(currDateLine.getDate(), dateLine.getDate()) == 0){
//				existingDateLine = currDateLine;
//			}
//		}
		copyGuiToMemory();
		DateLine existingDateLine = dateLineList.findByDate_Exactly(dateLine.getDate());
		boolean error = existingDateLine != null;
		if(error){
			Globals.showNotification("Date already exists", "", IFocEnvironment.TYPE_WARNING_MESSAGE);
		}else{
			error = super.validationCheckData(validationLayout);
			dateLineList.sort();
		}
		
    return error;
	}
}
