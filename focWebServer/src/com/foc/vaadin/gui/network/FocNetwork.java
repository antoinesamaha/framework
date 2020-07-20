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

import java.util.ArrayList;
import java.util.HashMap;

import org.vaadin.visjs.networkDiagram.Edge;
import org.vaadin.visjs.networkDiagram.NetworkDiagram;
import org.vaadin.visjs.networkDiagram.Node;
import org.vaadin.visjs.networkDiagram.options.Options;

import com.foc.ConfigInfo;
import com.foc.desc.FocObject;
import com.foc.util.Utils;

public abstract class FocNetwork extends NetworkDiagram {
	
	private static final int DEFAULT_MAX_LEVEL = 10;
	
	private HashMap<String, Node> drawnNodes = null;
	private HashMap<String, Edge> drawnEdges = null;
	
	private ArrayList<FocObjectNetwork> focObjectNetworks = null;
	private int level    = 0;
	private int maxLevel = 0;
	
	public FocNetwork(Options options){
		this(options, DEFAULT_MAX_LEVEL);
		
		String maxLevelString = ConfigInfo.getProperty("network.max.level");
		if (maxLevelString != null) {
			maxLevel = Utils.parseInteger(maxLevelString, DEFAULT_MAX_LEVEL);
		}
	}
	
	public FocNetwork(Options options, int maxLevel){
		super(options);
		
		drawnNodes = new HashMap<String, Node>();
		drawnEdges = new HashMap<String, Edge>();

		focObjectNetworks = new ArrayList<FocObjectNetwork>();
		this.maxLevel = maxLevel;
	}
	
	public void dispose(){
		if(drawnNodes != null){
			drawnNodes.clear();
			drawnNodes = null;
		}
		if(drawnEdges != null){
			drawnEdges.clear();
			drawnEdges = null;
		}
		if(focObjectNetworks != null){
			for(int i=0; i<focObjectNetworks.size(); i++){
				FocObjectNetwork fon = focObjectNetworks.get(i);
				fon.dispose();
			}
			focObjectNetworks.clear();
		}
	}

	public boolean incrementLevel() {
		level++;
		return level < maxLevel;
	}
	
	public int decrementLevel() {
		level--;
		return level;
	}
	
	public void addFocObjectNetwork(FocObjectNetwork focObjectNetwork){
		if(focObjectNetworks != null){
			focObjectNetworks.add(focObjectNetwork);
		}
	}

	public Edge findEdge(String id){
		return drawnEdges != null ? drawnEdges.get(id) : null;
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
		return object != null ? getID(object.getThisFocDesc().getStorageName(), object.getReferenceInt()) : null;
	}

	public String getID(String storage, long ref){
		return storage+"|"+ref;
	}

	public int getMaxLevel() {
		return maxLevel;
	}
	
	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}
}
