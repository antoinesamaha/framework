package com.fab.model.table.underlyingCustomisation;

import com.fab.model.table.FieldDefinitionGuiBrowsePanel;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class UndCustFieldGuiBrowsePanel extends FieldDefinitionGuiBrowsePanel {
	
	public UndCustFieldGuiBrowsePanel(FocList list, int viewID){
		super(list, viewID);
	}
	
	public UndCustFieldGuiBrowsePanel(FocList list, int viewID, boolean withDictionaryGroups){
		super(list, viewID, withDictionaryGroups);
	}
	
	@Override
	protected void addAdditionalColumns(boolean allowEdit){
		UndCustFieldDesc focDesc = (UndCustFieldDesc) UndCustFieldDesc.getInstance();
		getTableView().addColumn(focDesc, UndCustFieldDesc.FLD_NOT_PHYSICAL_DIFFERENCE, allowEdit);
	}
}
