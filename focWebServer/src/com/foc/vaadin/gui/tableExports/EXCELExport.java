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

import com.foc.Globals;
import com.foc.vaadin.gui.components.BlobResource;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;

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

	public EXCELSheet bookmark() {
		EXCELSheet clone = null;
		try{
			clone = fSheet != null ? fSheet.clone() : null;
		}catch (CloneNotSupportedException e){
			Globals.logExceptionWithoutPopup(e);		
		}
		
		return clone;
	}
	
	public void gotoBookmark(EXCELSheet bookmark) {
		fSheet = bookmark;
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
}
