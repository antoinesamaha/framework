package com.foc.web.modules.admin;

import com.foc.admin.FocUserDesc;
import com.foc.dataWrapper.FocListWrapper;
import com.foc.desc.FocObject;
import com.foc.desc.field.FField;
import com.foc.list.FocList;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.components.ITableTree;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLView;

@SuppressWarnings("serial")
public class FocUser_Saas_Table extends FocXMLLayout{
	
	@Override
	public void init(INavigationWindow window, XMLView xmlView, IFocData focData) {
		FocList userList = null;
		//We start by checking if the sent focData is the FocUserList 
		if(focData instanceof FocList){
			FocList list = (FocList) focData;
			if(list.getFocDesc().getStorageName().equals(FocUserDesc.getInstance().getStorageName())){
				userList = list;
			}
		}
		//If not we need to set the focData ourselves
		if(userList == null){
			userList = FocUserDesc.getList();
			userList.loadIfNotLoadedFromDB();
		}

		//Build a Wrapper that would show only the Role based users because other users like 01Barmaja should be kept hidden 
		FocListWrapper userWrapper = new FocListWrapper(userList);
		FField field = FocUserDesc.getInstance().getFieldByID(FocUserDesc.FLD_SAAS_APPLICATION_ROLE);
		if(field != null){
			userWrapper.addFilterByExpression("NOT("+field.getName()+"="+FocUserDesc.APPLICATION_ROLE_NONE+")");
			focData = userWrapper;
		}
		super.init(window, xmlView, focData);
	}
	
	@Override
	public FocObject table_AddItem(String tableName, ITableTree table, FocObject fatherObject) {
		return super.table_AddItem(tableName, table, fatherObject);
	}
}
