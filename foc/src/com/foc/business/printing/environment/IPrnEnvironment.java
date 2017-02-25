package com.foc.business.printing.environment;

import com.foc.gui.FAbstractListPanel;

public interface IPrnEnvironment {
	public void init(FAbstractListPanel listPanel);
	public void free();
	public void dispose();
}
