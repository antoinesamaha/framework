package com.fab.model.table;

import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;
import com.foc.list.FocList;

public class FabMultiChoiceSet extends FocObject {
	
	public FabMultiChoiceSet(FocConstructor constr){
		super(constr);
		newFocProperties();
	}
	
	public FocList getMultipleChoiceList(){
		FocList list = getPropertyList(FabMultiChoiceSetDesc.FLD_CHOICE_LIST);
		list.setDirectlyEditable(true);
		list.setDirectImpactOnDatabase(false);
		return list;
	}
}
