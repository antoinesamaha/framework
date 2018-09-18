package com.foc.web.modules.workflow.gui;

import com.foc.vaadin.gui.xmlForm.FocXMLLayout;

@SuppressWarnings("serial")
public class WF_ASSIGNEMENT_Table extends FocXMLLayout{

	/*
	@Override
	public void table_OpenItem(String tableName, ITableTree table, FocObject focObject, int viewContainer_Open) {
		if(focObject != null){
			WFTransactionConfig transactionConfig = (WFTransactionConfig) focObject;
			FocList list = transactionConfig.getFieldLockStageList();
			if(list != null){
				XMLViewKey xmlViewKey = new XMLViewKey(WFFieldLockStageDesc.getInstance().getStorageName(), XMLViewKey.TYPE_TABLE);
				ICentralPanel centralPanel = XMLViewDictionary.getInstance().newCentralPanel(getMainWindow(), xmlViewKey, list);
				getMainWindow().changeCentralPanelContent(centralPanel, true);
			}
		}else{
			super.table_OpenItem(tableName, table, focObject, viewContainer_Open);
		}
	}
	*/
}
