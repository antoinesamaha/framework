package com.foc.menuStructure;

import com.foc.desc.FocObject;
import com.foc.desc.field.FField;
import com.foc.list.FocLinkSimple;
import com.foc.list.FocList;
import com.foc.list.FocListOrder;

@SuppressWarnings("serial")
public class FocMenuItemList extends FocList {
	
	public FocMenuItemList(){
		super(new FocLinkSimple(FocMenuItemDesc.getInstance()));
		setDbResident(false);
		FocListOrder order = new FocListOrder(FField.REF_FIELD_ID);
		//order.setReverted(true);
		setListOrder(order);
		sort();
	}
	
	public FocMenuItem findMenuItem(FocMenuItem father, String code){
		FocMenuItem found = null;
		for(int i=0; i<size() && found == null; i++){
			FocMenuItem menuItem = (FocMenuItem) getFocObject(i);
			if(FocObject.equal(menuItem.getFatherObject(), father)){
				if(menuItem.getCode().equals(code)){
					found = menuItem;
				}
			}
		}
		return found;
	}
	
	public FocMenuItem pushMenu(FocMenuItem father, String code, String title){
		FocMenuItem     menuItem = (FocMenuItem) findMenuItem(father, code);
		
		if(menuItem == null){
			menuItem = (FocMenuItem) newEmptyItem();
			menuItem.setCode(code);
			menuItem.setTitle(title);
			menuItem.setFatherObject(father);
			
			add(menuItem);
		}
		
		return menuItem;
	}
}
