package com.foc.web.modules.workflow;

import com.foc.admin.FocUser;
import com.foc.business.company.Company;
import com.foc.business.company.UserCompanyRights;
import com.foc.desc.FocObject;
import com.foc.event.FocEvent;
import com.foc.event.FocListener;
import com.foc.list.FocList;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;

@SuppressWarnings("serial")
public class Company_Form extends FocXMLLayout {

	public Company getCompany(){
    return (Company) getFocData();
  }
  
  @Override
  public void init(com.foc.web.gui.INavigationWindow window, com.foc.web.server.xmlViewDictionary.XMLView xmlView, com.foc.shared.dataStore.IFocData focData) {
    super.init(window, xmlView, focData);
  };

}
