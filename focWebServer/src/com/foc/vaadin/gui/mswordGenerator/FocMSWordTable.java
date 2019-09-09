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

import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.property.FProperty;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;

public class FocMSWordTable extends FocMSWordLayout{

	public FocMSWordTable(FocMSWordLayout pdfParent, FocXMLAttributes xmlAttribute) {
		super(pdfParent, xmlAttribute);
	}
	
	@Override
	public void write(IMSWordContainer container) {
		XWPFTable table = container.insertTable();
		IFocData rootFocData = getFocData();
		if(table != null && rootFocData != null && getXmlAttribute() != null){
			
			String dataPath = getXmlAttribute().getValue(FXML.ATT_DATA_PATH);
			
			IFocData focData = rootFocData.iFocData_getDataByPath(dataPath);
			if(focData == null){
				focData = rootFocData;
			}
			if(focData != null){
				if(focData != null && focData instanceof FocList){
					FocList focList = (FocList) focData;
					
					int numberOfRows    = focList.size();
					int numberOfColumns = getComponentNumber();
									
					FocObject focObject = null;
						
					XWPFTableRow row = table.getRow(0);
					row.setRepeatHeader(true);
					
					for(int columnIndex=0;columnIndex<numberOfColumns; columnIndex++){
						FocMSWordTableColumn tableColumn = (FocMSWordTableColumn) getComponentAt(columnIndex);
						String caption = tableColumn.getXmlAttribute().getValue(FXML.ATT_CAPTION);
						addCell(row, columnIndex, caption, true);
					}
					
					for(int rowIndex=0;rowIndex<numberOfRows; rowIndex++){
						row = table.createRow();
						focObject = focList.getFocObject(rowIndex);
						for(int columnIndex=0;columnIndex<numberOfColumns; columnIndex++){
							FocMSWordTableColumn tableColumn = (FocMSWordTableColumn) getComponentAt(columnIndex);
							IFocData data = focObject.iFocData_getDataByPath(tableColumn.getDataPath());
							if(data != null && data instanceof FProperty){
								addCell(row, columnIndex, data.toString());
							}else{
								addCell(row, columnIndex, "");
							}
						}
					}
				}
			}
		}
	}
	
	private void addCell(XWPFTableRow row, int columnIndex, String content){
		addCell(row, columnIndex, content, false);
	}
	
	
	private void addCell(XWPFTableRow row, int columnIndex, String content, boolean isTitleRow){
		XWPFTableCell cell = null; 
		if(row.getCell(columnIndex) == null){
			cell = row.addNewTableCell();
			cell.setText(content);
		}else{
			cell = row.getCell(columnIndex);
			cell.setText(content);
		}
		if(isTitleRow){
			cell.setColor("838b83");
		}
	}
	
//	private void addCell(XWPFTableRow row, int columnIndex, String content){
//		if(row.getCell(columnIndex) == null){
//			row.addNewTableCell().setText(content);
//		}else{
//			row.getCell(columnIndex).setText(content);
//		}
//	}
}
