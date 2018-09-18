package com.foc.web.modules.workflow;

import com.foc.business.workflow.map.WFSignature;
import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.vaadin.gui.components.ITableTree;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;

@SuppressWarnings("serial")
public class WFSignature_Table extends FocXMLLayout{
	
	@Override
	public FocObject table_AddItem(String tableName, ITableTree table, FocObject fatherObject) {
		WFSignature signature = (WFSignature) super.table_AddItem(tableName, table, fatherObject);
		if(signature != null){
			FocList list = getFocList();
			if(list != null && list.size() >= 2){
				WFSignature prev = (WFSignature) list.getFocObject(list.size()-2);
				if(prev != null){
					signature.setPreviousStage(prev.getTargetStage());
				}
			}
		}
		return signature; 
	}
}
