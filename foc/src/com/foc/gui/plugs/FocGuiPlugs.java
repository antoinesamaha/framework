package com.foc.gui.plugs;

import java.util.HashMap;

import com.foc.gui.FAbstractListPanel;

public class FocGuiPlugs {
	
	private HashMap<String, ITableAfterConstruction> tableAfterConstructionArray = null;
	
	public FocGuiPlugs(){
		tableAfterConstructionArray = new HashMap<String, ITableAfterConstruction>(); 
	}
	
	public void dispose(){
		if(tableAfterConstructionArray != null){
			tableAfterConstructionArray.clear();
			tableAfterConstructionArray = null;
		}
	}
	
	public void putTableAfterConstruction(String listPanelClassName, ITableAfterConstruction tableAfterConstruction){
		tableAfterConstructionArray.put(listPanelClassName, tableAfterConstruction);
	}
	
	public void callTableAfterConstruct(FAbstractListPanel listPanel){
		ITableAfterConstruction afterConstructionInterface = tableAfterConstructionArray.get(listPanel.getClass().getName());
		if(afterConstructionInterface != null){
			afterConstructionInterface.afterConstruction(listPanel);
		}
	}
	
	private static FocGuiPlugs focGuiPlugs = null;
	public static FocGuiPlugs getInstance(){
		if(focGuiPlugs == null){
			focGuiPlugs = new FocGuiPlugs(); 
		}
		return focGuiPlugs;
	}
}
