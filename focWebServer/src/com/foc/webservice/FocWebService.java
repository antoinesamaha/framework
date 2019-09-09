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
package com.foc.webservice;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Iterator;

import com.foc.Globals;
import com.foc.IFocDescDeclaration;
import com.foc.admin.FocGroup;
import com.foc.admin.FocGroupDesc;
import com.foc.admin.FocUser;
import com.foc.admin.FocUserDesc;
import com.foc.admin.GrpWebModuleRights;
import com.foc.admin.GrpWebModuleRightsDesc;
import com.foc.business.company.Company;
import com.foc.business.company.CompanyDesc;
import com.foc.business.company.UserCompanyRightsDesc;
import com.foc.business.config.BusinessConfig;
import com.foc.business.currency.Currencies;
import com.foc.business.currency.Currency;
import com.foc.business.currency.CurrencyDesc;
import com.foc.business.notifier.FocNotificationEmailTemplateDesc;
import com.foc.business.workflow.WFSite;
import com.foc.business.workflow.WFSiteDesc;
import com.foc.business.workflow.WFTitle;
import com.foc.business.workflow.WFTitleDesc;
import com.foc.business.workflow.rights.RightLevel;
import com.foc.business.workflow.rights.RightLevelDesc;
import com.foc.desc.AutoPopulatable;
import com.foc.desc.FocDesc;
import com.foc.list.FocList;
import com.foc.property.FDate;
import com.foc.saas.manager.SaaSConfig;
import com.foc.vaadin.FocWebApplication;
import com.foc.vaadin.IApplicationConfigurator;
import com.foc.web.modules.admin.AdminWebModule;
import com.foc.web.server.FocWebServer;

public class FocWebService{
	public void setCompanyName(String name){
		Globals.getApp().getCurrentCompany().setName(name);
	}
	
	public FwsGroup findGroup(String name){
		FocList list = FocGroupDesc.getInstance().getFocList(FocList.LOAD_IF_NEEDED);
		FocGroup group = (FocGroup) list.searchByPropertyStringValue(FocGroupDesc.FLD_NAME, name);
		FwsGroup wsGroup = new FwsGroup();
		if(group != null){
			wsGroup.setGroup(group);
			wsGroup.setRef(group.getReference().getInteger());
		}
		return wsGroup;
	}
	
	public void createUserAndInsertIntoGroup(String username, String password, String groupName){
		FwsGroup fwsGroup = findGroup(groupName);
		if(fwsGroup != null){
			FocGroup group = fwsGroup.getGroup();
			FocUser user = FocUser.createIfNotExist(username, password, group);
			user.validate(true);
		}
	}
	
	public void initAcount(String companyName, String userName, String password, String currency, String appType, String plan, String userRole, String renewedUntil){
		initAcount(null, companyName, userName, password, currency, appType, plan, userRole, renewedUntil);
	}
	
	public void initAcount(FocWebApplication focWebApplication, String companyName, String userName, String password, String currency, String appType, String plan, String userRole, String renewedUntil){
		SaaSConfig saasConfig = Globals.getApp().loadAppConfiguration();
		if(saasConfig != null){
			if(appType != null){
				saasConfig.setApplicationType(appType);
				if(plan != null){
					saasConfig.setPlan(plan);
				}
				DateFormat sdf = FDate.getDateFormat();
				java.util.Date date = null;
				if(renewedUntil != null){
					try{
						date = sdf.parse(renewedUntil);
					}catch (ParseException e){
						Globals.logExceptionWithoutPopup(e);
					}
					if(date != null){
						saasConfig.setRenewedUntilDate(new Date(date.getTime()));
					}
				}
				saasConfig.validate(true);
				saasConfig.adaptUserRights();
			}
		}
		
		Globals.logString("Configuring company: "+companyName);
		Company currentCompany = configureEmptyCompany(companyName);
		Globals.logString("                   -- OK");
		
		initBusinessConfig();
		
		FocGroup group = null;
		
		FocUser newUser = getEmptyUser();
		if(newUser != null){
			newUser.setName(userName);
			newUser.setPassword(password);
			if(userRole != null){
				newUser.setSaasApplicationRole(userRole);
				saasConfig.adaptUserRights(newUser);
			}else{
				group = FocGroupDesc.getInstance().findGroupByName(FocGroup.STANDARD);
				newUser.setGroup(group);				
			}
		}
		
		WFTitle title = createTitle(WFTitle.TITLE_GM);
		Globals.logString("after creating the title");
		WFSite site = currentCompany.findOrCreateSite(WFSite.DEFAULT_SITE_NAME);
		site.findOrAddOperator(title, newUser);
		
		Globals.logString("after configuring the site");
		RightLevel rightLevel = RightLevelDesc.getInstance().findOrAddRightLevel(RightLevelDesc.ALL_RIGHTS);
		site.findOrAddUserTransactionRight(title, null, "", rightLevel);
		site.validate(true);
		Globals.logString("after validating site");
		FocList siteList = WFSiteDesc.getInstance().getFocList(FocList.LOAD_IF_NEEDED);
		siteList.validate(true);
		
		newUser.addCompanyRights(currentCompany, UserCompanyRightsDesc.ACCESS_RIGHT_READ_WRITE);
		currentCompany.validate(true);
		
		newUser.setCurrentCompany(currentCompany);
		newUser.setCurrentSite(site);
		newUser.setCurrentTitle(title);
		newUser.validate(true);
		
		configureWebModuleRights(group);
		launchAutoPopulate();
		Currency currObject = CurrencyDesc.getInstance().makeSureCurrencyExist(currency, currency, false);
		Currencies.getCurrencies().setBaseCurrency(currObject);
		Currencies.getCurrencies().setDefaultViewCurrency(currObject);
		Currencies.getCurrencies().validate(true);
		
		//If the current company is not set this will fail creating CostBreakdown and FinanceConfig...
		if(Globals.getApp().getCurrentCompany() != null){
			launchGeneralLedgerConfiguration();
		}
		
		if(saasConfig != null){
			saasConfig.adaptUserRights();
		}
	}
	
