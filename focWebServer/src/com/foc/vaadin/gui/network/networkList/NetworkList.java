package com.foc.vaadin.gui.network.networkList;

import java.util.HashMap;
import java.util.Iterator;

import com.foc.list.FocList;
import com.foc.vaadin.gui.network.FocNetwork;
import com.foc.vaadin.gui.network.INetworkItem;
import org.vaadin.visjs.networkDiagram.Node;

public abstract class NetworkList {
	public abstract void load(); 
	public abstract void pickup();
	
	protected FocList list = null;
	
	private FocNetwork network = null;
	private HashMap<Long, NetworkListOrderer> orderers = null;

	public NetworkList(FocNetwork network) {
		this.network = network;
		orderers = new HashMap<Long, NetworkListOrderer>();
	}
	
	public void dispose_List() {
		if (list != null) {
			list.dispose();
			list = null;
		}		
	}
	
	public void dispose() {
		dispose_List();
		if (orderers != null) {
			Iterator<NetworkListOrderer> iter = orderers.values().iterator(); 
			while (iter != null && iter.hasNext()) {
				NetworkListOrderer orderer = iter.next();
				if (orderer != null) {
					orderer.dispose();
					orderer = null;
				}
			}
			orderers.clear();
			orderers = null;
		}
		network = null;
	}
	
	public String buildWhereRefList() {
		StringBuffer buffer = new StringBuffer(); // adapt_proofread
		Iterator<NetworkListOrderer> iter = orderers.values().iterator(); 
		while (iter != null && iter.hasNext()) {
			NetworkListOrderer orderer = iter.next();
			if (orderer != null) {
				long ref = orderer.getRef();
				if (ref > 0) {
					if(buffer.length() > 0) buffer.append(",");
					buffer.append(ref);
				}
			}
		}
		return buffer.toString();
	}
	
	public void addOrderer(Node node, INetworkItem item, int level) {
		NetworkListOrderer orderer = new NetworkListOrderer(node, item, level);
		orderers.put(orderer.getRef(), orderer);
	}
	
	public void distribute() {
		pickup();
		
//		if(orderers != null) {
//			Iterator<NetworkListOrderer> iter = orderers.values().iterator(); 
//			while (iter != null && iter.hasNext()) {
//				NetworkListOrderer orderer = iter.next();
//				if (orderer != null) {
//					INetworkItem item = orderer.getItem();
//					int level = orderer.getLevel();
//					
//					pickup(item, level);
//				}
//			}
//		}
	}
	
	public FocNetwork getNetwork() {
		return network;
	}
	
	public FocList getFocList() {
		return list;
	}
	
	public Iterator<NetworkListOrderer> getOrderersIterator() {
		return orderers != null ? orderers.values().iterator() : null;
	}
	
	public NetworkListOrderer getOrderer(long ref) {
		return orderers != null ? orderers.get(ref) : null;
	}
}
