package com.fab.model.table;

import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;

@SuppressWarnings("serial")
public class FabMultipleChoice extends FocObject {
	
	public FabMultipleChoice(FocConstructor constr){
		super(constr);
		newFocProperties();
	}
	
	public int getIntValue(){
		return getPropertyInteger(FabMultipleChoiceDesc.FLD_INT_VALUE);
	}
	
	public String getDisplayText(){
		return getPropertyString(FabMultipleChoiceDesc.FLD_DISPLAY_TEXT);
	}
	
	public FabMultiChoiceSet getMultipleChoiceSet(){
		return (FabMultiChoiceSet) getPropertyObject(FabMultipleChoiceDesc.FLD_MULTIPLE_CHOICE_SET);
	}
}
