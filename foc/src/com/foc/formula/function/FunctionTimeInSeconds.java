package com.foc.formula.function;

import java.util.Date;

import com.foc.formula.FFormulaNode;
import com.foc.formula.FunctionFactory;
import com.foc.property.FTime;

public class FunctionTimeInSeconds extends Function {
	private static final String FUNCTION_NAME = "timeInSeconds";
	private static final String OPERATOR_SYMBOL = "";

	/*public Object compute() {
		double value = 0;
		IOperand operand0 = getOperandAt(0);
		//String value0Str = (String) operand0.compute();
		String value0Str = String.valueOf(operand0.compute());
		value = Double.valueOf(value0Str);
		
		int operandCount = getOperandCount();
		for(int i = 1; i < operandCount; i++){
			double operandValue = 0; 
			IOperand operand = getOperandAt(i);
			Object operandValueObj = operand.compute();
			if(operandValueObj != null){
				String operandValueStr = String.valueOf(operand.compute());
				operandValue = Double.valueOf(operandValueStr);
			}
			value -= operandValue;
		}
		return value;
	}*/
	
	public synchronized Object compute(FFormulaNode formulaNode){
		double result = 0;
		if(formulaNode != null){
			FFormulaNode childNode = (FFormulaNode) formulaNode.getChildAt(0);
			if(childNode != null){
				String timeStr = childNode.getCalculatedValue_String();
				Date   date    = timeStr != null ? FTime.convertStringToTime(timeStr) : new Date(0);
				result = date != null ? date.getTime() / 1000 : 0;
			}
		}
		return result;
	}
	
	/*
	public Object compute(FFormulaNode formulaNode){
		double result = 0;
		if(formulaNode != null){
			if(formulaNode.getChildCount() == 1){
				result = compute_UnaryMinus(formulaNode);
			}else{
				result = compute_NormalSubstract(formulaNode);
			}
		}
		return result;
	}
	*/
	
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
