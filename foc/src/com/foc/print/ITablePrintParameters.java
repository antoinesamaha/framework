package com.foc.print;

import java.awt.Graphics;

import com.foc.gui.table.print.FTablePrintable;

public interface ITablePrintParameters {
	public void setPrintable(FTablePrintable tablePrintable);
	public void dispose();
	public int  getPageHeaderHeight();
	public int  getPageFooterHeight();
	public void printHeader(Graphics graphics);
	public void printFooter(Graphics graphics);
}
