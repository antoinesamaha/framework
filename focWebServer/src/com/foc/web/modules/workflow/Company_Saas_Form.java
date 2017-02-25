package com.foc.web.modules.workflow;

import com.foc.business.company.Company;
import com.foc.business.company.CompanyDesc;
import com.foc.list.FocList;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;

@SuppressWarnings("serial")
public class Company_Saas_Form extends FocXMLLayout {

	public Company getCompany(){
    return (Company) getFocData();
  }
  
  @Override
  public void init(com.foc.web.gui.INavigationWindow window, com.foc.web.server.xmlViewDictionary.XMLView xmlView, com.foc.shared.dataStore.IFocData focData) {
    FocList companyList = CompanyDesc.getInstance().getFocList(FocList.LOAD_IF_NEEDED);
    Company comp = (Company) companyList.getFocObject(0);
    focData = comp;
  	super.init(window, xmlView, focData);
  }
}
