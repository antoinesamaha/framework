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
	public boolean validationCommit(FVValidationLayout validationLayout) {
		boolean error = super.validationCommit(validationLayout);
		
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
