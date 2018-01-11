package com.foc.vaadin.gui.tableExports;

import com.foc.Globals;
import com.foc.util.Utils;
import com.foc.vaadin.gui.components.BlobResource;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public abstract class EXCELExport {
	private File textFile = null;
	private XSSFWorkbook workbook = null;
	private HashMap<String, EXCELSheet> sheetMap = new HashMap<String, EXCELSheet>();
	EXCELSheet fSheet = null;

	protected abstract String getFileName();
	protected abstract void fillFile();

	public void init() {
		createFile();
		fillFile();
		downloadFile();
	}

	public void dispose() {
		this.textFile = null;
		if(sheetMap != null){
			Iterator<EXCELSheet> iter = sheetMap.values().iterator();
			while(iter != null && iter.hasNext()) {
				EXCELSheet xl = iter.next();
				xl.dispose();
			}
			sheetMap.clear();
			sheetMap = null;
		}
	}

	public void createFile() {
		try{
			this.textFile = new File(getFileName() + ".xlsx");
			FileWriter localFileWriter = new FileWriter(this.textFile);
			this.workbook = new XSSFWorkbook();
			newSheet(getMainSheetName());
		}catch (Exception localException){
			localException.printStackTrace();
		}
	}

	public String getMainSheetName() {
		return getFileName();
	}
	
	public boolean switchToSheetOrCreateIfNeeded(String sheetName) {
		boolean created = false;
		if(switchToSheet(sheetName)) {
			newSheet(sheetName);
			created = true;
		}
		return created;
	}
	
	public boolean switchToSheet(String sheetName) {
		boolean error = true;
		if(sheetMap != null && sheetName != null){
			fSheet = sheetMap.get(sheetName);
			error = fSheet == null; 
		}
		return error;
	}

	public void newSheet(String sheetName) {
		try{
			XSSFSheet sheet = this.workbook.createSheet(sheetName);
			fSheet = new EXCELSheet(sheetName, sheet);
			if(sheetMap == null){
				sheetMap = new HashMap<String, EXCELSheet>();
			}
			sheetMap.put(sheetName, fSheet);
		}catch (Exception localException){
			localException.printStackTrace();
		}
	}

	public void setRightToLeft(boolean r2l) {
		if(fSheet != null) {
			fSheet.setRightToLeft(r2l);
		}
	}

	public void downloadFile() {
		try{
			File localFile = getFile();
			if(localFile != null){
				FileOutputStream localFileOutputStream = new FileOutputStream(localFile);
				this.workbook.write(localFileOutputStream);
				byte[] arrayOfByte = new byte[(int) localFile.length()];
				FileInputStream localFileInputStream = new FileInputStream(localFile);
				localFileInputStream.read(arrayOfByte);
				localFileInputStream.close();
				ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(arrayOfByte);
				String str = ".xlsx";
				BlobResource localBlobResource = new BlobResource(new File(""), localByteArrayInputStream, getFileName() + str);
				localBlobResource.openDownloadWindow();
			}
		}catch (Exception localException){
			localException.printStackTrace();
		}
	}

	public File getFile() {
		return this.textFile;
	}

	public void addCellValue(Object paramObject, int paramInt) {
		if(fSheet != null) fSheet.addCellValue(paramObject, paramInt);
	}

	public void addCellValue(Object paramObject) {
		if(fSheet != null) fSheet.addCellValue(paramObject);
	}

	public void addNewLine() {
		if(fSheet != null) fSheet.addNewLine();
	}

	public class EXCELSheet {
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
				XSSFCell localXSSFCell = this.row.createCell(this.columnCount++);
				if((paramObject instanceof String)){
					if(Utils.isNumeric((String) paramObject)){
						double d = Double.parseDouble((String) paramObject);
						localXSSFCell.setCellValue(d);
					}else{
						localXSSFCell.setCellValue((String) paramObject);
					}
				}
			}catch (Exception localException){
				Globals.logException(localException);
			}
		}

	}
}
