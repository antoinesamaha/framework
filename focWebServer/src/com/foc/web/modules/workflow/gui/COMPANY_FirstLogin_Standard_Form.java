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
package com.foc.web.modules.workflow.gui;

import com.foc.business.company.Company;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.Reindeer;

@SuppressWarnings("serial")
public class COMPANY_FirstLogin_Standard_Form extends FocXMLLayout {
//  private FocListener userCompanyRightsListener = null;
  
  public Company getCompany(){
    return (Company) getFocData();
  }
  
  @Override
  public void init(com.foc.web.gui.INavigationWindow window, com.foc.web.server.xmlViewDictionary.XMLView xmlView, com.foc.shared.dataStore.IFocData focData) {
    super.init(window, xmlView, focData);
    /*
    FocList userCompanyList = getCompany().getUserRightsList();
    
    userCompanyRightsListener = new FocListener(){

      @Override
      public void focActionPerformed(FocEvent evt) {
        if((evt.getID() == FocEvent.ID_ITEM_ADD || evt.getID() == FocEvent.ID_ITEM_MODIFY || evt.getID() == FocEvent.ID_ITEM_REMOVE) && evt.getEventSubject() instanceof FocObject){
          UserCompanyRights userCompanyRights = (UserCompanyRights) evt.getEventSubject();
          if(userCompanyRights != null){
            FocUser user = userCompanyRights.getUser();
            if(user != null){
              user.clearCompanyList();
            }
          }
        }
      }

      @Override
      public void dispose() {
      } 
    };
    userCompanyList.addFocListener(userCompanyRightsListener);
    */
  };
  