	private void launchGeneralLedgerConfiguration() {
		for(int i=0; i<FocWebServer.getInstance().applicationConfigurators_Size(); i++){
	    IApplicationConfigurator config = FocWebServer.getInstance().applicationConfigurators_Get(i);
	    if(config.getCode().equals("LEBANESE_COMPANY_PRE_CONFIGURATION")){
	    	config.run();
	    }
	  }
	}

	public void configureWebModuleRights(FocGroup group){
		if(group != null){
			GrpWebModuleRights webModuleRight = group.getWebModuleRightsObject(AdminWebModule.MODULE_NAME);
			if(webModuleRight != null){
				webModuleRight.setRight(GrpWebModuleRightsDesc.ACCESS_FULL);
				webModuleRight.validate(true);
			}
			webModuleRight = group.getWebModuleRightsObject("SITES_AND_WORKFLOW");
			if(webModuleRight != null){
				webModuleRight.setRight(GrpWebModuleRightsDesc.ACCESS_FULL);
				webModuleRight.validate(true);
			}
			webModuleRight = group.getWebModuleRightsObject("FINANCE");
			if(webModuleRight != null){
				webModuleRight.setRight(GrpWebModuleRightsDesc.ACCESS_FULL);
				webModuleRight.validate(true);
			}
			webModuleRight = group.getWebModuleRightsObject("INVENTORY");
			if(webModuleRight != null){
				webModuleRight.setRight(GrpWebModuleRightsDesc.ACCESS_FULL);
				webModuleRight.validate(true);
			}
			group.getWebModuleRightsList().validate(true);
			group.validate(true);
			FocList listOfGroups = FocGroup.getList(FocList.LOAD_IF_NEEDED);
			listOfGroups.validate(true);
		}
	}
	
	public void launchAutoPopulate(){
		Iterator<IFocDescDeclaration> focDescDeclarationIterator = Globals.getApp().getFocDescDeclarationIterator();
    while(focDescDeclarationIterator != null && focDescDeclarationIterator.hasNext()){
    	IFocDescDeclaration focDescDeclaration = focDescDeclarationIterator.next();
    	if(focDescDeclaration != null){
    		FocDesc focDesc = focDescDeclaration.getFocDescription();
    		if(focDesc instanceof AutoPopulatable){
    			AutoPopulatable autoPopulatable = (AutoPopulatable) focDesc;
    			if(autoPopulatable != null){
    				autoPopulatable.populate();
    			}
    		}
    	}
    }
	}

	public Company configureEmptyCompany(String companyName){
		Company currentCompany = null;
		FocList focList = CompanyDesc.getInstance().getFocList();
		focList.loadIfNotLoadedFromDB();
		if(focList.size() == 1){
			currentCompany = (Company) focList.getFocObject(0);
		  if(currentCompany != null){
				currentCompany.setDescription(companyName);
				currentCompany.setName(companyName);
				currentCompany.validate(true);
		  }
		}
		return currentCompany;
	}
	
  public WFTitle createTitle(String titleName){
  	WFTitle title = (WFTitle) WFTitleDesc.getInstance().getFocList().newEmptyItem();
		title.setName(titleName);
		title.validate(true);
		return title;
  }
  
  public FocUser getEmptyUser(){
  	FocUser newUser = (FocUser) FocUserDesc.getInstance().getFocList().iFocList_searchByPropertyValue(FocUserDesc.FLDNAME_NAME, FocUser.EMPTY_USER);
  	return newUser;
  }
  
  public void initBusinessConfig(){
		FocList focList = CompanyDesc.getInstance().getFocList();
		focList.loadIfNotLoadedFromDB();
		for(int i=0; i<focList.size(); i++){
			Company currentCompany = (Company) focList.getFocObject(i);
		  if(currentCompany != null){
		  	BusinessConfig busConf = BusinessConfig.getOrCreateForCompany(currentCompany);
		  	if(busConf != null){
		  		busConf.setPartyCodePrefix("P{YY}-");
		  		busConf.setPartyNbrDigits(3);
		  		busConf.setGeneralEmailTemplate(FocNotificationEmailTemplateDesc.getFocNotificationEmailTemplateByName(FocNotificationEmailTemplateDesc.DEFAULT_PDF_PRINTOUT_SENDING));
		  		busConf.validate(true);
		  	}
		  }
		}
  }
}
