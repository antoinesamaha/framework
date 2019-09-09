/*******************************************************************************
 * Copyright 2016 Antoine Nicolas SAMAHA
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.foc.vaadin.gui.network;

import org.vaadin.visjs.networkDiagram.Node;
import org.vaadin.visjs.networkDiagram.Node.NodeDoubleClickListener;
import org.vaadin.visjs.networkDiagram.event.node.DoubleClickEvent;

import com.foc.Globals;
import com.foc.IFocEnvironment;
import com.foc.desc.FocObject;
import com.vaadin.ui.UI;

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
	
	/*
	public void addClickListener(Node node){
		NodeDoubleClickListener listener = new NodeDoubleClickListener(node) {
			@Override
			public void onFired(DoubleClickEvent arg0) {
				Globals.showNotification("Hello", "Hello", IFocEnvironment.TYPE_HUMANIZED_MESSAGE);
			}
		};
		getNetwork().addNodeDoubleClickListener(listener);
	}
	*/
}
