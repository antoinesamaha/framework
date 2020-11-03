package com.foc.web.microservice.loockups;

import java.util.ArrayList;

import com.foc.Globals;
import com.foc.desc.FocDesc;
import com.foc.list.FocList;
import com.foc.shared.json.B01JsonBuilder;

public class WSSingleLookup {
	private String  key        = null;
	private String  json       = null;
	private FocDesc focDesc    = null;
	private long    expiryDuration = 0;
	private long    nextExpiryTime = 0;
	private ArrayList<String> relatedLookups = null;
	
	public WSSingleLookup(String key, FocDesc focDesc) {
		this(key, focDesc, 0);
	}

	public WSSingleLookup(String key, FocDesc focDesc, ArrayList<String> relatedLookups) {
		this(key, focDesc, 0);
		setRelatedLookups(relatedLookups);
	}
	
	public WSSingleLookup(String key, FocDesc focDesc, long expiryDuration, ArrayList<String> relatedLookups) {
		this.key = key;
		this.focDesc = focDesc;
		setExpiryDuration(expiryDuration);
		setRelatedLookups(relatedLookups);
	}
	
	public WSSingleLookup(String key, FocDesc focDesc, long expiryDuration) {
		this.key = key;
		this.focDesc = focDesc;
		setExpiryDuration(expiryDuration);
	}
	
	public ArrayList<String> getRelatedLookups() {
		return relatedLookups;
	}
	
	public void setRelatedLookups(ArrayList<String> list) {
		if(list != null) {
			if(relatedLookups == null) relatedLookups = new ArrayList<String>();
			for(int i=0; i < list.size(); i++) {
				pushRelatedLookup(list.get(i));
			}
		}
	}
	
	public void pushRelatedLookup(String name) {
		if(relatedLookups == null) relatedLookups = new ArrayList<String>();
		if(!relatedLookups.contains(name)) relatedLookups.add(name);
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
		builder.setScanSubList(true);
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
		refreshRelatedLookups();
	}
	
	public void refreshRelatedLookups() {
		if(relatedLookups != null) {
			for(int i=0; i < relatedLookups.size(); i++) {
				String lookupName = relatedLookups.get(i);
				WSLookupFactory factory = WSLookupFactory.getInstance();
				if(factory != null) {
					WSSingleLookup lookup= factory.getLookup(lookupName);
					if(lookup !=null) {
						Globals.logString("Reload Servlet : lookup found, name : "+lookupName);
						lookup.refresh();
					}
				}
			}
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
