package com.foc.formula.function;

import java.text.NumberFormat;

import com.foc.formula.FFormulaNode;
import com.foc.formula.FunctionFactory;
import com.foc.gui.FGNumField;

public class FunctionStr extends Function {
	private static final String FUNCTION_NAME = "STR";
	private static final String OPERATOR_SYMBOL = "";

	public synchronized Object compute(FFormulaNode formulaNode){
		String result = "";		
		if(formulaNode != null){
			FFormulaNode childNode1 = (FFormulaNode) formulaNode.getChildAt(0);
			FFormulaNode childNode2 = (FFormulaNode) formulaNode.getChildAt(1);
			FFormulaNode childNode3 = (FFormulaNode) formulaNode.getChildAt(2);
			int nbrDecimals = childNode2 != null ? (int)childNode2.getCalculatedValue_double() : 0;
			boolean groupingUsed = childNode3 != null ? childNode3.getCalculatedValue_boolean() : false;
			if(childNode1 != null){
				double dVal = childNode1.getCalculatedValue_double();
				NumberFormat format = FGNumField.newNumberFormat(20, nbrDecimals, groupingUsed);
				result = format.format(dVal);
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
		return OPERATOR_SYMBOL;
	}
	
	public int getOperatorPriority(){
		return FunctionFactory.PRIORITY_MULTIPLICATIVE;
	}
}
