package com.foc.business.workflow.map;

import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;

public class WFStage extends FocObject {
	
	public WFStage(FocConstructor constr){
		super(constr);
		newFocProperties();
	}
	
	public boolean isApprovalStage(){
		return getPropertyBoolean(WFStageDesc.FLD_IS_APPROVAL);
	}
}
