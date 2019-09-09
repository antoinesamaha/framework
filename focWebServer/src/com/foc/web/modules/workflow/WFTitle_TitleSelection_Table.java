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
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.layouts.FVTableWrapperLayout;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLView;

@SuppressWarnings("serial")
public class WFTitle_TitleSelection_Table extends FocXMLLayout {

	@Override
	public void init(INavigationWindow window, XMLView xmlView, IFocData focData) {
		super.init(window, xmlView, focData);
	}
	
	@Override
	protected void afterLayoutConstruction() {
		super.afterLayoutConstruction();
	}

	private ArrayList<WFSite> selectedSiteList;
	private FocUser user = null;
	
	public ArrayList<WFSite> getSelectedSiteList() {
		return selectedSiteList;
	}

	public void setSelectedSiteList(ArrayList<WFSite> selectedSiteList) {
		this.selectedSiteList = selectedSiteList;
	}
	
	public FocUser getUser() {
		return user;
	}

	public void setUser(FocUser user) {
		this.user = user;
	}
	
	@Override
	public void dispose() {
		super.dispose();
		selectedSiteList = null;
	}
	
	public void insertRowsAfterTitleSelection(){
		for(int i=0; i<getSelectedSiteList().size(); i++){
			WFSite site = getSelectedSiteList().get(i);
			WFOperator operator = (WFOperator) site.getOperatorList().newEmptyItem();
			for(int j=0;j<getSelectedTitle().size();j++){
				operator.setTitle(getSelectedTitle().get(j));
			}
			operator.setUser(getUser());
			site.getOperatorList().add(operator);
			operator.validate(true);
			site.validate(true);
		}
	}
	
	@Override
	public boolean validationCheckData(FVValidationLayout validationLayout) {
		boolean error = super.validationCheckData(validationLayout);
		if(!error) {
			insertRowsAfterTitleSelection();
		}
		return error;
	}
	
	private ArrayList<WFTitle> getSelectedTitle(){
		ArrayList<WFTitle> titleList = new ArrayList<WFTitle>();
		FVTableWrapperLayout tableWrapper = (FVTableWrapperLayout) getComponentByName("TITLE_SELECTION_TABLE");
		if(tableWrapper != null && tableWrapper.getTableTreeDelegate() != null && tableWrapper.getTableTreeDelegate().selectionColumn_getSelectedIdArrayList() != null){
			ArrayList<Object> refList = tableWrapper.getTableTreeDelegate().selectionColumn_getSelectedIdArrayList();
			for(int i=0; i<refList.size();i++){
				long id = (Long) refList.get(i);
				WFTitle title = (WFTitle) getFocList().searchByReference(id);
				if(title != null){
					titleList.add(title);
				}
			}
		}
		return titleList;
	}
}
