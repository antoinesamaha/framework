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
package com.foc.web.unitTesting.suites.admin;

import com.foc.access.FocLogger;
import com.foc.web.unitTesting.FocUnitTest;
import com.foc.web.unitTesting.FocUnitTestingSuite;

public abstract class FocAdminAbstractTest extends FocUnitTest {
	  
	public static final String GUI_TABLE_WF_RIGHT_LEVEL = "WF_RIGHT_LEVEL";
	
	public FocAdminAbstractTest(String name, FocUnitTestingSuite suite) {
		super(name, suite);
	}
	
	protected void createTitle(String title) throws Exception {
		FocLogger.getInstance().openNode("Create '"+title+"' Title");
		menuAdmin_Click("WF_TITLE_TABLE");
		long ref = table_Add("WF_TITLE");
		componentInTable_SetValue("WF_TITLE", ref, "NAME", title, null);
		button_ClickApply();
		FocLogger.getInstance().closeNode();
	}
	
	protected void createSite(String siteName, String parentSiteName, String transactionPrefix) throws Exception {
		FocLogger.getInstance().openNode("Create '"+siteName+"' Site");
		menuAdmin_Click("SITE_TABLE");
		table_Add("SITE");
		component_SetValue("NAME", siteName, false);
		component_SetValue("DESCRIP", siteName, false);
		if(transactionPrefix != null) {
			component_SetValue("TRANSACTION_PREFIX", transactionPrefix, false);
		}
		component_SetValue("FATHER_NODE>NAME", parentSiteName, false);
		button_ClickApply();
		button_ClickDiscard();
		FocLogger.getInstance().closeNode();
	}
	
	protected void siteForm_AddTransactionRight(String transaction, String title, String rightLevel) throws Exception {
		long ref = table_Add("WF_USER_TRANS_RIGHT_LIST");
		componentInTable_SetValue("WF_USER_TRANS_RIGHT_LIST", ref, "TRANSACTION", transaction);
		componentInTable_SetValue("WF_USER_TRANS_RIGHT_LIST", ref, "TITLE", title);
		componentInTable_SetValue("WF_USER_TRANS_RIGHT_LIST", ref, "RIGHT_LEVEL", rightLevel);
	}
	
	protected void group_Add(String name) throws Exception {
		group_Add(name, name);
	}
	
	protected void group_Add(String name, String description) throws Exception {
		menuAdmin_Click("FOC_GROUP");
		table_Add("TABLE");
		component_SetValue("NAME", name, false);
		component_SetValue("DESCRIP", description, false);
		button_ClickApply();
		button_ClickDiscard();
	}
	
	protected void group_Open(String name) throws Exception {
		menuAdmin_Click("FOC_GROUP");
		table_Open("TABLE", "NAME", name);
	}
	
	protected void group_SetModuleRight(String module, String access) throws Exception {
		long ref = table_Select("MODULES_TABLE", "MODULE_NAME", module);
		componentInTable_SetValue("MODULES_TABLE", ref, "ACCESS", access);
	}
	
	protected void user_Add(String userName, String fullName, String group, String company, String title, String siteName) throws Exception {
		menuAdmin_Click("FOC_USER");
		table_Add("USER_TABLE");
		component_SetValue("NAME", userName, false);
		component_SetValue("FULL_NAME", fullName, false);
		component_SetValue("UGROUP", group, false);
		
		table_Add("COMAPNY_LIST");
		component_SetValue("COMPANY", company, false);
		component_SetValue("ACCESS_RIGHT", "Read Write", false);
		button_ClickApply();

		long ref = table_Select("SITE_SELECTION_TABLE", "NAME",  siteName);
		componentInTable_SetValue("SITE_SELECTION_TABLE", ref, "_SELECT", "true");
		button_ClickApply();
		
		ref = table_Select("TITLE_SELECTION_TABLE", "NAME",  title);
		componentInTable_SetValue("TITLE_SELECTION_TABLE", ref, "_SELECT", "true");
		button_ClickApply();

		button_ClickApply();
	}
}
