package com.foc.formula.function;

import com.foc.formula.FFormulaNode;
import com.foc.formula.FunctionFactory;

public class FunctionStringContains extends Function {
	private static final String FUNCTION_NAME = "CONTAINS";

	public synchronized Object compute(FFormulaNode formulaNode){
		boolean result = false;
		if(formulaNode != null && formulaNode.getChildCount() == 2){
			FFormulaNode child1 = (FFormulaNode) formulaNode.getChildAt(0);
			FFormulaNode child2 = (FFormulaNode) formulaNode.getChildAt(1);
			
			if(child1 != null && child2 != null){
				String str1 = child1.getCalculatedValue_String().toUpperCase();
				String str2 = child2.getCalculatedValue_String().toUpperCase();
				result = str1.contains(str2);
			}
		}
		return result;
	}
	
	public String getName(){
		return FUNCTION_NAME;
	}
	
	public String getOperatorSymbol(){
		return null;
	}
	
	public int getOperatorPriority(){
		return FunctionFactory.PRIORITY_ADDITIVE;
	}

	@Override
	public boolean needsManualNotificationToCompute() {
		return false;
	}
}
