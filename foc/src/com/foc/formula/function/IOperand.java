package com.foc.formula.function;

import com.foc.formula.FFormulaNode;

public interface IOperand {
	public Object compute(FFormulaNode formulaNode);
	public void dispose();
}
