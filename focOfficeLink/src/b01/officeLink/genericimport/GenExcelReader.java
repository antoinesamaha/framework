package b01.officeLink.genericimport;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.foc.Globals;
import com.foc.IFocEnvironment;
import com.foc.OptionDialog;
import com.foc.desc.FocObject;
import com.foc.desc.field.FField;
import com.foc.list.FocList;
import com.foc.property.FObject;
import com.foc.util.Utils;
//import com.foc.vaadin.FocWebEnvironment;

import b01.officeLink.excel.FocExcelSheet;

public abstract class GenExcelReader {
	
	protected abstract String isLineValid(GenExcelLine line);
	protected abstract void   scanAndFillLines(PrintStream logFile, ArrayList<GenExcelLine> lineList);
	
	private InputStream   inputStream = null;
	private Workbook      workbook    = null;
	private FocExcelSheet mainSheet   = null;
	
	private String endOfSheetField = null;
	private String logPrefix       = null;
	private HashMap<String, String> fieldIsDate = null;
	
	public GenExcelReader(InputStream inputStream, String endOfSheetField, String logPrefix) {
		this.inputStream     = inputStream;
		this.endOfSheetField = endOfSheetField;
		this.logPrefix       = logPrefix;
		fieldIsDate = new HashMap<String, String>();
	}
	
	public void execute() {
		openWorkbook();
		openSheets();
	}

	public void dispose() {
		if (mainSheet != null) {
			mainSheet.dispose();
			mainSheet = null;
		}
		workbook = null;
		dispose_Stream();
	}
	
	public void addDateField(String fieldName) {
		if(fieldIsDate != null) fieldIsDate.put(fieldName, fieldName); 
	}
	
	public boolean isDateField(String fieldName) {
		if(fieldIsDate != null) return fieldIsDate.get(fieldName) != null;
		return false;
	}
	
	public String getValueFromCell(FocExcelSheet sheet, String fieldName, int type, int line, int col) {
		return null;
	}
	
	public void copyString(FocObject focObj, GenExcelLine line, String fieldName) {
		if (focObj != null && line != null && !Utils.isStringEmpty(fieldName)) {
			focObj.setPropertyString(fieldName, line.get(fieldName));
		}
	}
	
	public void copyString(FocObject focObj, GenExcelLine line, String fieldName,String columnName) {
		if (focObj != null && line != null && !Utils.isStringEmpty(fieldName)) {
			focObj.setPropertyString(fieldName, line.get(columnName));
		}
	}
	
	public void copyDouble(FocObject focObj, GenExcelLine line, String fieldName) {
		if (focObj != null && line != null && !Utils.isStringEmpty(fieldName)) {
			double val = Utils.parseDouble(line.get(fieldName), 0);
			focObj.setPropertyDouble(fieldName, val);
		}
	}
	
	public void copyDouble(FocObject focObj, GenExcelLine line, String fieldName,String columnName) {
		if (focObj != null && line != null && !Utils.isStringEmpty(fieldName)) {
			double val = Utils.parseDouble(line.get(columnName), 0);
			focObj.setPropertyDouble(fieldName, val);
		}
	}
	
	public void copyInt(FocObject focObj, GenExcelLine line, String fieldName) {
		if (focObj != null && line != null && !Utils.isStringEmpty(fieldName)) {
			int val = Utils.parseInteger(line.get(fieldName), 0);
			focObj.setPropertyInteger(fieldName, val);
		}
	}
	
	public void copyInt(FocObject focObj, GenExcelLine line, String fieldName,String columnName) {
		if (focObj != null && line != null && !Utils.isStringEmpty(fieldName)) {
			int val = Utils.parseInteger(line.get(columnName), 0);
			focObj.setPropertyInteger(fieldName, val);
		}
	}
	
	public void copyBoolean(FocObject focObj, GenExcelLine line, String fieldName,String columnName) {
		if (focObj != null && line != null && !Utils.isStringEmpty(fieldName)) {
			boolean val = Boolean.parseBoolean(line.get(columnName));
			focObj.setPropertyBoolean(fieldName, val);
		}
	}

	public String copyObject(FocObject focObj, GenExcelLine line, String fieldName) {
		String error = null;
		
		if (focObj != null && line != null && !Utils.isStringEmpty(fieldName)) {
			String name = line.get(fieldName);
			
			if (!Utils.isStringEmpty(name)) {
				FObject objProp = (FObject) focObj.getFocPropertyByName(fieldName);
				FocList selectinoList = objProp.getPropertySourceList();
				
				FocObject selectedObject = selectinoList.searchByPropertyStringValue(FField.FNAME_NAME, name);
				if (selectedObject != null) {
					objProp.setObject(selectedObject);
				} else {
					error = "Selection "+name+" not found for "+fieldName;
				}
			}
		}
		
		return error;
	}

	private void openWorkbook() {
		if (inputStream != null) {
			try {
				workbook = WorkbookFactory.create(inputStream);
			} catch (Exception e) {
				Globals.logException(e);
			}
		}
	}

	protected FocExcelSheet openSheet(String name) {
		FocExcelSheet sheet = null;
		if (workbook != null) {
			Sheet xlSheet = workbook.getSheet(name);
			if (xlSheet != null) {
				sheet = new FocExcelSheet(xlSheet);
			} else {
				Globals.showNotification("Sheet not found!", name, IFocEnvironment.TYPE_ERROR_MESSAGE);
			}
		}
		return sheet;
	}

	public void dispose_Stream() {
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				Globals.logException(e);
			}
			inputStream = null;
		}
	}

	public void openSheets() {
		//FocList list = FocUser.getFocDesc().getFocList(FocList.FORCE_RELOAD);
		FocExcelSheet sheet = openSheet("DATA");
		if (sheet != null) {
			GenExcelSheetReader sheetReader = new GenExcelSheetReader(this, sheet, endOfSheetField);
			sheetReader.execute();
			
			if (sheetReader != null && Utils.isStringEmpty(sheetReader.getErrorMessage())) {
				OptionDialog dialog = new OptionDialog("Info", "Data is valid for import. Do you wish to save?") {
					@Override
					public boolean executeOption(String optionName) {
						if (optionName.equals("SAVE")) {
							try {
					    	PrintStream logFile    = null;
					    	
					    	try {
					    		logFile    = new PrintStream(Globals.logFile_GetFileName(logPrefix, "log"), "UTF-8");
					    	} catch(Exception e) {
					    		Globals.logException(e);
					    	}

								ArrayList<GenExcelLine> lineList = sheetReader.getItemArray();
					    	scanAndFillLines(logFile, lineList);
								
								if(logFile != null) {
									logFile.flush();
									logFile.close();
								}
							} catch (Exception e) {
								Globals.logException(e);
							}
						}
						sheetReader.dispose();
						return false;
					}
				};
				dialog.addOption("SAVE", "Save");
				dialog.addOption("CLOSE", "Don't Save");
				dialog.popup();

			} else {
				OptionDialog dialog = new OptionDialog("Alert", "There are missing info. Please fix and import again. <br> <br>" + sheetReader.getErrorMessage()) {
					@Override
					public boolean executeOption(String optionName) {
						return false;
					}
				};
				dialog.addOption("OK", "OK");
				dialog.popup();
			}
			sheetReader.setListOfData(null);
			sheetReader.setErrorMessage("");
//			sheetReader.dispose();
		}
	}
}
