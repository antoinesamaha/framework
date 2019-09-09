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
