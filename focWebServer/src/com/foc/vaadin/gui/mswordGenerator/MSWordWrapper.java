/*******************************************************************************
 * Copyright 2016 Antoine Nicolas SAMAHA
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
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
