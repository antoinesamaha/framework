package com.foc.web.microservice.loockups;

import com.foc.Globals;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.shared.json.B01JsonBuilder;

public class WSSingleLookup {
	private String  key        = null;
	private String  json       = null;
	private FocDesc focDesc    = null;
	private long    expiryDuration = 0;
	private long    nextExpiryTime = 0;
	private boolean scanSubList = true;
	
	public WSSingleLookup(String key, FocDesc focDesc) {
		this(key, focDesc, 0);
	}
	
	public WSSingleLookup(String key, FocDesc focDesc, boolean scanSubList) {
		this(key, focDesc, 0);
		this.scanSubList = scanSubList;
	}
	
	public WSSingleLookup(String key, FocDesc focDesc, long expiryDuration) {
		this.key = key;
		this.focDesc = focDesc;
		setExpiryDuration(expiryDuration);
	}
	
	private void dispose() {
		focDesc = null;
	}
	
	public FocDesc getFocDesc() {
		return focDesc;
	}
	
	protected boolean shouldRefresh() {
		boolean shouldRefresh = false;
		if (nextExpiryTime > 0) {
			long currentTime = System.currentTimeMillis();
			shouldRefresh = currentTime > nextExpiryTime;
			if(shouldRefresh) nextExpiryTime = currentTime + expiryDuration;
		}
		return shouldRefresh;
	}
	
	public synchronized String getJson() {
		if(json == null) {
			jsonRebuild();
		} else if (shouldRefresh()){
			refresh();
		}
		Globals.logString("json is :"  + json);
		return json;
	}
	
	public String getKey() {
		return key;
	}

	protected synchronized void replaceJson(String newJson) {
		json = newJson;
	}

	protected B01JsonBuilder newJsonBuiler() {
		B01JsonBuilder builder = new B01JsonBuilder();
		builder.setPrintForeignKeyFullObject(true);
		builder.setHideWorkflowFields(true);
		builder.setScanSubList(scanSubList);
		return builder;
	}

	public FocList getFocList(boolean load) {
		FocDesc focDesc = getFocDesc();
		FocList list = focDesc != null ? focDesc.getFocList() : null;
		if(list != null && load) {
			list.loadIfNotLoadedFromDB();
		}
		return list; 
	}

	public void fillJson(B01JsonBuilder builder) {
		FocList focList = getFocList(true); 
		if (focList != null && builder != null) {
			focList.toJson(builder);
		}
	}
	
	public void jsonRebuild() {
		try {
			B01JsonBuilder builder = newJsonBuiler();
			if(builder != null) {
				fillJson(builder);
				
				String newJson = builder.toString();
				if(newJson != null) {
					replaceJson(newJson);
				}
			}
		} catch (Exception e) {
			Globals.logException(e);
		}
	}
	
	public synchronized void refresh(){
		FocList focList = getFocList(true); 
		if (focList != null) focList.reloadFromDB();
		jsonRebuild();	
	}
	
	public synchronized void refreshObjectByReference(long ref){
		if(ref > 0) {
			FocList focList = getFocList(true); 
			if (focList != null) {
				FocObject object = focList.searchByRealReferenceOnly(ref);
				if (object != null) {
					object.reloadWithSlaveLists();
				}
			}
			jsonRebuild();
		}
	}
	
	public long getExpiryTime() {
		return expiryDuration;
	}

	public void setExpiryDuration(long expiryTime) {
		this.expiryDuration = expiryTime;
		if (this.expiryDuration > 0) {
			nextExpiryTime = System.currentTimeMillis();
		}
	}

}
