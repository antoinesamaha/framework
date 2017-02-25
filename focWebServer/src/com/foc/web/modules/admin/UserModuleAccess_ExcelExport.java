package com.foc.web.modules.admin;

import com.foc.admin.userModuleAccess.UserModuleAccess;
import com.foc.admin.userModuleAccess.UserModuleList;
import com.foc.vaadin.gui.tableExports.CSVExport;

public class UserModuleAccess_ExcelExport extends CSVExport {

	private UserModuleList userModuleAccess_ExcelExport = null;
	
	public UserModuleAccess_ExcelExport(UserModuleList userModuleAccess_ExcelExport){
		this.userModuleAccess_ExcelExport = userModuleAccess_ExcelExport; 
	}

	public void dispose(){
		super.dispose();
		userModuleAccess_ExcelExport = null;
	}
	
	@Override
	protected String getFileName() {
		return "User_Access_To_Everpro";
	}

	@Override
	protected void fillFile() {
		addCellValue("USER");
		addCellValue("GROUP");
		addCellValue("MODULE");
		addCellValue("PRICE");
		addNewLine();
		
		for(int i=0; i<userModuleAccess_ExcelExport.size(); i++){
			UserModuleAccess mat = (UserModuleAccess) userModuleAccess_ExcelExport.getFocObject(i);
			addCellValue(mat.getUser().getName());
			addCellValue(mat.getGroup().getName());
			addCellValue(mat.getModule());
			addCellValue("0");
			addNewLine();
		}
		
	}

}
