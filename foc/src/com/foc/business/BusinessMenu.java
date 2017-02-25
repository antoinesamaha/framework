package com.foc.business;

import com.foc.business.company.CompanyDesc;
import com.foc.business.printing.PrnContextDesc;
import com.foc.menu.FMenuAction;
import com.foc.menu.FMenuItem;
import com.foc.menu.FMenuList;

public class BusinessMenu {
  public static FMenuItem addCompanyMenu(FMenuList menuList){
    FMenuItem companyMenu = new FMenuItem("Company", 'C', "COMPANY", new FMenuAction(CompanyDesc.getInstance(), true));
    menuList.addMenu(companyMenu);
    return companyMenu;
  }
  
  public static FMenuItem addReportLayoutMenu(FMenuList menuList){
    FMenuItem companyMenu = new FMenuItem("Report Layout", 'L', "REPORT_LAYOUTS", new FMenuAction(PrnContextDesc.getInstance(), true));
    menuList.addMenu(companyMenu);
    return companyMenu;
  }
}
