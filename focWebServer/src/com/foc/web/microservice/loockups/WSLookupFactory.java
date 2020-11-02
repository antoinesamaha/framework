package com.foc.web.microservice.loockups;

import java.util.HashMap;

import com.foc.admin.FocGroupDesc;
import com.foc.admin.FocUserDesc;
import com.foc.business.workflow.WFTitleDesc;

public class WSLookupFactory {

	private HashMap<String, WSSingleLookup> map = null;
	private HashMap<String, WSSingleLookup> mapByFocDescName = null;

	private WSLookupFactory() {
		map = new HashMap<String, WSSingleLookup>();
		mapByFocDescName = new HashMap<String, WSSingleLookup>();

		putLookup(new WSSingleLookup("wf_title", WFTitleDesc.getInstance()));
		putLookup(new WSSingleLookup("fgroup", FocGroupDesc.getInstance()));
		putLookup(new WSSingleLookup("fuser", FocUserDesc.getInstance()));
	}

//	public void putLookup(WSSingleLookup lookup) {
//		map.put(lookup.getKey(), lookup);
//		mapByFocDescName.put(lookup.getFocDesc().getName(),lookup);
//	}
	
	public void putLookup(WSSingleLookup lookup) {
		if(lookup != null) {
			map.put(lookup.getKey(), lookup);
			if(lookup.getFocDesc() != null) mapByFocDescName.put(lookup.getFocDesc().getName(),lookup);
		}
	}

	public WSSingleLookup getLookup(String key) {
		return map != null ? map.get(key) : null;
	}
	
	public WSSingleLookup getLookupByFocDescName(String name) {
		return map != null ? mapByFocDescName.get(name) : null;
	}

	private static WSLookupFactory instance = null;

	public static synchronized WSLookupFactory getInstance() {
		if (instance == null) {
			instance = new WSLookupFactory();
		}
		return instance;
	}
}
