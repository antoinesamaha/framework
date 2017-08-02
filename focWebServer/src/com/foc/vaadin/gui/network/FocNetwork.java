package com.foc.vaadin.gui.network;

import java.util.HashMap;

import org.vaadin.visjs.networkDiagram.NetworkDiagram;
import org.vaadin.visjs.networkDiagram.Node;
import org.vaadin.visjs.networkDiagram.options.Options;

import com.foc.desc.FocObject;

public abstract class FocNetwork extends NetworkDiagram {
	
	public HashMap<String, Node> drawnNodes = null;
	
	public FocNetwork(Options options){
		super(options);
		
		drawnNodes = new HashMap<String, Node>();
	}
	
	public void dispose(){
		
	}

	public Node findNode(String id){
		return drawnNodes != null ? drawnNodes.get(id) : null;
	}
	
	public Node newNode(String storage, int ref, String caption, String iconName){
		return newNode(getID(storage, ref), caption, iconName);
	}

	public Node newNode(FocObject focObject, String caption, String iconName){
		return newNode(getID(focObject), caption, iconName);
	}

	public Node newNode(String id, String caption, String iconName){
		Node node = findNode(id);
		if(node == null){
			node = new Node(id, caption, iconName);
			super.addNode(node);
			drawnNodes.put(id, node);
		}
		return node;
	}

	public String getID(FocObject object){
		return object != null ? getID(object.getThisFocDesc().getStorageName(),object.getReferenceInt()) : null;
	}

	public String getID(String storage, int ref){
		return storage+"|"+ref;
	}

//	public Node addNode(String id, String caption, String iconName) {
//		Node node = new Node(id, caption, iconName);
//		super.addNode(node);
//		drawnNodes.put(id, node);
//		return node;
//	}


}
