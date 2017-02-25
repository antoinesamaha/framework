package com.foc.business.workflow;

import com.foc.admin.FocUser;
import com.foc.business.department.Department;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;

@SuppressWarnings("serial")
public class WFOperator extends FocObject {

	private boolean duplicatedForUserArrayOfTitles = false;
	
	public WFOperator(){
		this(new FocConstructor(WFOperatorDesc.getInstance(), null));
	}
	
	public WFOperator(FocConstructor constr){
		super(constr);
		newFocProperties();
	}
	
	public FocUser getUser(){
		return (FocUser) getPropertyObject(WFOperatorDesc.FLD_USER);
	}

	public void setUser(FocUser title){
		setPropertyObject(WFOperatorDesc.FLD_USER, title);
	}
	
	public WFTitle getTitle(){
		return (WFTitle) getPropertyObject(WFOperatorDesc.FLD_TITLE);
	}
	
	public void setTitle(WFTitle title){
		setPropertyObject(WFOperatorDesc.FLD_TITLE, title);
	}
	
	public Department getDepartment(){
		return (Department) getPropertyObject(WFOperatorDesc.FLD_DEPARTMENT);
	}

	public WFSite getArea(){
		return (WFSite) getPropertyObject(WFOperatorDesc.FLD_AREA);
	}

	public void setArea(WFSite area){
		setPropertyObject(WFOperatorDesc.FLD_AREA, area);
	}
	
	public void resetUserOperatorArray(){
		if(!isDuplicatedForUserArrayOfTitles()){
			FocUser user = getUser();
			if(user != null){
				user.dispose_OperatorsArrayForThisUserAllAreas();
			}
		}
	}

	public boolean isDuplicatedForUserArrayOfTitles() {
		return duplicatedForUserArrayOfTitles;
	}

	public void setDuplicatedForUserArrayOfTitles(boolean duplicatedForUserArrayOfTitles) {
		this.duplicatedForUserArrayOfTitles = duplicatedForUserArrayOfTitles;
	}
}
