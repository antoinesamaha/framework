package com.foc.desc.parsers;

import com.foc.business.workflow.implementation.FocWorkflowObject;
import com.foc.desc.FocConstructor;

public class ParsedFocObject extends FocWorkflowObject {

	public ParsedFocObject(FocConstructor constr) {
		super(constr);
		newFocProperties();
	}

}
