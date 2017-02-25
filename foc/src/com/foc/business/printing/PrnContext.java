package com.foc.business.printing;

import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.list.FocListOrder;

public class PrnContext extends FocObject {
	
	public PrnContext(FocConstructor constr){
		super(constr);
		newFocProperties();
	}
	
	public String getDBName(){
		return getPropertyString(PrnContextDesc.FLD_DB_NAME);
	}

	public void setDBName(String name){
		setPropertyString(PrnContextDesc.FLD_DB_NAME, name);
	}

	public FocList getLayoutList(){
		FocList list = getPropertyList(PrnContextDesc.FLD_LAYOUT_LIST);
		if(list != null && list.getListOrder() == null){
			list.setListOrder(new FocListOrder(PrnLayoutDesc.FLD_FILE_NAME));
			list.setDirectImpactOnDatabase(false);
			list.setDirectlyEditable(true);
		}
		return list;
	}

	public PrnLayout getLayoutByName(String name){
		PrnLayout layout     = null;
		FocList   layoutList = getLayoutList();
		if(layoutList != null){
			layoutList.reloadFromDB();
			layout = (PrnLayout) layoutList.searchByPropertyStringValue(PrnLayoutDesc.FLD_NAME, name);
		}
		return layout;
	}

	public PrnLayout pushLayout(String fileName, String name, String description){
		PrnLayout layout     = null;
		FocList   layoutList = getLayoutList();
		if(layoutList != null){
			layoutList.reloadFromDB();
			layout = (PrnLayout) layoutList.searchByPropertyStringValue(PrnLayoutDesc.FLD_FILE_NAME, fileName);
			if(layout == null){
				layout = (PrnLayout) layoutList.newEmptyItem();
				layout.setFileName(fileName);
			}
			if(layout != null){
				layout.setName(name);
				layout.setDescription(description);
				layout.validate(true);
				layout.setCreated(false);
			}
		}
		return layout;
	}
}
