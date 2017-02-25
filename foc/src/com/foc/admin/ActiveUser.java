// EXTERNAL PROPERTIES
// INSTANCE
//    MAIN
//    PANEL
// LIST
// DESCRIPTION

/*
 * Created on 20-May-2005
 */
package com.foc.admin;

import java.sql.Date;

import com.foc.business.company.Company;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;

/**
 * @author 01Barmaja
 */
@SuppressWarnings("serial")
public class ActiveUser extends FocObject{
  
  public static final int VIEW_READ_ONLY = 2;  
  
  
  public ActiveUser(FocConstructor constr) {
    super(constr);
    newFocProperties();
  }
  
  public void dispose(){
    super.dispose();
  }
  
  public FocUser getUser(){
    return (FocUser) getPropertyObject(ActiveUserDesc.FLD_USER);
  }
  
  public void setUser(FocUser user){
  	setPropertyObject(ActiveUserDesc.FLD_USER, user);
  }
  
  public Company getUserCompany(){
    return (Company) getPropertyObject(ActiveUserDesc.FLD_COMPANY);
  }
  
  public void setUserCompany(Company company){
  	setPropertyObject(ActiveUserDesc.FLD_COMPANY, company);
  }
  
  public Date getLastHeartBeat(){
    return (Date) getPropertyDate(ActiveUserDesc.FLD_LAST_HEART_BEAT);
  }
  
  public void setLastHeartBeat(Date date){
  	setPropertyDate(ActiveUserDesc.FLD_LAST_HEART_BEAT, date);
  }

  public static FocDesc getFocDesc() {
    return FocGroupDesc.getInstance();
  }
}
