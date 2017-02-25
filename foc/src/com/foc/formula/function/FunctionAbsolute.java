package com.foc.formula.function;

import com.foc.formula.FFormulaNode;
import com.foc.formula.FunctionFactory;

public class FunctionAbsolute extends Function {
	private static final String FUNCTION_NAME = "ABS";
	
	public synchronized Object compute(FFormulaNode formulaNode){
		double result = 0;
		if(formulaNode != null && formulaNode.getChildCount() == 1){
			FFormulaNode childNode = (FFormulaNode) formulaNode.getChildAt(0);
			if(childNode != null){
				double childNodeCalculatedValue = childNode.getCalculatedValue_double();
				result = childNodeCalculatedValue;
				if(result < 0) result = -result;
			}
		}
		return result;
	}
	
	public String getName(){
		return FUNCTION_NAME;
	}
	
	public String getOperatorSymbol(){
		return "";
	}
	
	public int getOperatorPriority(){
		return FunctionFactory.PRIORITY_ADDITIVE;
	}

	@Override
	public boolean needsManualNotificationToCompute() {
		return false;
	}

}
