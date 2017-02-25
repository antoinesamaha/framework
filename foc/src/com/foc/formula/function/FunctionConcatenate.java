package com.foc.formula.function;

import com.foc.formula.FFormulaNode;
import com.foc.formula.FunctionFactory;

public class FunctionConcatenate extends Function {
	private static final String FUNCTION_NAME   = "CONCATENATE";
	private static final String OPERATOR_SYMBOL = "";

	public synchronized Object compute(FFormulaNode formulaNode){
		String result = "";
		if(formulaNode != null){
			for(int i = 0; i < formulaNode.getChildCount(); i++){
				FFormulaNode childNode = (FFormulaNode) formulaNode.getChildAt(i);
				if(childNode != null){
					String childNodeCalculatedValue = childNode.getCalculatedValue_String();
					result += childNodeCalculatedValue;
				}
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
