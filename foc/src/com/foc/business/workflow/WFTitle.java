package com.foc.business.workflow;

import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;

@SuppressWarnings("serial")
public class WFTitle extends FocObject {
	
	public static final String TITLE_SUPERUSER = "Super User"; 
	public static final String TITLE_GM        = "GM";
	
	public WFTitle(FocConstructor constr){
		super(constr);
		newFocProperties();
	}
	
	public boolean isProjectSpecific(){
		return getPropertyBoolean(WFTitleDesc.FLD_IS_PROJECT_SPECIFIC);
	}
	
	public void setProjectSpecific(boolean sys){
		setPropertyBoolean(WFTitleDesc.FLD_IS_PROJECT_SPECIFIC, sys);
	}
	
	public String getUserDataPathFromProjWBS(){
		return getPropertyString(WFTitleDesc.FLD_USER_DATAPATH_FROM_PROJ_WBS);
	}
	
	public void setUserDataPathFromProjWBS(String path){
		setPropertyString(WFTitleDesc.FLD_USER_DATAPATH_FROM_PROJ_WBS, path);
	}
}
