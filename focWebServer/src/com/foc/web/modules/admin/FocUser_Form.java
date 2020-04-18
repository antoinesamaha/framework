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

import java.util.ArrayList;

import com.foc.Globals;
import com.foc.IFocEnvironment;
import com.foc.access.FocDataConstant;
import com.foc.access.FocDataMap;
import com.foc.admin.FocUser;
import com.foc.business.adrBook.Contact;
import com.foc.business.company.Company;
import com.foc.business.company.CompanyDesc;
import com.foc.business.company.UserCompanyRights;
import com.foc.business.company.UserCompanyRightsDesc;
import com.foc.business.config.BusinessConfig;
import com.foc.business.notifier.FocNotificationEmail;
import com.foc.business.notifier.FocNotificationEmailTemplate;
import com.foc.business.workflow.WFOperator;
import com.foc.business.workflow.WFSite;
import com.foc.business.workflow.WFSiteDesc;
import com.foc.business.workflow.WFTitle;
import com.foc.business.workflow.WFTitleDesc;
import com.foc.business.workflow.rights.UserTransactionRightDesc;
import com.foc.dataWrapper.FocListWrapper;
import com.foc.desc.FocObject;
import com.foc.event.FocEvent;
import com.foc.event.FocListener;
import com.foc.list.FocList;
import com.foc.saas.manager.SaaSApplicationAdaptor;
import com.foc.saas.manager.SaaSConfig;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.util.Utils;
import com.foc.vaadin.FocCentralPanel;
import com.foc.vaadin.FocWebApplication;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.vaadin.gui.components.FVButtonClickEvent;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.windows.UserChangePasswordWindow;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.modules.workflow.WFSite_SiteSelection_Table;
import com.foc.web.modules.workflow.WFTitle_TitleSelection_Table;
import com.foc.web.modules.workflow.WorkflowWebModule;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.server.Page;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class FocUser_Form extends FocXMLLayout {
	
	private FocListener userCompanyRightsListener     = null;
	
	protected FocUser getFocUser(){
		return (FocUser) getFocObject();
	}
	
  @Override
  public void init(com.foc.web.gui.INavigationWindow window, com.foc.web.server.xmlViewDictionary.XMLView xmlView, com.foc.shared.dataStore.IFocData focData) {
    super.init(window, xmlView, focData);
    FocList userCompanyList = getFocUser().getCompanyRightsList();
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
    fillCompanyRightsIfUnique();
  };

	
	@Override
	public void showValidationLayout(boolean showBackButton) {
		super.showValidationLayout(showBackButton);
		if(showBackButton && getValidationLayout() != null){
			/*
	  	Button emailButton = (Button) getValidationLayout().getEmailButton(true);
	  	if(emailButton != null){
	  		emailButton.addClickListener(new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						FocUser focUser = getFocUser();
						if(focUser != null && focUser.getContact() != null){
							String email = focUser.getContact().getEMail();
							if(email != null && !email.isEmpty()){
								String name = focUser.getContact().getFullName(); 
								String javaScript = "window.location = 'mailto:" + email + "?body=Dear "+name+",%20%0d%0a%20%0d%0aPlease find here under your username and password to track online your deliveries:%20%0d%0aUsername:"+focUser.getName()+"%20%0d%0aPassword:"+focUser.getPassword()+"%20%0d%0a%20%0d%0aRegards,'";
			          JavaScript.getCurrent().execute(javaScript);
							}
						}
					}
				});
	  	}
	  	*/
  	}
	}
	
	@Override
  protected void afterLayoutConstruction(){
		super.afterLayoutConstruction();
  	FVButton changePasswordBtn = (FVButton) getComponentByName("CHANGE_PASSWORD");
    
  	if(changePasswordBtn != null){
	  	changePasswordBtn.addClickListener(new ClickListener() {
	      @Override
	      public void buttonClick(ClickEvent event) {
	        changePassword();
	      }
	    });
  	}
  	
  	FVButton resetPasswordBtn = (FVButton) getComponentByName("RESET_PASSWORD");
  	if(resetPasswordBtn != null){
  		resetPasswordBtn.addClickListener(new ClickListener() {
	      @Override
	      public void buttonClick(ClickEvent event) {
	        resetPassword();	        
	      }
	    });
  	}  	
  }
	
	// To automatically fill the company and company rights if only one company exists
	protected void fillCompanyRightsIfUnique() {
		FocUser user = getFocUser();
		FocList companyList = CompanyDesc.getInstance().getFocList();
		if(user != null && user.isCreated() && companyList != null && companyList.size() == 1) {			
			FocList compRightsList = user.getCompanyRightsList();
	  	UserCompanyRights userRight = (UserCompanyRights) compRightsList.newEmptyItem();
	  	userRight.setCompany((Company) companyList.getFocObject(0));
	  	userRight.setAccessRight(UserCompanyRightsDesc.ACCESS_RIGHT_READ_WRITE);    	
	  	compRightsList.add(userRight);
		}
	}
  
  protected void resetPassword() {
    boolean error = getValidationLayout().commit();//In case the user is still created, the commit makes sure it gets saved with a real reference before updating the Password.
    if(!error){
    	FocUser user = (FocUser) getFocData();
    	changePasswordForUser(user);
    }
  }

  public static void changePasswordForUser(FocUser focUser){
  	String newPassStr = focUser.resetPassword(); 

		if(focUser != null && focUser.getContact() != null){
			String email = focUser.getContact().getEMail();
			if(email != null && !email.isEmpty()){
				String name = focUser.getContact().getFullName();
				String ENT = "%20%0d%0a";
				String javaScript = "window.location = 'mailto:" + email + "?body=Dear "+name+","+ENT+ENT;
				javaScript += "You can have real time follow up of your goods delivery on our online website: "+ENT+Page.getCurrent().getLocation()+ENT+ENT;
				javaScript += "Username: "+focUser.getName()+ENT;
				javaScript += "Password: "+newPassStr+ENT+ENT;
				javaScript += "Regards,'";
        JavaScript.getCurrent().execute(javaScript);
			}
		}
  }
  
  private void changePassword() {
    boolean error = getValidationLayout().commit();//In case the user is still created, the commit makes sure it gets saved with a real reference before updating the Password.
    if(!error){
    	FocUser user = (FocUser) getFocData();
    	UserChangePasswordWindow loginWindow = new UserChangePasswordWindow(user, true);
    	loginWindow.init();
      FocWebApplication.getInstanceForThread().addWindow(loginWindow);
    }
  }
  
  protected Contact getRelatedContact(){
  	return getFocUser() != null ? getFocUser().getContact() : null;
  }
  
  protected void setRelatedContact(Contact contact){
  	if(getFocUser() != null){
  		getFocUser().setContact(contact);
  	}
  }
   
  @Override
	public boolean validationCheckData(FVValidationLayout validationLayout) {
		boolean error = super.validationCheckData(validationLayout);
		if(!error) {
			boolean result = executeSiteOperatorChecks();
		//if(!error) error = result;  // uncomment this if you want to totally forbid the admin to create a user without WFOperator for a company
		}
		return error;
	}
  
  private boolean executeSiteOperatorChecks() {
  	boolean result = true;
  	FocList companyRightsList = getFocUser().getCompanyRightsList();			
		for(int i=0; i < companyRightsList.size(); i++) {		
			UserCompanyRights userRight = (UserCompanyRights) companyRightsList.getFocObject(i);
			Company comp = userRight.getCompany();
			if(comp != null) {
				ArrayList<WFSite> siteList = WFSiteDesc.getSiteListForCompany(comp);
				if(siteList != null && siteList.size() > 0) {
					WFOperator op = null;
					for(int z = 0; z < siteList.size() && op==null; z++) {
						WFSite site = siteList.get(z);
						op = getSiteOperatorForUser(site);
					}
					if(op == null) {
						WFSite site = setSiteOperatorForCompanyRight((UserCompanyRights) companyRightsList.getFocObject(i));
						if(site != null) {
							result = false;
							if(UserTransactionRightDesc.getInstance() != null) {
								FocList list = WFTitleDesc.getInstance().getFocList(FocList.LOAD_IF_NEEDED);
								if(list != null) list.validate(true);
							}
							site.validate(true);
						}
					}
				}
			}
		}
		return result;
  }
  
  private WFOperator getSiteOperatorForUser(WFSite site){
  	WFOperator operator= null;
  	if(site != null) {
  		FocList siteOperatorList = site.getOperatorList();
			for(int i=0; i < siteOperatorList.size() && operator == null; i++) {
				WFOperator opr = (WFOperator) siteOperatorList.getFocObject(i);
				if(opr.getUser().equals(getFocUser())) operator = opr;
			}
  	}
  	return operator;
  }

	private WFSite setSiteOperatorForCompanyRight(UserCompanyRights right) {
//		boolean error = false;
		WFSite site = null;
		if(right != null 
				&& right.getCompany() != null 
				&& right.getCompany().getSiteListSize() > 0){
			if(right.getCompany().getSiteListSize() == 1){
				if(WFTitleDesc.getInstance().getFocList().size() == 1){
					site = (WFSite) right.getCompany().getAnySite();
					WFTitle title = (WFTitle) WFTitleDesc.getInstance().getFocList().getFocObject(0);
					WFOperator operator = (WFOperator) site.getOperatorList().newEmptyItem();
					operator.setCreated(true);
					operator.setUser(right.getUser());
					operator.setTitle(title);
				}else{
					ArrayList<WFSite> selectedSiteList = new ArrayList<WFSite>();
					selectedSiteList.add((WFSite) right.getCompany().getAnySite());
					FocList focList = WFTitleDesc.getList(FocList.LOAD_IF_NEEDED);
	        XMLViewKey xmlViewKey = new XMLViewKey(WFTitleDesc.getInstance().getStorageName(), XMLViewKey.TYPE_TABLE,WorkflowWebModule.CTXT_TITLE_SELECTION,XMLViewKey.VIEW_DEFAULT);
	        WFTitle_TitleSelection_Table centralPanel = (WFTitle_TitleSelection_Table) XMLViewDictionary.getInstance().newCentralPanel((FocCentralPanel) getMainWindow(), xmlViewKey, focList);
	        centralPanel.setSelectedSiteList(selectedSiteList);
	        centralPanel.setUser(right.getUser());
	        Window titleSelectionWindow = null;
					FocCentralPanel centralWindow = new FocCentralPanel();
					centralWindow.fill();
					centralWindow.changeCentralPanelContent(centralPanel, false);
					titleSelectionWindow = centralWindow.newWrapperWindow();
					titleSelectionWindow.setPositionX(300);
					titleSelectionWindow.setPositionY(100);
					FocWebApplication.getInstanceForThread().addWindow(titleSelectionWindow);
					titleSelectionWindow.setModal(true);
//					error = true;
				}
			}else{
//				error = true;
				if(WFTitleDesc.getInstance().getFocList().size()>0 && WFTitleDesc.getInstance().getFocList().size() == 1){
					popupWorkfloSiteSelectionTable(false, right);
				}else{
					popupWorkfloSiteSelectionTable(true, right);
				}
			}
		}
		return site;
	}
	
	private void popupWorkfloSiteSelectionTable(boolean hasMultipleTitles, UserCompanyRights compRight){
		Company companyToFilterOn = compRight.getCompany();
		if(companyToFilterOn != null){
			FocListWrapper wrapper = companyToFilterOn.newFocListWrapperForCurrentCompany();
	    XMLViewKey xmlViewKey = new XMLViewKey(WFSiteDesc.getInstance().getStorageName(), XMLViewKey.TYPE_TABLE,WorkflowWebModule.CTXT_SITE_SELECTION,XMLViewKey.VIEW_DEFAULT);
	    WFSite_SiteSelection_Table centralPanel = (WFSite_SiteSelection_Table) XMLViewDictionary.getInstance().newCentralPanel(getMainWindow(), xmlViewKey, wrapper);
	    centralPanel.setFocDataOwner(true);
	    centralPanel.setHasMultipleTitles(hasMultipleTitles);
	    centralPanel.setUser(compRight.getUser());
	    Window titleSelectionWindow = null;
			FocCentralPanel centralWindow = new FocCentralPanel();
			centralWindow.fill();
			centralWindow.changeCentralPanelContent(centralPanel, false);
			titleSelectionWindow = centralWindow.newWrapperWindow();
			titleSelectionWindow.setPositionX(300);
			titleSelectionWindow.setPositionY(100);
			FocWebApplication.getInstanceForThread().addWindow(titleSelectionWindow);
			titleSelectionWindow.setModal(true);
		}
	}
  
  @Override
  public void validationAfter(FVValidationLayout validationLayout, boolean commited) {
  	super.validationAfter(validationLayout, commited);
  	if(commited){
  		if(getRelatedContact() != null){
  			getRelatedContact().validate(true);
  		}
    	SaaSApplicationAdaptor adaptor = SaaSConfig.getInstance() != null ? SaaSConfig.getInstance().getSaasApplicationAdaptor() : null;
    	if(adaptor != null){
    		adaptor.adaptUserRights(getFocUser());
    	}
  	}
  }
  
  public void button_RESET_GUEST_PASSWORD_Clicked(FVButtonClickEvent event){
		FocUser user = getFocUser();
		if(user != null) {
			String  newPassword = user.resetPassword();
			Contact contact     = user.getContact();
			if(contact != null && !Utils.isStringEmpty(contact.getEMail())) {
    		FocNotificationEmailTemplate template = (FocNotificationEmailTemplate) BusinessConfig.getInstance().getEmailTemplatePasswordChange();
    		if(user != null && !user.isCreated() && template != null && !Utils.isStringEmpty(contact.getEMail())) {
    			FocDataMap focDataMap = new FocDataMap(user);
    			focDataMap.put("Contact", contact);
    			focDataMap.put("CLEAR_PASSWORD", new FocDataConstant(newPassword));
    			boolean sentSuccessfully = true;
    			try {
    		  	FocNotificationEmail email = new FocNotificationEmail(template, focDataMap);
    			  email.send();
    			  email.setCreated(true);
    			  email.validate(true);
    			} catch (Exception e) {
    				sentSuccessfully = false;
    				Globals.logException(e);
    			}
    			
    			if(sentSuccessfully) {
    				Globals.showNotification("Password changed and notification email sent to "+contact.getEMail(), "", IFocEnvironment.TYPE_WARNING_MESSAGE);
    			}
    		}

			}
		}
  }
  
	public void button_SUSPEND_USER_Clicked(FVButtonClickEvent event){
		FocUser user = getFocUser();
		if(user != null) {
			user.setSuspended(true);
			user.validate(true);
			goBack(null);
		}
	}
	
	public void button_ENABLE_USER_Clicked(FVButtonClickEvent event){
		FocUser user = getFocUser();
		if(user != null) {
			user.setSuspended(false);
			user.validate(true);
			goBack(null);
		}
	}
}
