package com.foc.admin;

import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class DocRightsGroup extends FocObject{
  
  public DocRightsGroup(FocConstructor constr) {
    super(constr);
    newFocProperties();
  }
  
  public void dispose(){
    super.dispose();
  }
  
  public FocList getDocRightsGroupUsersList(){
		FocList list = getPropertyList(DocRightsGroupDesc.FLD_DOC_RIGHTS_GROUP_USERS_LIST);
		return list;
	}
}
