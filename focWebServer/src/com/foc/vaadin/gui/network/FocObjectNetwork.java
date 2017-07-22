package com.foc.vaadin.gui.network;

import org.vaadin.visjs.networkDiagram.Node;

import com.foc.desc.FocObject;

public abstract class FocObjectNetwork<FO extends FocObject> {

	public abstract Node fill(); 
	
	private FO         focObject = null;
	private FocNetwork network   = null;
	
	public FocObjectNetwork(FO focObject, FocNetwork 	network){
		this.focObject = focObject;
		this.network = network;
	}
	
	public FO getFocObject(){
		return focObject;
	}
	
	public FocNetwork getNetwork(){
		return network;
	}
	
	public static String getID(String storage, int ref){
		return storage+"|"+ref;
	}
	
	public static String getID(FocObject object){
		return object != null ? getID(object.getThisFocDesc().getStorageName(),object.getReferenceInt()) : null;
	}
	
	public static Node newNode(FocObject focObject, String caption, String iconName){
		return new Node(getID(focObject), caption, "VAADIN/themes/fenix/custom/isf/"+iconName);
	}
	
	public static Node newNode(String storage, int ref, String caption, String iconName){
		return new Node(getID(storage, ref), caption, "VAADIN/themes/fenix/custom/isf/"+iconName);
	}
}
