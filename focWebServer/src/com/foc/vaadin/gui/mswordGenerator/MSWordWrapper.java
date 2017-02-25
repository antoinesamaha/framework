package com.foc.vaadin.gui.mswordGenerator;

import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;

public class MSWordWrapper implements IMSWordContainer {
	private XWPFTableCell tableCell    = null;
	private XWPFDocument  wordDocument = null;
	
	public MSWordWrapper(XWPFDocument wordDocument, XWPFTableCell tableCell){
		this.wordDocument = wordDocument;
		this.tableCell = tableCell;
	}
	
	public MSWordWrapper(XWPFDocument wordDocument){
		this.wordDocument = wordDocument;
	}
	
	public void dispose(){
		tableCell = null;
		wordDocument = null;
	}

	@Override
	public XWPFTable insertTable() {
		XWPFTable newTable = null;
		if(tableCell != null){
//			wordDocument.getDocument().getDomNode()
//			XWPFTable newTable = new XWPFTable();
//			XmlCursor xmlCursor = wordDocument.getDocument().getBody().newCursor();
//			xmlCursor.to
//			tableCell.insertNewTbl(arg0)
			
//			XWPFParagraph dummyParaphraph = insertParagraph();
//			XWPFRun       run       = dummyParaphraph.createRun();
//			run.setText("Dummy");

			tableCell.setText("Wao");
			List<XWPFParagraph> listParagraphs = tableCell.getParagraphs();
			if(listParagraphs.size() > 0){
				XWPFParagraph paragraph = listParagraphs.get(0);
				tableCell.removeParagraph(0);
			}
			
			CTTbl cttbl = tableCell.getCTTc().addNewTbl();
			tableCell.getCTTc().addNewP();
			
			newTable = new XWPFTable(cttbl/*wordDocument.getDocument().getBody().getTblArray(0)*/, tableCell);
			tableCell.insertTable(0, newTable);
		}else	if(wordDocument != null){
			newTable = wordDocument.createTable();
		} 
		return newTable;
	}
	
	@Override
	public XWPFParagraph insertParagraph(){
		XWPFParagraph paragraph = null;
		if(tableCell != null){
			
			List<XWPFParagraph> listParagraphs = tableCell.getParagraphs();
			if(listParagraphs.size() > 0){
				paragraph = listParagraphs.get(0);
			}else{
				paragraph = tableCell.addParagraph();
			}
		}else	if(wordDocument != null){
			paragraph = wordDocument.createParagraph();
		}
		return paragraph;
	}
}
