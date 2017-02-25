package com.foc.formula;

import java.util.HashMap;

public interface IFormulaContext {
	public abstract Object compute();
	public abstract Object evaluateFormula();
	public abstract Object evaluateExpression(String expression) throws FFormulaException;
	public abstract void commitValueToOutput(Object value);
	public abstract String getNewExpression(String oldExpression, HashMap<String, String> oldValuesNewValuedMap);

	public abstract void plugListeners();
	public abstract void unplugListeners();
	public abstract void dispose();

}
