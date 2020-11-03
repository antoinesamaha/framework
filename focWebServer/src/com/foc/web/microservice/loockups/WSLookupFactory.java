package com.foc.web.microservice.loockups;

import java.util.HashMap;

import com.foc.admin.FocGroupDesc;
import com.foc.admin.FocUserDesc;
import com.foc.business.workflow.WFTitleDesc;

public class WSLookupFactory {

	private HashMap<String, WSSingleLookup> map = null;

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
		}
	}

	public WSSingleLookup getLookup(String key) {
		return map != null ? map.get(key) : null;
	}

	private static WSLookupFactory instance = null;

	public static synchronized WSLookupFactory getInstance() {
		if (instance == null) {
			instance = new WSLookupFactory();
		}
		return instance;
	}
}
