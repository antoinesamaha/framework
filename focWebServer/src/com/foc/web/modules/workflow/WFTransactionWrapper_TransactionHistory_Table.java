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
import com.foc.admin.FocUserHistory;
import com.foc.admin.FocUserHistoryDesc;
import com.foc.admin.FocUserHistoryList;
import com.foc.business.workflow.signing.WFTransactionWrapper;
import com.foc.business.workflow.signing.WFTransactionWrapperList;
import com.foc.desc.FocObject;
import com.foc.shared.dataStore.IFocData;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.components.ITableTree;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLView;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;

@SuppressWarnings("serial")
public class WFTransactionWrapper_TransactionHistory_Table extends FocXMLLayout{

	@Override
	public void init(INavigationWindow window, XMLView xmlView, IFocData focData) {
		WFTransactionWrapperList transactionWrapperList = null;
		
		FocUserHistoryList focUserHistoryList = (FocUserHistoryList) FocUserHistoryDesc.getInstance().getFocList();
		FocUserHistory focUserHistory = focUserHistoryList.findHistory(Globals.getApp().getUser_ForThisSession());
		if(focUserHistory != null){
			transactionWrapperList = focUserHistory.newRecentTransactionList();
		}else{
			transactionWrapperList = new WFTransactionWrapperList();
		}
		super.init(window, xmlView, transactionWrapperList);
	}
	
	@Override
	public ICentralPanel table_OpenItem(String tableName, ITableTree table, FocObject focObject, int viewContainer_Open) {
		ICentralPanel centralPanel = null;
		if(focObject != null && focObject instanceof WFTransactionWrapper){
			WFTransactionWrapper transactionWrapper = (WFTransactionWrapper) focObject;
			FocObject workflowObject = (FocObject) transactionWrapper.getWorkflow();
			if(workflowObject != null){
				XMLViewKey xmlViewKey = new XMLViewKey(workflowObject.getThisFocDesc().getStorageName(), XMLViewKey.TYPE_FORM, workflowObject.getThisFocDesc().focDesc_getGuiContext(), XMLViewKey.VIEW_DEFAULT);
				centralPanel = XMLViewDictionary.getInstance().newCentralPanel(getMainWindow(), xmlViewKey, workflowObject);
				getMainWindow().changeCentralPanelContent(centralPanel, true);
			}
		}
		return centralPanel;
	}
}
