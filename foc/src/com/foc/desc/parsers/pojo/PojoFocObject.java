package com.foc.desc.parsers.pojo;

import com.foc.business.workflow.implementation.FocWorkflowObject;
import com.foc.desc.FocConstructor;

public class PojoFocObject extends FocWorkflowObject {

	public PojoFocObject(FocConstructor constr){
    super(constr);
    newFocProperties();
	}
	
	public void dispose(){
		super.dispose();
	}

  //Implementation of the Workflow
  //------------------------------

	//------------------------------
}
