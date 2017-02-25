package com.foc.formula.function;

import com.foc.formula.FFormulaNode;
import com.foc.formula.FunctionFactory;
import com.foc.util.FocMath;

public class FunctionRound extends BooleanFunction {
	private static final String FUNCTION_NAME   = "ROUND";
//	private static final String OPERATOR_SYMBOL = "="     ;
	
	
	//First node in formula represents the value to round.
	//Second node in formula represents the precision.
	public synchronized Object compute(FFormulaNode formulaNode){
		double result = 0.0;

		if(formulaNode != null && formulaNode.getChildCount() >= 2){
			FFormulaNode childNode1 = (FFormulaNode) formulaNode.getChildAt(0);
			FFormulaNode childNode2 = (FFormulaNode) formulaNode.getChildAt(1);

			if(childNode1 != null && childNode2 != null){
				double valueToRound = childNode1.getCalculatedValue_double();
				double precision    = childNode2.getCalculatedValue_double();

				result = FocMath.round(valueToRound, precision);
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
