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

import com.foc.admin.GrpMobileModuleRights;
import com.foc.admin.GrpMobileModuleRightsDesc;
import com.foc.list.FocList;
import com.foc.menuStructure.FocMenuItem;
import com.foc.vaadin.FocWebVaadinWindow;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.vaadin.gui.layouts.FVVerticalLayout;
import com.foc.vaadin.gui.menuTree.FVMenuTree;
import com.foc.web.modules.IWebModuleMenuCode;
import com.vaadin.ui.Button;

@SuppressWarnings("serial")
public class HomePage_AfterLogin_Mobile_Form extends HomePage_AfterLogin_Form{

	protected FVVerticalLayout getMobileHomeView(){
		return (FVVerticalLayout) getComponentByName("MOBILE_HOME_VIEW");
	}
	
	protected FVMenuTree getMenuTree(){
		FocWebVaadinWindow focWebVaadinWindow = (FocWebVaadinWindow) getMainWindow();
		return focWebVaadinWindow != null ? focWebVaadinWindow.getMenuTree(true) : null;
	}
	
	@Override
	protected void afterLayoutConstruction() {
		super.afterLayoutConstruction();
		FVVerticalLayout layout = getMobileHomeView();
		if(layout != null && GrpMobileModuleRightsDesc.getInstance().getFocList(FocList.LOAD_IF_NEEDED) != null){
			FocList list = GrpMobileModuleRightsDesc.getInstance().getFocList(FocList.LOAD_IF_NEEDED);
			for(int i=0; i<list.size(); i++){
				GrpMobileModuleRights grpMobileModuleRights = (GrpMobileModuleRights) list.getFocObject(i);
				if(grpMobileModuleRights.hasAccessRight(GrpMobileModuleRightsDesc.MAN_POWER)){
					FVButton employeeProjectChickInChartButton = new FVButton("Employee Project Checkin - Chart");
					employeeProjectChickInChartButton.addClickListener(new Button.ClickListener() {
						
						@Override
						public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
							if(getMenuTree() != null){
								FocMenuItem focMenuItem = getMenuTree().findMenuItem(IWebModuleMenuCode.PAYROLL_WEB_MODULLE_MENU_HR_WORKFORCE_DISTRIBUTION);
								if(focMenuItem != null){
									getMenuTree().clickMenuItem(getNavigationWindow(), focMenuItem.getCode());
								}
							}
						}
					});
					FVButton employeeProjectTableButton = new FVButton("Employee Project - Table");
					
					getMobileHomeView().addComponent(employeeProjectChickInChartButton);
//					getMobileHomeView().addComponent(employeeProjectTableButton);
				}else if(grpMobileModuleRights.hasAccessRight(GrpMobileModuleRightsDesc.ADDRESS_BOOK)){
					FVButton addressBookButton = new FVButton("Address Book");
					addressBookButton.addClickListener(new Button.ClickListener() {
						
						@Override
						public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
							if(getMenuTree() != null){
								FocMenuItem focMenuItem = getMenuTree().findMenuItem(IWebModuleMenuCode.BASICS_WEB_MODULE_MENU_CODE_ADR_BK_PARTY);
								if(focMenuItem != null){
									getMenuTree().clickMenuItem(getNavigationWindow(), focMenuItem.getCode());
								}
							}
						}
					});
					getMobileHomeView().addComponent(addressBookButton);
					
				}else if(grpMobileModuleRights.hasAccessRight(GrpMobileModuleRightsDesc.TIME_SHEET)){
					FVButton employeeTimesheetButton = new FVButton("Employee Timesheet");
					employeeTimesheetButton.addClickListener(new Button.ClickListener() {
						
						@Override
						public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
							if(getMenuTree() != null){
								FocMenuItem focMenuItem = getMenuTree().findMenuItem(IWebModuleMenuCode.PAYROLL_WEB_MODULLE_MENU_HR_EMPLOYEE_FILE);
								if(focMenuItem != null){
									getMenuTree().clickMenuItem(getNavigationWindow(), focMenuItem.getCode());
								}
							}
						}
					});
					getMobileHomeView().addComponent(employeeTimesheetButton);
					
				}
			}
		}
	}
}
