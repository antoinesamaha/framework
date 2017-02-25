package com.foc.formula;

import com.foc.gui.FAbstractListPanel;
import com.foc.property.FProperty;

public interface ICellTitleBuilder {
	public String getCellTitle(FAbstractListPanel table, FProperty selectedProperty, int row, int col);
}
