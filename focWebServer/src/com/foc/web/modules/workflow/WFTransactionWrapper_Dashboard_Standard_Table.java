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
package com.foc.web.modules.workflow;

import com.foc.Globals;
import com.foc.business.workflow.signing.WFTransactionWrapper;
import com.foc.business.workflow.signing.WFTransactionWrapperList;
import com.foc.dataDictionary.FocDataDictionary;
import com.foc.dataDictionary.IFocDataResolver;
import com.foc.dataWrapper.FocDataWrapper;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.shared.dataStore.IFocData;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.util.Utils;
import com.foc.vaadin.FocWebVaadinWindow;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.components.ITableTree;
import com.foc.vaadin.gui.layouts.FVTableWrapperLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLView;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;

@SuppressWarnings("serial")
public class WFTransactionWrapper_Dashboard_Standard_Table extends WFTransactionWrapper_Table {
	
	private	WFTransactionWrapperList wrapperList = null;
	private boolean filterAdded = false;
	
	@Override
	public void init(INavigationWindow window, XMLView xmlView, IFocData focData) {
		if(focData == null) {
			wrapperList = new WFTransactionWrapperList();
			focData = wrapperList;
		}
		super.init(window, xmlView, focData);
	}
	
	public void dispose(){
		super.dispose();
		
		if(wrapperList != null) {
			wrapperList.dispose();
			wrapperList = null;
		}
	}
	
	@Override
	protected void afterLayoutConstruction() {
		super.afterLayoutConstruction();
		//The reload here is not needed since we have a reload in the refresh
//		reloadTransactionWrapperList();
	}
	
	protected void addFilter() {
		
	}
	
	protected boolean isFocDescIncuded(FocDesc focDescToFind) {
		boolean found = false;
		FocDataDictionary dictionary = getFocDataDictionary(false);
		if(dictionary != null && focDescToFind != null) {
			int index = 1;
			IFocDataResolver resolver = dictionary.getParameter("DESC"+index);
			while (resolver != null && !found) {
				String tableName = (String) resolver.getValue(null, null);
				found = focDescToFind.getStorageName().equals(tableName);
				index++;
				resolver = dictionary.getParameter("DESC"+index);
			}
		}
		return found;
	}
	
	protected void reloadTransactionWrapperList() {
		if(wrapperList != null) {
			FVTableWrapperLayout wrapperLayout = getTableWrapperLayout();
			
			boolean backupRefreshValue = true;
			FocDataWrapper focDataWrpper = wrapperLayout != null ? wrapperLayout.getFocDataWrapper() : null;
			if(focDataWrpper != null) backupRefreshValue = focDataWrpper.setRefreshGuiDisabled(true);
			
			wrapperList.removeAll();
			
			if(!isFilterAdded()) {
				addFilter();
				setFilterAdded(true);
			}
			
			FocDataDictionary dictionary = getFocDataDictionary(false);
			if(dictionary != null) {
				int index = 1;
				
				IFocDataResolver resolver = dictionary.getParameter("DESC"+index);
				while (resolver != null) {
					String tableName = (String) resolver.getValue(null, null);
					if(!Utils.isStringEmpty(tableName)) {
						FocDesc focDesc = Globals.getApp().getFocDescByName(tableName);
						if(focDesc != null) {
							wrapperList.fillWrapperListForFocDesc(focDesc);
						}
					}
					index++;
					resolver = dictionary.getParameter("DESC"+index);
				}
			}
			
			wrapperList.setDefaultListOrder();
			if(focDataWrpper != null) focDataWrpper.setRefreshGuiDisabled(backupRefreshValue);
		}
	}
	
	@Override
	public void refresh() {
		reloadTransactionWrapperList();
		INavigationWindow window = getMainWindow();
		if(window != null && window instanceof FocWebVaadinWindow) {
			((FocWebVaadinWindow)window).refreshAllSignatureCounts();
		}	
		super.refresh();
	}
	
	@Override
	public ICentralPanel table_OpenItem(String tableName, ITableTree table, FocObject focObject, int viewContainer_Open) {
		WFTransactionWrapper wfTransactionWrapper = (WFTransactionWrapper) focObject;
		FocObject originalTransaction = wfTransactionWrapper != null ? wfTransactionWrapper.getFocObject() : null;

		ICentralPanel centralPanel = null;
		
		if(table != null && table.getTableTreeDelegate() != null) {
			if(originalTransaction != null) {
				XMLViewKey key = new XMLViewKey(originalTransaction.getThisFocDesc().getStorageName(), XMLViewKey.TYPE_FORM);
				centralPanel = XMLViewDictionary.getInstance().newCentralPanel(getMainWindow(), key, originalTransaction);
			}
			
			if(centralPanel != null) {
				table.getTableTreeDelegate().openFormPanel(centralPanel, viewContainer_Open);
			}
		}
		
		/*
		WFTransactionWrapperList transList = getWFTransactionWrapperList();
		for(int i=0; i<transList.size(); i++){
			WFTransactionWrapper wfTransactionWrapper = (WFTransactionWrapper) transList.getFocObject(i);
			if(wfTransactionWrapper != null){
				if(wfTransactionWrapper.equalsRef(focObject)){
					rowOpened = i;
					break;
				}
			}
		}
		
		ICentralPanel centralPanel = super.table_OpenItem(tableName, table, focObject, viewContainer_Open);
		if(centralPanel != null && centralPanel instanceof WFTransactionWrapper_Form){
			WFTransactionWrapper_Form currentWrapper_Form = (WFTransactionWrapper_Form) centralPanel;
			currentWrapper_Form.setWFTransactionWrapper_Table(WFTransactionWrapper_Table.this);
		}
		*/
		
		return centralPanel;
	}

	public boolean isFilterAdded() {
		return filterAdded;
	}

	public void setFilterAdded(boolean filterAdded) {
		this.filterAdded = filterAdded;
	}

}
