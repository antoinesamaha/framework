package com.foc.gui.table;

import java.awt.Graphics;

import javax.swing.JComponent;

public interface IFocCellPainter {
	public void beforePaint(JComponent jComponent, Graphics g);
	public void afterPaint(JComponent jComponent, Graphics g);
	public void clear();
}
