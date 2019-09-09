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
package com.foc.vaadin.gui.tableExports;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.foc.Globals;
import com.foc.util.Utils;

public class EXCELSheet implements Cloneable {
	private String name = null;
	private XSSFSheet sheet = null;
	private int rowCount = 0;
	private int columnCount = 0;
	private XSSFRow row = null;
	private XSSFCell cell = null;

	public EXCELSheet(String name, XSSFSheet sheet) {
		this.sheet = sheet;
		this.name = name;
		rowCount = 0;
		columnCount = 0;
		row = null;
		cell = null;
	}

	public void dispose() {
		sheet = null;
		row = null;
		cell = null;
	}
	
	@Override
	protected EXCELSheet clone() throws CloneNotSupportedException {
		return (EXCELSheet) super.clone();
	}
	
	public void addNewLine() {
		try{
			if(this.sheet != null){
				this.row = this.sheet.createRow(this.rowCount++);
				this.columnCount = 0;
			}
		}catch (Exception localException){
			localException.printStackTrace();
		}
	}
	
	public void setRightToLeft(boolean r2l) {
		if(this.sheet != null){
			this.sheet.setRightToLeft(r2l);
		}
	}
	
	public void addCellValue(Object paramObject, int paramInt) {
		String str = "";
		try{
			while (paramInt > 0){
				str = str + " ";
				paramInt--;
			}
			addCellValue("\"" + str + paramObject + "\"");
		}catch (Exception localException){
			Globals.logException(localException);
		}
	}

	public void addCellValue(Object paramObject) {
		try{
			int currentColumn = this.columnCount++;
			if((paramObject instanceof String)){
				String value = (String) paramObject;
				if(value != null && !value.isEmpty()) {
					XSSFCell localXSSFCell = this.row.createCell(currentColumn);
					if(Utils.isNumeric(value)){
						double d = Double.parseDouble(value);
						localXSSFCell.setCellValue(d);
					}else{
						localXSSFCell.setCellValue(value);
					}
				}
			}
		}catch (Exception localException){
			Globals.logException(localException);
		}
	}

}
