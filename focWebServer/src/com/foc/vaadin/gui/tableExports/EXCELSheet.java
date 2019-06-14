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