package com.foc.vaadin.gui.network.networkList;

import com.foc.desc.FocObject;
import com.foc.vaadin.gui.network.INetworkItem;
import org.vaadin.visjs.networkDiagram.Node;

public class NetworkListOrderer {
	private Node node = null;
	private INetworkItem item = null;
	private int level = -1;
	
	public NetworkListOrderer(Node node, INetworkItem item, int level) {
		this.node = node;
		this.item = item;
		this.level = level;		
	}
	
	public void dispose() {
		node = null;
		item = null;		
	}

	public INetworkItem getItem() {
		return item;
	}

	public int getLevel() {
		return level;
	}
	
	public long getRef() {
		long ref = -1;
		if (item != null) {
			FocObject obj = (FocObject) item;
			if(obj != null) ref = obj.getReferenceInt();
		}
		return ref;
	}

	public Node getNode() {
		return node;
	}
}
