package com.foc.formula;

import java.util.HashMap;

import com.foc.desc.FocObject;
import com.foc.desc.field.FFieldPath;

public class FocSimpleFormulaContext extends FocAbstractFormulaContext {
	private FocObject originFocObject = null;
	
	public FocSimpleFormulaContext(Formula formula, FFieldPath outputFieldPath) {
		super(formula, outputFieldPath);
	}
	
	public FocSimpleFormulaContext(Formula formula) {
		this(formula, null);
	}
	
	public void dispose(){
		originFocObject = null;
	}

	public Object compute(FocObject originFocObject){
		this.originFocObject = originFocObject;
		return compute();
	}
	
	@Override
	public void plugListeners(String expression) {
	}

	@Override
	public void unplugListeners(String expression) {
	}

	public String getNewExpression(String oldExpression, HashMap<String, String> oldValuesNewValuedMap) {
		return null;
	}

	@Override
	public FocObject getOriginFocObject() {
		return originFocObject;
	}
	
	public boolean computeBooleanValue(FocObject focObj){
		boolean visible = false;
    Object valueObj = compute(focObj);
    if(valueObj instanceof Boolean){
      visible = ((Boolean)valueObj).booleanValue();
    }else if(valueObj instanceof String){
      visible = ((String)valueObj).toUpperCase().equals("TRUE");                    
    }
    return visible;
	}
}
