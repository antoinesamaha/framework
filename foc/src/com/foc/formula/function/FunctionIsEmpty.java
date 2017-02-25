package com.foc.formula.function;

import com.foc.formula.FFormulaNode;
import com.foc.formula.FunctionFactory;

public class FunctionIsEmpty extends BooleanFunction {
	private static final String FUNCTION_NAME   = "IS_EMPTY";
//	private static final String OPERATOR_SYMBOL = "="     ;
	
	public synchronized Object compute(FFormulaNode formulaNode){
		boolean result = false;
		if(formulaNode != null){			
			FFormulaNode childNode1 = (FFormulaNode) formulaNode.getChildAt(0);
			if(childNode1 != null){
				String string1 = childNode1.getCalculatedValue_String();
				result = string1 == null ? true : string1.trim().isEmpty();
			}else{
				result = true;
			}
		}
		return result;
	}
	
	public boolean needsManualNotificationToCompute() {
		return false;
	}
	
	public String getName(){
		return FUNCTION_NAME;
	}
	
	public String getOperatorSymbol(){
		return null;
	}
	
	public int getOperatorPriority(){
		return FunctionFactory.PRIORITY_EQUALITY;
	}
}
