package com.fab.gui.xmlView;

import com.foc.admin.FocUser;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;

@SuppressWarnings("serial")
public class UserXMLView extends FocObject{

  public UserXMLView(FocConstructor constr) {
    super(constr);
    newFocProperties();
  }
  
  public void setStorageName(String storageName){
    setPropertyString(UserXMLViewDesc.FLD_STORAGE_NAME, storageName);
  }
  
  public String getStorageName(){
    return getPropertyString(UserXMLViewDesc.FLD_STORAGE_NAME);
  }
  
  public void setView(String userView){
    setPropertyString(UserXMLViewDesc.FLD_VIEW, userView);
  }
  
  public String getView(){
    return getPropertyString(UserXMLViewDesc.FLD_VIEW);
  }
  
  public void setPrintingView(String printingView){
    setPropertyString(UserXMLViewDesc.FLD_PRINTING_VIEW, printingView);
  }
  
  public String getPrintingView(){
    return getPropertyString(UserXMLViewDesc.FLD_PRINTING_VIEW);
  }
  
  public FocUser getUser(){
    return (FocUser) getPropertyObject(UserXMLViewDesc.FLD_USER);
  }

  public void setUser(FocUser user){
    setPropertyObject(UserXMLViewDesc.FLD_USER, user);
  }
  
  public void setContext(String xmlContext){
    setPropertyString(UserXMLViewDesc.FLD_CONTEXT, xmlContext);
  }
  
  public String getContext(){
    return getPropertyString(UserXMLViewDesc.FLD_CONTEXT);
  }
  
  public void setType(int type){
    setPropertyInteger(UserXMLViewDesc.FLD_TYPE, type);
  }
  
  public int getType(){
    return getPropertyInteger(UserXMLViewDesc.FLD_TYPE);
  }
}