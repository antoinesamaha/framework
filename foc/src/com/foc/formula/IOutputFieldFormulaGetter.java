package com.foc.formula;

import com.foc.gui.FAbstractListPanel;
import com.foc.property.FProperty;

public interface IOutputFieldFormulaGetter {
	public String getOutputFieldFormulaExpression(FAbstractListPanel table, FProperty selectedProperty, int row, int col);
}
