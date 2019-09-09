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
