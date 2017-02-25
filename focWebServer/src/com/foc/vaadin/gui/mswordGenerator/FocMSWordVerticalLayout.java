package com.foc.vaadin.gui.mswordGenerator;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTable.XWPFBorderType;

import com.foc.Globals;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;

import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

public class FocMSWordVerticalLayout extends FocMSWordLayout {
	
	private float spacing = 0;
	
	public FocMSWordVerticalLayout(FocMSWordLayout pdfLayout, FocXMLAttributes attribute){
		super(pdfLayout, attribute);
	}

	public float getSpacing() {
		return spacing;
	}

	public void setSpacing(float spacing) {
		this.spacing = spacing;
	}

	public void debug(int indentation){
		Globals.logString(getDebugIndentation(indentation)+"Vertical - ("+getDebugString()+")");
		super.debug(indentation);
	}

	@Override
	public void write(IMSWordContainer container) {
		XWPFDocument wordDocument = getWordDocument();
		if(wordDocument != null){
			XWPFTable newTable = container.insertTable();
			applyBorderAttribute(newTable);
			
			for(int rowIndex=0; rowIndex<getComponentNumber(); rowIndex++){
				XWPFTableRow  tableRow  = rowIndex==0 ? newTable.getRow(0): newTable.createRow();
				
				XWPFTableCell tableCell = tableRow.getCell(0);//tableRow.addNewTableCell();
				
				MSWordWrapper wrapper = new MSWordWrapper(wordDocument, tableCell);
				
				FocMSWordComponent component = getComponentAt(rowIndex);
				if(component != null){
					component.write(wrapper);
				}
			}
		}		
	}
}
