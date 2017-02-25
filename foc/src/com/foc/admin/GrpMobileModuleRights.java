package com.foc.admin;

import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;

@SuppressWarnings("serial")
public class GrpMobileModuleRights extends FocObject {

  public GrpMobileModuleRights(FocConstructor constr) {
    super(constr);
    newFocProperties();
  }
  
  public FocGroup getGroup(){
    return (FocGroup) getPropertyObject(GrpMobileModuleRightsDesc.FLD_GROUP);
  }
  
  public void setGroup(FocGroup group){
    setPropertyObject(GrpMobileModuleRightsDesc.FLD_GROUP, group);
  }
   
  public String getModuleName(){
    return getPropertyString(GrpMobileModuleRightsDesc.FLD_MODULE_NAME);
  }

  public void setModuleName(String key){
    setPropertyString(GrpMobileModuleRightsDesc.FLD_MODULE_NAME, key);
  }

  public String getModuleTitle(){
    return getPropertyString(GrpMobileModuleRightsDesc.FLD_MODULE_TITLE);
  }

  public void setModuleTitle(String key){
    setPropertyString(GrpMobileModuleRightsDesc.FLD_MODULE_TITLE, key);
  }

  public int getRight(){
    return getPropertyMultiChoice(GrpMobileModuleRightsDesc.FLD_ACCESS_RIGHT);
  }
  
  public void setRight(int right){
    setPropertyMultiChoice(GrpMobileModuleRightsDesc.FLD_ACCESS_RIGHT, right);
  }
  
  public boolean hasAccessRight(String moduleName){
		boolean access = false;
		if(getModuleName() != null && getModuleName().equals(moduleName) && getRight() == GrpMobileModuleRightsDesc.ACCESS_FULL){
			access = true;
		}
		return access;
	}
}
