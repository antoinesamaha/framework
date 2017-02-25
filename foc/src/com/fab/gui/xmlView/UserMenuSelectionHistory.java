package com.fab.gui.xmlView;

import com.foc.admin.FocUser;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;

public class UserMenuSelectionHistory extends FocObject{

  public UserMenuSelectionHistory(FocConstructor constr) {
    super(constr);
    newFocProperties();
  }
  
  public void setUser(FocUser user){
    setPropertyObject(UserMenuSelectionHistoryDesc.FLD_USER, user);
  }
  
  public FocUser getUser(){
    return (FocUser) getPropertyObject(UserMenuSelectionHistoryDesc.FLD_USER);
  }
  
  public void setMenuCode(String menuCode){
    setPropertyString(UserMenuSelectionHistoryDesc.FLD_MENU_CODE, menuCode);
  }
  
  public String getMenuCode(){
    return getPropertyString(UserMenuSelectionHistoryDesc.FLD_MENU_CODE);
  }
  
  public void setMenuOrder(int menuOrder){
    setPropertyInteger(UserMenuSelectionHistoryDesc.FLD_MENU_ORDER, menuOrder);
  }
  
  public int getMenuOrder(){
    return getPropertyInteger(UserMenuSelectionHistoryDesc.FLD_MENU_ORDER);
  }
}