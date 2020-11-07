package com.foc.web.microservice.loockups;

import java.util.ArrayList;
import java.util.HashMap;

import com.foc.admin.FocGroupDesc;
import com.foc.admin.FocUserDesc;
import com.foc.business.workflow.WFTitleDesc;
import com.foc.desc.FocDesc;
import com.foc.util.Utils;

public class WSLookupFactory {

	private HashMap<String, WSSingleLookup> map = null;
	private HashMap<String, ArrayList<WSSingleLookup>> mapByFocDesc = null;

	private WSLookupFactory() {
		map = new HashMap<String, WSSingleLookup>();

		putLookup(new WSSingleLookup("wf_title", WFTitleDesc.getInstance()));
		putLookup(new WSSingleLookup("fgroup", FocGroupDesc.getInstance()));
		putLookup(new WSSingleLookup("fuser", FocUserDesc.getInstance()));
	}

	public void putLookup(WSSingleLookup lookup) {
		if(lookup != null) {
			if(map == null) map = new HashMap<String, WSSingleLookup>();
			map.put(lookup.getKey(), lookup);
			
			if (lookup.getFocDesc() != null) {
				String tableName = lookup.getFocDesc().getStorageName();
				
				if (!Utils.isStringEmpty(tableName)) {
					if(mapByFocDesc == null) {
						mapByFocDesc = new HashMap<String, ArrayList<WSSingleLookup>>();
					}
					
					ArrayList<WSSingleLookup> arrayList = mapByFocDesc.get(tableName);
					if(arrayList == null) {
						arrayList = new ArrayList<WSSingleLookup>();
						mapByFocDesc.put(tableName, arrayList);
					}
					
					arrayList.add(lookup);
				}
			}
		}
	}

	public WSSingleLookup getLookup(String key) {
		return map != null ? map.get(key) : null;
	}

	public boolean refreshLookupByName(String lookupKey) {
		boolean error = true;

		WSSingleLookup lookup = map.get(lookupKey);
		if(lookup != null) {
			lookup.refresh();
			error = false;
		}

		return error;
	}
	
	public boolean refreshLookupByDesc(FocDesc focDesc) {
		boolean error = true;

		ArrayList<WSSingleLookup> arrayList = mapByFocDesc.get(focDesc.getStorageName());
		if (arrayList != null) {
			for (int i=0; i<arrayList.size(); i++) {
				WSSingleLookup lookup = arrayList.get(i);
				lookup.refresh();
				error = false;
			}
		}

		return error;
	}
	
	private static WSLookupFactory instance = null;

	public static synchronized WSLookupFactory getInstance() {
		if (instance == null) {
			instance = new WSLookupFactory();
		}
		return instance;
	}
}
