package com.foc.web.modules.workflow;

import com.foc.Globals;
import com.foc.business.workflow.signing.WFTransactionWrapperList;
import com.foc.dataDictionary.FocDataDictionary;
import com.foc.dataDictionary.IFocDataResolver;
import com.foc.desc.FocDesc;
import com.foc.shared.dataStore.IFocData;
import com.foc.util.Utils;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLView;

@SuppressWarnings("serial")
public class WFTransactionWrapper_Dashboard_Standard_Table extends WFTransactionWrapper_Table {
	
	private	WFTransactionWrapperList wrapperList = null;
	
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
		reloadTransactionWrapperList();
	}
	
	private void reloadTransactionWrapperList() {
		if(wrapperList != null) {
			wrapperList.removeAll();
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
		}
	}
	
	@Override
	public void refresh() {
		reloadTransactionWrapperList();
		super.refresh();
	}
}
