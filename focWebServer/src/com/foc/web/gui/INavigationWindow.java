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
package com.foc.web.gui;

import com.foc.dataWrapper.FocListWrapper;
import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.IRightPanel;
import com.foc.vaadin.gui.menuTree.FVMenuTree;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.vaadin.ui.Component;

public interface INavigationWindow {
	public FVMenuTree getMenuTree(boolean createIfNeeded);
	public void changeCentralPanelContent(ICentralPanel newCentralPanel, boolean keepPrevious);
	public void changeCentralPanelContent(ICentralPanel newCentralPanel, int previousMode);
	public ICentralPanel changeCentralPanelContent_ToTableForFocList(FocList focList);
	public ICentralPanel changeCentralPanelContent_ToTableForFocListWrapper(FocListWrapper focList);
	public ICentralPanel changeCentralPanelContent_ToFormForFocObject(FocObject focObject);
	public void goBack(ICentralPanel iCentralPanelToRemove);
	public void refreshCentralPanelAndRightPanel();
	public void addUtilityPanel(IRightPanel utilityPanel);
	public void removeUtilityPanel(IRightPanel utilityPanel);
	public void fillHomepageShortcutMenu(FocXMLLayout centralPanel);
	public void showValidationLayout(Component validationLayout);
	public void showHeaderLayout(Component headerLayout);
}
