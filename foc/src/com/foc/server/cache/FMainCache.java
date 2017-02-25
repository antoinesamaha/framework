package com.foc.server.cache;

import java.util.HashMap;
import java.util.Iterator;

import com.foc.Globals;
import com.foc.desc.FocDesc;

public class FMainCache {

	private HashMap<String, FTableCache> tableToFiltersMap = null;
	
	public FMainCache(){
		tableToFiltersMap = new HashMap<String, FTableCache>();
	}
	
	public void dispose(){
		if(tableToFiltersMap != null){
			Iterator<FTableCache> iter = tableToFiltersMap.values().iterator();
			while(iter != null && iter.hasNext()){
				FTableCache tableCache = iter.next();
				if(tableCache != null){
					tableCache.dispose();
					tableCache = null;
				}
			}
			tableToFiltersMap.clear();
			tableToFiltersMap = null;
		}
	}
	
	public FTableCache pushTableCache(String storageName){
		FTableCache tableCache = tableToFiltersMap.get(storageName);
		if(tableCache == null){
			FocDesc focDesc = Globals.getApp().getFocDescByName(storageName);
			tableCache = new FTableCache(focDesc);
			tableToFiltersMap.put(storageName, tableCache);
		}
		return tableCache;
	}
	
	public void getList(StringBuffer stringBuffer, String storageName, String filter){
		FTableCache tableCache = pushTableCache(storageName);
		if(tableCache != null){
			FListCache listCache = tableCache.pushListCache(filter);
			if(listCache != null){
				
			}
		}
	}
}
