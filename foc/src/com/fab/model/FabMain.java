package com.fab.model;

import com.fab.model.table.FieldDefinition;
import com.foc.Globals;
import com.foc.desc.FocDesc;
import com.foc.list.FocList;

public class FabMain {
	
	public static FocList getFocList(String tableName){
		FocList list = null; 
		FocDesc desc = Globals.getApp().getFocDescByName(tableName);
		if(desc != null){
			list = FieldDefinition.getFocList(0, desc);
		}
		return list;
	}
	
}
