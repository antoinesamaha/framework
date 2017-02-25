package com.foc.webservice;

public class FWS {
	public void setCompanyName(String name){
//		Globals.getApp().getCurrentCompany().setName(name);
	}

	public int multiply(int a, int b){
		return a*b;
	}
	
	/*
	public FwsGroup findGroup(String name){
		FocList list = FocGroupDesc.getInstance().getFocList(FocList.LOAD_IF_NEEDED);
		FocGroup group = (FocGroup) list.searchByPropertyStringValue(FocGroupDesc.FLD_NAME, name);
		
		FwsGroup wsGroup = new FwsGroup();
//		wsGroup.setGroup(group);
		wsGroup.setRef(group.getReference().getInteger());
		return wsGroup;
	}
	*/
	
	public int createUser(String username, String password, String groupCode){
		return 0;
	}

}
