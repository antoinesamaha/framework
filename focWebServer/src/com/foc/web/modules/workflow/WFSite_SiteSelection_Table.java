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
package com.foc.web.modules.workflow;

import java.util.ArrayList;

import com.foc.admin.FocUser;
import com.foc.business.workflow.WFOperator;
import com.foc.business.workflow.WFSite;
import com.foc.business.workflow.WFTitle;
import com.foc.business.workflow.WFTitleDesc;
import com.foc.list.FocList;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.FocCentralPanel;
import com.foc.vaadin.FocWebApplication;
import com.foc.vaadin.gui.layouts.FVTableWrapperLayout;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class WFSite_SiteSelection_Table extends FocXMLLayout {

	private boolean hasMultipleTitles;
	private FocUser user = null;
	
	public void insertRowsAfterSelection(){
		if(isHasMultipleTitles()){
			FocList focList = WFTitleDesc.getList(FocList.LOAD_IF_NEEDED);
      XMLViewKey xmlViewKey = new XMLViewKey(WFTitleDesc.getInstance().getStorageName(), XMLViewKey.TYPE_TABLE,WorkflowWebModule.CTXT_TITLE_SELECTION,XMLViewKey.VIEW_DEFAULT);
      WFTitle_TitleSelection_Table centralPanel = (WFTitle_TitleSelection_Table) XMLViewDictionary.getInstance().newCentralPanel(getMainWindow(), xmlViewKey, focList);
      centralPanel.setSelectedSiteList(getSelectedSites());
      centralPanel.setUser(getUser());
      Window titleSelectionWindow = null;
			FocCentralPanel centralWindow = new FocCentralPanel();
			centralWindow.fill();
			centralWindow.changeCentralPanelContent(centralPanel, false);
			titleSelectionWindow = centralWindow.newWrapperWindow();
			titleSelectionWindow.setPositionX(300);
			titleSelectionWindow.setPositionY(100);
			FocWebApplication.getInstanceForThread().addWindow(titleSelectionWindow);
			titleSelectionWindow.setModal(true);
		}else{
			WFTitle title = (WFTitle) WFTitleDesc.getInstance().getFocList().getFocObject(0);
			FocUser user = getUser();
			for(int i=0;i<getSelectedSites().size();i++){
				WFSite site = getSelectedSites().get(i);
				WFOperator operator  = (WFOperator) site.getOperatorList().newEmptyItem();
				operator.setTitle(title);
				operator.setUser(user);
				site.getOperatorList().add(operator);
				operator.validate(true);
			}
		}
	}
	
	@Override
	public boolean validationCheckData(FVValidationLayout validationLayout) {
		boolean error = super.validationCheckData(validationLayout);
		if(!error) {
			insertRowsAfterSelection();
		}
		return error;
	}
	
	private ArrayList<WFSite> getSelectedSites(){
		ArrayList<WFSite> sitesList = new ArrayList<WFSite>();
		FVTableWrapperLayout tableWrapper = (FVTableWrapperLayout) getComponentByName("SITE_SELECTION_TABLE");
		if(tableWrapper != null && tableWrapper.getTableTreeDelegate() != null && tableWrapper.getTableTreeDelegate().selectionColumn_getSelectedIdArrayList() != null){
			ArrayList<Object> refList = tableWrapper.getTableTreeDelegate().selectionColumn_getSelectedIdArrayList();
			for(int i=0; i<refList.size();i++){
				long id = (Long) refList.get(i);
				WFSite site = (WFSite) getFocList().searchByReference(id);
				if(site != null){
					sitesList.add(site);
				}
			}
		}
		return sitesList;
	}
	
	public boolean isHasMultipleTitles() {
		return hasMultipleTitles;
	}

	public void setHasMultipleTitles(boolean hasMultipleTitles) {
		this.hasMultipleTitles = hasMultipleTitles;
	}

	public FocUser getUser() {
		return user;
	}

	public void setUser(FocUser user) {
		this.user = user;
	}
}
