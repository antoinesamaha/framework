package com.foc.vaadin.gui.mswordGenerator;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;

public interface IMSWordContainer {
	public XWPFTable     insertTable();
	public XWPFParagraph insertParagraph();
}
