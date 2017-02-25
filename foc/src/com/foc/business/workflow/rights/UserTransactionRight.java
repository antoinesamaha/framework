package com.foc.business.workflow.rights;

import com.foc.admin.FocUser;
import com.foc.business.workflow.WFSite;
import com.foc.business.workflow.WFTitle;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;

@SuppressWarnings("serial")
public class UserTransactionRight extends FocObject {
	
	public UserTransactionRight(FocConstructor constr){
		super(constr);
		newFocProperties();
	}

	public void setTransactionDBTitle(String dbTitle){
		setPropertyString(UserTransactionRightDesc.FLD_TRANSACTION, dbTitle);
	}

	public String getTransactionDBTitle(){
		return getPropertyString(UserTransactionRightDesc.FLD_TRANSACTION);
	}

	public FocUser getUser(){
		return (FocUser) getPropertyObject(UserTransactionRightDesc.FLD_USER);
	}

	public void setUser(FocUser user){
		setPropertyObject(UserTransactionRightDesc.FLD_USER, user);
	}

	public WFTitle getTitle(){
		return (WFTitle) getPropertyObject(UserTransactionRightDesc.FLD_TITLE);
	}

	public void setTitle(WFTitle title){
		setPropertyObject(UserTransactionRightDesc.FLD_TITLE, title);
	}

	public WFSite getSite(){
		return (WFSite) getPropertyObject(UserTransactionRightDesc.FLD_SITE);
	}
	
	public RightLevel getWFRightsLevel(){
		return (RightLevel) getPropertyObject(UserTransactionRightDesc.FLD_RIGHTS_LEVEL);
	}
	
	public void setWFRightsLevel(RightLevel level){
		setPropertyObject(UserTransactionRightDesc.FLD_RIGHTS_LEVEL, level);
	}
}
