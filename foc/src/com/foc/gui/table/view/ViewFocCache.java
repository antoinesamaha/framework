package com.foc.gui.table.view;

import java.util.HashMap;

import com.foc.Globals;
import com.foc.db.SQLFilter;
import com.foc.desc.FocConstructor;
import com.foc.list.FocLinkSimple;
import com.foc.list.FocList;

public class ViewFocCache {

	private HashMap<String, ViewFocList> map                 = null;
	private FocList                      userDefaultViewList = null;
	
	private ViewFocCache(){
		map = new HashMap<String, ViewFocList>();
		
		UserView usrViewTemp = new UserView(new FocConstructor(UserViewDesc.getInstance(), null));
		usrViewTemp.setUser(Globals.getApp().getUser());
  	SQLFilter filter = new SQLFilter(usrViewTemp, SQLFilter.FILTER_ON_SELECTED);
  	filter.addSelectedField(UserViewDesc.FLD_USER);
  	userDefaultViewList = new FocList(new FocLinkSimple(UserViewDesc.getInstance()), filter);
  	userDefaultViewList.loadIfNotLoadedFromDB();			
	}

	public ViewFocList get(String contextKey, boolean create){
		ViewFocList list = map.get(contextKey);
		if(list == null && create){
			list = new ViewFocList(contextKey);
			map.put(contextKey, list);
		}
		return list;
	}
	
	public UserView getUserView(String key, String context){
		UserView userView = null;
		for(int i=0; i<userDefaultViewList.size() && userView == null; i++){
			UserView uV = (UserView) userDefaultViewList.getFocObject(i);
			if(uV != null && uV.getViewKey().equals(key) && uV.getViewContext().equals(context)){
				userView = uV;
			}
		}
  	if(userView == null){
  		userView = (UserView) userDefaultViewList.newEmptyItem();
  		userView.setUser(Globals.getApp().getUser());
  		userView.setViewKey(key);
  		userView.setViewContext(context);
  	}
		return userView;
	}
	
	private static ViewFocCache viewFocListCache = null;   
	public static ViewFocCache getInstance(){
		if(viewFocListCache == null){
			viewFocListCache = new ViewFocCache(); 
		}
		return viewFocListCache;
	}
}
