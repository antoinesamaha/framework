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

import com.foc.admin.FocUser;
import com.foc.admin.UserSession;
import com.foc.business.currency.DateLineDesc;
import com.foc.business.currency.DateLineList;
import com.foc.desc.FocObject;
import com.foc.shared.dataStore.IFocData;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.components.ITableTree;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLView;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;

@SuppressWarnings("serial")
public class CurrencyDateLine_Table extends FocXMLLayout {

	@Override
	public void init(INavigationWindow window, XMLView xmlView, IFocData focData) {
		focData = new DateLineList(true);
		super.init(window, xmlView, focData);
		FocUser user = UserSession.getInstanceForThread().getUser();
		boolean allowEditing = user != null && user.getGroup() != null && user.getGroup().allowCurrencyRateModif();
		if(!allowEditing){
			setEditable(false);
		}
	}

	public void dispose(){
		super.dispose();
		DateLineList list = getDateLineList();
		if(list != null){
			list.dispose();
			list = null;
		}
	}
	
	@Override
	protected void afterLayoutConstruction() {
		super.afterLayoutConstruction();
//		FVTableWrapperLayout tableWrapper = (FVTableWrapperLayout) getComponentByName("CURRENCY_DATE_LINE_TABLE");
//		TableTreeDelegate delegate = tableWrapper.getTableTreeDelegate();
//		FVTableColumn tableCol = delegate.findColumn("RATE0");
//		tableCol.setCaption("R 0");
	}

	public DateLineList getDateLineList(){
		return (DateLineList) getFocData();	
	}

	@Override
	public boolean validationCheckData(FVValidationLayout validationLayout) {
		boolean error = super.validationCheckData(validationLayout);
		
		DateLineList dateLineList = getDateLineList();
		dateLineList.copyToListInCache();
		DateLineList.getInstance(true).saveRatesToDB();
		
    return false;
	}

	@Override
	public ICentralPanel table_NewCentralPanel_ForForm(String tableName, ITableTree table, FocObject focObject) {
		XMLViewKey xmlViewKey = new XMLViewKey(DateLineDesc.TABLE_NAME, XMLViewKey.TYPE_FORM);
		return XMLViewDictionary.getInstance().newCentralPanel(getMainWindow(), xmlViewKey, focObject);
	}
	
//	@Override
//	public FocObject table_AddItem(String tableName, ITableTree table, FocObject fatherObject) {
//		DateLine dateLine = (DateLine) super.table_AddItem(tableName, table, fatherObject);
//		
//		Globals.popup(dateLine, true);
//		
//		return dateLine;
//	}

}
