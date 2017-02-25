package com.foc.formula.function;

import com.foc.formula.FFormulaNode;
import com.foc.formula.FunctionFactory;
import com.foc.util.FocMath;

public class FunctionEquals extends BooleanFunction {
	private static final String FUNCTION_NAME   = "EQUALS";
	private static final String OPERATOR_SYMBOL = "="     ;

	public boolean compareStrings(String string1, String string2){
		boolean result = false;
		
		boolean alreadyCompared = false;
		if(string1 != null && string2 != null){
			try{
				double v1 = Double.valueOf(string1);
				double v2 = Double.valueOf(string2);
				result = FocMath.equals(v1, v2);
//				result = v1 == v2;
				alreadyCompared = true;
			}catch(Exception e){
				//Globals.logString(e.getMessage());
			}
		}
		
		if(!alreadyCompared){
			if(string1 == null){
				result = string2 == null || string2.isEmpty() || string2.equalsIgnoreCase("null");
			}else{
				result = string2 == null ? false : string1.equals(string2);
			}
		}
		return result;
	}
	
	public synchronized Object compute(FFormulaNode formulaNode){
		boolean result = false;
		if(formulaNode != null){
			FFormulaNode childNode1 = (FFormulaNode) formulaNode.getChildAt(0);
			FFormulaNode childNode2 = (FFormulaNode) formulaNode.getChildAt(1);
			if(childNode1 != null && childNode2 != null){
				String string1 = childNode1.getCalculatedValue_String();
				String string2 = childNode2.getCalculatedValue_String();
				result = compareStrings(string1, string2);
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
		return FunctionFactory.PRIORITY_EQUALITY;
	}
}
