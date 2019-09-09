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

import com.foc.Application;
import com.foc.Globals;
import com.foc.admin.FocUser;
import com.foc.admin.FocUserHistory;
import com.foc.admin.FocUserHistoryConst;
import com.foc.admin.FocUserHistoryDesc;
import com.foc.admin.FocUserHistoryList;
import com.foc.business.company.Company;
import com.foc.business.workflow.WFSite;
import com.foc.business.workflow.WFTitle;
import com.foc.list.FocList;
import com.foc.vaadin.FocWebApplication;
import com.foc.vaadin.FocWebVaadinWindow;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.vaadin.gui.menuTree.FVMenuTree;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.modules.workflow.WFTransactionWrapper_TransactionHistory_Table;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class HomePage_AfterLogin_Form extends FocXMLLayout {

	@Override
	public void showValidationLayout(boolean showBackButton) {
	}
	
  @Override
  public void init(com.foc.web.gui.INavigationWindow window, com.foc.web.server.xmlViewDictionary.XMLView xmlView, com.foc.shared.dataStore.IFocData focData) {
    super.init(window, xmlView, focData);
    Application application = Globals.getApp();
    if(application != null){
	    FocUser user = application.getUser_ForThisSession();
	    if(user != null){
		    user.dispose_CompanyList();
		    user.dispose_SitesList();
		    user.dispose_TitlesList();
	    
		    FocList companyList = user.getCompanyList();
		    
		    if(companyList != null){
		      Company currentCompany = Globals.getApp().getCurrentCompany();
		      if(currentCompany != null){
		        if(!companyList.containsObject(currentCompany)){
		          user.setCurrentCompany(null);
		        }
		      }
		    	
		      if(Globals.getApp().getCurrentCompany() == null){
		      	Company company = (Company) companyList.getAnyItem();
		      	user.setCurrentCompany(company);
		      }
		    }
		    
		    FocList sitesList = user.getSitesList();
		    sitesList.reloadFromDB();
		    
		    if(sitesList != null){
		      WFSite currentSite = Globals.getApp().getCurrentSite();
		      if(currentSite != null){
		        if(!sitesList.containsObject(currentSite)){
		          user.setCurrentSite(null);
		        }
		      }
		    	
		      if(Globals.getApp().getCurrentSite() == null){
		      	WFSite site = (WFSite) sitesList.getAnyItem();
		      	user.setCurrentSite(site);
		      }
		    }
		    
		    FocList titlesList = user.getTitlesList();
		    if(titlesList != null){
		      WFTitle currentTitle = Globals.getApp().getCurrentTitle();
		      if(currentTitle != null){
		        if(!titlesList.containsObject(currentTitle)){
		          user.setCurrentTitle(null);
		        }
		      }
		    	
		      if(Globals.getApp().getCurrentTitle() == null){
		      	WFTitle title = (WFTitle) titlesList.getAnyItem();
		      	user.setCurrentTitle(title);
		      }
		    }
	    }
    }
  }
  
  protected void afterLayoutConstruction() {
    FocUserHistoryList historyList = (FocUserHistoryList) FocUserHistoryDesc.getInstance().getFocList();
    historyList.loadIfNotLoadedFromDB();

    int screenSizeMode = historyList.retrieveFullScreenSettings(FocWebApplication.getFocUser());

    switch (screenSizeMode) {
    case FocUserHistoryConst.MODE_WINDOWED: {
      FocWebVaadinWindow w = (FocWebVaadinWindow) getMainWindow();
      w.setFullScreenMode(FocWebVaadinWindow.FORMAT_PORTRAIT);
      break;
    }
    case FocUserHistoryConst.MODE_DEFAULT:
    case FocUserHistoryConst.MODE_FULLSCREEN: {
      FocWebVaadinWindow w = (FocWebVaadinWindow) getMainWindow();
      w.setFullScreenMode(FocWebVaadinWindow.FORMAT_FULL_SCREEN);
      break;
    }
    default:
      break;

    }

    FVButton clearHistoryButton = (FVButton) getComponentByName("CLEAR_HISTORY");
    if(clearHistoryButton != null){
      clearHistoryButton.addClickListener(new ClickListener() {
        
        @Override
        public void buttonClick(ClickEvent event) {
          clearHistory(false);
        }
      });
    }
    
    FVButton clearTransactionButton = (FVButton) getComponentByName("CLEAR_TRANSACTION_HISTORY");
    if(clearTransactionButton != null){
    	clearTransactionButton.addClickListener(new ClickListener() {
        
        @Override
        public void buttonClick(ClickEvent event) {
          clearHistory(true);
        }
      });
    }
    if(getMainWindow() != null){
    	getMainWindow().fillHomepageShortcutMenu(this);
    }
  }
  
  private void clearHistory(boolean transaction) {
    FVMenuTree component = (FVMenuTree) getComponentByName("MENU_HISTORY");
    FocUserHistory history = component.getHistoryList().findHistory(FocWebApplication.getFocUser());
    if(transaction){
    	history.clearRecentTransdactions();
    }else{
    	history.clearHistory();
    }
    history.validate(true);

    WFTransactionWrapper_TransactionHistory_Table wfTable = (WFTransactionWrapper_TransactionHistory_Table) getComponentByName("TRANSACTION_HISTORY");
    if(wfTable != null) wfTable.dispose();
    
    component.dispose();
    component.fill();
    re_parseXMLAndBuildGui();
  }

}
