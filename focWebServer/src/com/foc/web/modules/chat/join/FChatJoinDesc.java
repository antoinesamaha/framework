package com.foc.web.modules.chat.join;

import com.foc.business.workflow.implementation.WorkflowDesc;
import com.foc.desc.field.FField;
import com.foc.desc.field.FObjectField;
import com.foc.desc.parsers.pojo.PojoFocDesc;
import com.foc.desc.parsers.pojo.PojoFocObject;
import com.foc.list.FocList;
import com.foc.list.FocListOrder;

public class FChatJoinDesc extends PojoFocDesc {

	public FChatJoinDesc(Class<PojoFocObject> focObjectClass, boolean dbResident, String storageName, boolean isKeyUnique) {
		super(focObjectClass, dbResident, storageName, isKeyUnique);
		workflowDesc = new WorkflowDesc(this);
	}

	public static FChatJoinDesc getInstance() {
		return (FChatJoinDesc) FChatJoin.getFocDesc();
	}

	@Override
	public boolean isByCompany() {
		return getFieldByName("C-" + FField.FNAME_COMPANY) != null;
	}

	@Override
	public FocList newFocList() {
		FocList list = super.newFocList();
		list.setDirectlyEditable(false);
		list.setDirectImpactOnDatabase(true);
//		FocListOrder order = new FocListOrder(FField.FLD_DATE);
//		order.setReverted(true);
//		list.setListOrder(order);
		return list;
	}

}
