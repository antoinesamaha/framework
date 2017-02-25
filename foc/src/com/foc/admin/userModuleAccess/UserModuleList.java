package com.foc.admin.userModuleAccess;

import com.foc.admin.FocGroup;
import com.foc.admin.FocUser;
import com.foc.admin.FocUserDesc;
import com.foc.admin.GrpWebModuleRights;
import com.foc.admin.GrpWebModuleRightsDesc;
import com.foc.list.FocLinkSimple;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class UserModuleList extends FocList {

	public UserModuleList() {
		super(new FocLinkSimple(UserModuleAccessDesc.getInstance()));
		setDbResident(false);
	}
	
	public void dispose(){
		super.dispose();
	}
	
	public void fill(){
		FocList userList = FocUserDesc.getList();
		for(int i=0; i<userList.size(); i++){
			FocUser user = (FocUser) userList.getFocObject(i);
			if(!user.isSuspended() && !user.getName().equals("01BARMAJA")){
				FocGroup group = user.getGroup();
				FocList webModuleList = group.getWebModuleRightsList();
				for(int m=0; m<webModuleList.size(); m++){
					GrpWebModuleRights moduleRight = (GrpWebModuleRights) webModuleList.getFocObject(m);
					if(moduleRight != null && !moduleRight.isAdminModule() && moduleRight.getRight() == GrpWebModuleRightsDesc.ACCESS_FULL && !moduleRight.getModuleTitle().isEmpty()){
						UserModuleAccess uma = (UserModuleAccess) newEmptyItem();
						uma.setUser(user);
						uma.setGroup(group);
						uma.setModule(moduleRight.getModuleName());
						add(uma);
					}
				}
			}
		}
	}
}
