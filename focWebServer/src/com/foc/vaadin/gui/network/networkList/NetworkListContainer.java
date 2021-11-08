package com.foc.vaadin.gui.network.networkList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import org.vaadin.visjs.networkDiagram.Node;

import com.foc.vaadin.gui.network.FocNetwork;
import com.foc.vaadin.gui.network.INetworkItem;

public class NetworkListContainer {

	private HashMap<String, NetworkList> networkListMap_Previous = null;
	private HashMap<String, NetworkList> networkListMap_Next = null;	
	
	private FocNetwork network = null;
	
	public NetworkListContainer(FocNetwork network) {
		this.network = network;
	}
	
	public void dispose_Map(HashMap<String, NetworkList> map) {
		if(map != null) {
			Iterator<NetworkList> iter = map.values().iterator();
			while (iter != null && iter.hasNext()) {
				NetworkList netList = iter.next();
				if(netList != null) netList.dispose();
			}
			map.clear();
			map = null;
		}
	}
	
	public void dispose_PreviousMap(){
		dispose_Map(networkListMap_Previous);
		networkListMap_Previous = null;
	}
	
	public void dispose_NextMap(){
		dispose_Map(networkListMap_Next);
		networkListMap_Next = null;
	}
	
	public void dispose(){
		dispose_PreviousMap();
		dispose_NextMap();
		network = null;
	}
	
	public FocNetwork getNetwork() {
		return network;
	}
	
	public void orderList(String listType, Node node, INetworkItem networkItem, int level) {
		if(networkListMap_Next == null) {
			networkListMap_Next = new HashMap<String, NetworkList>();
		}
		
		NetworkList netList = networkListMap_Next.get(listType);
		if (netList == null) {
			netList = getNetwork().newNetworkList(listType);
			if(netList != null) networkListMap_Next.put(listType, netList);
		}
	
		if (netList != null) {
			netList.addOrderer(node, networkItem, level);
		}
	}
	
	public boolean hasOrderedLists() {
		boolean hasOrdered = networkListMap_Next != null;
		if(hasOrdered) {
			Set<String> setKeys = networkListMap_Next.keySet();
			hasOrdered = setKeys != null && !setKeys.isEmpty(); 
		}
		return hasOrdered; 
	}
	
	public void loadLists() {
		if(networkListMap_Next != null) {
			Iterator<String> iter = networkListMap_Next.keySet().iterator();
			while (iter != null && iter.hasNext()) {
				String key = iter.next();
				NetworkList list = networkListMap_Next.get(key);
				if (list != null) {
					list.load();
				}
			}
		}
	}
	
	public void distributeLists() {
		dispose_PreviousMap();
		networkListMap_Previous = networkListMap_Next;
		networkListMap_Next = null;

		if(networkListMap_Previous != null) {
			Iterator<String> iter = networkListMap_Previous.keySet().iterator();
			while (iter != null && iter.hasNext()) {
				String key = iter.next();
				NetworkList list = networkListMap_Previous.get(key);
				if (list != null) {
					list.distribute();
				}
			}
		}
		
	}
}
