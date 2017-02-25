package com.foc.server.cache;

import com.foc.list.FocList;

public class FListCache {
	
	private String  filter = null;
	private FocList list   = null;
	
	public FListCache(String filter){
		this.filter = filter;
	}
	
	public void dispose(){
		filter = null;
		if(list != null){
			list.dispose();
			list = null;
		}
	}
}
