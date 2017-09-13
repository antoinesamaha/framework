package com.foc.vaadin.gui.network;

import org.vaadin.visjs.networkDiagram.Node;

import com.foc.desc.FocObject;

public abstract class FocObjectNetwork<FO extends FocObject> {

	public abstract Node fill(); 
	
	private FO         focObject = null;
	private FocNetwork network   = null;
	
	public FocObjectNetwork(FO focObject, FocNetwork 	network){
		this.focObject = focObject;
		this.network   = network;
		network.addFocObjectNetwork(this);
	}
	
	public void dispose(){
		focObject = null;
		network   = null;
	}
	
	public FO getFocObject(){
		return focObject;
	}
	
	public FocNetwork getNetwork(){
		return network;
	}
}