  @Override
  protected void afterLayoutConstruction() {
  	super.afterLayoutConstruction();
		FVButton button = (FVButton) getComponentByName("NEXT_STEP");
		button.setStyleName(Reindeer.BUTTON_LINK);
		button.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
//				copyGuiToMemory();
//				AdrBookParty adrBookParty = (AdrBookParty) AdrBookPartyDesc.getInstance().getFocList().newEmptyItem();
//				if(getCompany() != null && getCompany().getName() != null && getCompany().getDescription() != null){
//					adrBookParty.setName(getCompany().getDescription());
//					adrBookParty.setCode(getCompany().getName());
//					adrBookParty.validate(true);
//					Company currentCompany = Globals.getApp().getCurrentCompany();
//					currentCompany.setDescription(getCompany().getDescription());
//					currentCompany.setName(getCompany().getName());
//					currentCompany.setAdrBookParty(adrBookParty);
//					currentCompany.validate(true);
//					UserSession.getInstanceForThread().setCompany(currentCompany);
//					Globals.getApp().getUser_ForThisSession().setCurrentCompany(currentCompany);
//					Globals.getApp().getUser_ForThisSession().validate(true);
//					FocUser newUser = (FocUser) FocUserDesc.getInstance().getFocList().iFocList_searchByPropertyValue(FocUserDesc.FLDNAME_NAME, "username");
//					FocGroup group = (FocGroup) FocGroupDesc.getInstance().getFocList().iFocList_searchByPropertyValue("NAME", "EMPTY_GROUP");
//					group.setName(getCompany().getName());
//					newUser.setGroup(group);
//					group.validate(true);
//					newUser.validate(true);
//					WFTitle title = (WFTitle) WFTitleDesc.getInstance().getFocList().newEmptyItem();
//					title.setName("GM");
//					title.validate(true);
//					WFSite site = (WFSite) currentCompany.getSiteList().newEmptyItem();
//					site.setName(getCompany().getName() +" Headquarters");
//					WFOperator operator = (WFOperator) site.getOperatorList().newEmptyItem();
//					operator.setUser(newUser);
//					operator.setTitle(title);
//					operator.validate(true);
//					RightLevel rightLevel = (RightLevel) RightLevelDesc.getInstance().getFocList().newEmptyItem();
//					rightLevel.setName("AllRights");
//					rightLevel.setAllowApprove(true);
//					rightLevel.setAllowCancel(true);
//					rightLevel.setAllowClose(true);
//					rightLevel.setAllowDeleteApprove(true);
//					rightLevel.setAllowDeleteDraft(true);
//					rightLevel.setAllowInsert(true);
//					rightLevel.setAllowModifyApproved(true);
//					rightLevel.setAllowModifyCodeApproved(true);
//					rightLevel.setAllowModifyCodeDraft(true);
//					rightLevel.setAllowModifyDraft(true);
//					rightLevel.setAllowPrintApprove(true);
//					rightLevel.setAllowPrintDraft(true);
//					rightLevel.setAllowRead(true);
//					rightLevel.setAllowUndoSignature(true);
//					rightLevel.validate(true);
//					UserTransactionRight userTransactionRight = (UserTransactionRight) site.getUserTransactionRightsList().newEmptyItem();
//					userTransactionRight.setUser(newUser);
//					userTransactionRight.setTitle(title);
//					userTransactionRight.setWFRightsLevel(rightLevel);
//					userTransactionRight.validate(true);
//					site.validate(true);
//					currentCompany.getSiteList().validate(true);
//					UserCompanyRights userCompany = (UserCompanyRights) currentCompany.getUserRightsList().newEmptyItem();
//					userCompany.setCompany(currentCompany);
//					userCompany.setUser(newUser);
//					userCompany.setAccessRight(UserCompanyRightsDesc.ACCESS_RIGHT_READ_WRITE);
//					userCompany.validate(true);
//					newUser.setCurrentCompany(currentCompany);
//					newUser.validate(true);
//					Globals.getApp().setCompany(currentCompany);
//					GrpWebModuleRights webModuleRight = group.getWebModuleRightsObject(AdminWebModule.MODULE_NAME);
//					if(webModuleRight != null){
//						webModuleRight.setRight(GrpWebModuleRightsDesc.ACCESS_FULL);
//						webModuleRight.validate(true);
//					}
//					webModuleRight = group.getWebModuleRightsObject("SITES_AND_WORKFLOW");
//					if(webModuleRight != null){
//						webModuleRight.setRight(GrpWebModuleRightsDesc.ACCESS_FULL);
//						webModuleRight.validate(true);
//					}
//					webModuleRight = group.getWebModuleRightsObject("FINANCE");
//					if(webModuleRight != null){
//						webModuleRight.setRight(GrpWebModuleRightsDesc.ACCESS_FULL);
//						webModuleRight.validate(true);
//					}
//					webModuleRight = group.getWebModuleRightsObject("INVENTORY");
//					if(webModuleRight != null){
//						webModuleRight.setRight(GrpWebModuleRightsDesc.ACCESS_FULL);
//						webModuleRight.validate(true);
//					}
//					group.getWebModuleRightsList().validate(true);
//					group.validate(true);
//					FocList listOfGroups = FocGroup.getList(FocList.LOAD_IF_NEEDED);
//					listOfGroups.validate(true);
//					Iterator<IFocDescDeclaration> focDescDeclarationIterator = Globals.getApp().getFocDescDeclarationIterator();
//			    while(focDescDeclarationIterator != null && focDescDeclarationIterator.hasNext()){
//			    	IFocDescDeclaration focDescDeclaration = focDescDeclarationIterator.next();
//			    	if(focDescDeclaration != null){
//			    		FocDesc focDesc = focDescDeclaration.getFocDescription();
//			    		if(focDesc instanceof AutoPopulatable){
//			    			AutoPopulatable autoPopulatable = (AutoPopulatable) focDesc;
//			    			if(autoPopulatable != null){
//			    				autoPopulatable.populate();
//			    			}
//			    		}
//			    	}
//			    }
//					for(int i=0; i<FocWebServer.getInstance().applicationConfigurators_Size(); i++){
//				    IApplicationConfigurator config = FocWebServer.getInstance().applicationConfigurators_Get(i);
//				    if(config.getCode().equals("LEBANESE_COMPANY_PRE_CONFIGURATION")){
//				    	config.run();
//				    }
//				  }
//					Globals.getApp().getDataSource().setEmptyDatabaseJustCreated(false);
//					XMLViewKey xmlViewKey = new XMLViewKey(AdminWebModule.STORAGE_HOMEPAGE, XMLViewKey.TYPE_FORM);
//			    ICentralPanel centralPanel = XMLViewDictionary.getInstance().newCentralPanel(getMainWindow() , xmlViewKey, null);
//			    getMainWindow().changeCentralPanelContent(centralPanel, true);
//				}
			}
		});
  }
}
