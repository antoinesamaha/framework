package com.foc.admin;

import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;

@SuppressWarnings("serial")
public class GroupXMLView extends FocObject{
	
  public GroupXMLView(FocConstructor constr) {
    super(constr);
    newFocProperties();
  }
    
  public FocGroup getGroup(){
    return (FocGroup) getPropertyObject(GroupXMLViewDesc.FLD_GROUP);
  }
  
  public void setGroup(FocGroup group){
    setPropertyObject(GroupXMLViewDesc.FLD_GROUP, group);
  }
  
  public int getRight(){
    return getPropertyMultiChoice(GroupXMLViewDesc.FLD_VIEW_RIGHT);
  }

  public void setRight(int right){
    setPropertyMultiChoice(GroupXMLViewDesc.FLD_VIEW_RIGHT, right);
  }
  
  public void setStorageName(String storageName){
    setPropertyString(GroupXMLViewDesc.FLD_STORAGE_NAME, storageName);
  }
  
  public String getStorageName(){
    return getPropertyString(GroupXMLViewDesc.FLD_STORAGE_NAME);
  }
  
  public void setType(int type){
    setPropertyInteger(GroupXMLViewDesc.FLD_TYPE, type);
  }
  
  public int getType(){
    return getPropertyInteger(GroupXMLViewDesc.FLD_TYPE);
  }
  
  public void setView(String userView){
    setPropertyString(GroupXMLViewDesc.FLD_VIEW, userView);
  }
  
  public String getView(){
    return getPropertyString(GroupXMLViewDesc.FLD_VIEW);
  }
  
  public void setContext(String xmlContexr){
    setPropertyString(GroupXMLViewDesc.FLD_CONTEXT, xmlContexr);
  }
  
  public String getContext(){
    return getPropertyString(GroupXMLViewDesc.FLD_CONTEXT);
  }
  
  public void setXMLViewsKey(String storageName, int type, String xmlContext){
    setStorageName(storageName);
    setType(type);
    setContext(xmlContext);
  }
}
