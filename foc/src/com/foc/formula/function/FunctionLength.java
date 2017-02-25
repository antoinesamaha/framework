package com.foc.formula.function;

import com.foc.formula.FFormulaNode;
import com.foc.formula.FunctionFactory;

public class FunctionLength extends Function {
	private static final String FUNCTION_NAME   = "LEN";
	private static final String OPERATOR_SYMBOL = "";

	public synchronized Object compute(FFormulaNode formulaNode){
		int result = 0;
		if(formulaNode != null && formulaNode.getChildCount() > 0){
			FFormulaNode childNode = (FFormulaNode) formulaNode.getChildAt(0);
			if(childNode != null){
				String childNodeCalculatedValue = childNode.getCalculatedValue_String();
				result = childNodeCalculatedValue.length();
			}
		}
		return result;
	}
	
	public String getName(){
		return FUNCTION_NAME;
	}
	
	public String getOperatorSymbol(){
		return OPERATOR_SYMBOL;
	}
	
	public int getOperatorPriority(){
		return FunctionFactory.PRIORITY_ADDITIVE;
	}

	@Override
	public boolean needsManualNotificationToCompute() {
		return false;
	}
}
