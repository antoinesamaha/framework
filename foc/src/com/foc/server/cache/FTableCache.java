package com.foc.server.cache;

import java.util.HashMap;
import java.util.Iterator;

import com.foc.Globals;
import com.foc.desc.FocDesc;

public class FTableCache {
	
	private FocDesc                     focDesc        = null;
	private HashMap<String, FListCache> filter2ListMap = null;
	
	public FTableCache(FocDesc focDesc){
		this.focDesc = focDesc;
		filter2ListMap = new HashMap<String, FListCache>();
	}
	
	public void dispose(){
		if(filter2ListMap != null){
			Iterator<FListCache> iter = filter2ListMap.values().iterator();
			while(iter != null && iter.hasNext()){
				FListCache listCache = iter.next();
				if(listCache != null){
					listCache.dispose();
					listCache = null;
				}
			}
			filter2ListMap.clear();
			filter2ListMap = null;
		}
		focDesc = null;
	}
	
	public FListCache pushListCache(String filter){
		FListCache tableCache = filter2ListMap.get(filter);
		if(tableCache == null){
			tableCache = new FListCache(filter);
			filter2ListMap.put(filter, tableCache);
		}
		return tableCache;
	}
	
}
