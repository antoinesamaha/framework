package com.foc.admin;

import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;

@SuppressWarnings("serial")
public class DocRightsGroupUsers extends FocObject{
  
  public DocRightsGroupUsers(FocConstructor constr) {
    super(constr);
    newFocProperties();
  }
  
  public void dispose(){
    super.dispose();
  }
  
  public DocRightsGroup getDocRightsGroup(){
  	return (DocRightsGroup) getPropertyObject(DocRightsGroupUsersDesc.FLD_DOC_RIGHTS_GROUPS);
  }
	
	public void setDocRightsGroup(DocRightsGroup docRightsGroup){
  	setPropertyObject(DocRightsGroupUsersDesc.FLD_DOC_RIGHTS_GROUPS, docRightsGroup);
  }
	
	public FocUser getUser(){
  	return (FocUser) getPropertyObject(DocRightsGroupUsersDesc.FLD_USER);
  }
	
	public void setUser(FocUser user){
  	setPropertyObject(DocRightsGroupUsersDesc.FLD_USER, user);
  }
}
