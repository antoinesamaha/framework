package com.foc.formula.function;

import com.foc.formula.FFormulaNode;
import com.foc.formula.FunctionFactory;
import com.foc.list.FocList;

public class FunctionListSize extends Function {
	private static final String FUNCTION_NAME = "SIZE";
	private static final String OPERATOR_SYMBOL = "";

	public synchronized Object compute(FFormulaNode formulaNode){
		int result = 0;
		if(formulaNode != null && formulaNode.getChildCount() == 1){
			Object listObject = ((FFormulaNode)formulaNode.getChildAt(0)).getCalculatedValue();
			if(listObject instanceof FocList){
				result = ((FocList)listObject).size();
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
