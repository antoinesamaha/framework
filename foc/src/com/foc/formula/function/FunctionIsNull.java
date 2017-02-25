package com.foc.formula.function;

import com.foc.formula.FFormulaNode;
import com.foc.formula.FunctionFactory;

public class FunctionIsNull extends BooleanFunction {
	private static final String FUNCTION_NAME = "IS_NULL";
//	private static final String OPERATOR_SYMBOL = "="     ;
	
	public synchronized Object compute(FFormulaNode formulaNode){
		boolean result = false;
		if(formulaNode != null){			
			FFormulaNode childNode1 = (FFormulaNode) formulaNode.getChildAt(0);
			if(childNode1 != null){
				Object obj = childNode1.getCalculatedValue();
				result = obj == null;
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
