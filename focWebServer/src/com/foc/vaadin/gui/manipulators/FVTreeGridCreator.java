package com.foc.vaadin.gui.manipulators;

import org.xml.sax.Attributes;

import com.foc.Globals;
import com.foc.access.FocDataMap;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.FocWebEnvironment;
import com.foc.vaadin.fields.FocXMLGuiComponentCreator;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.components.treeGrid.FVTreeGrid;
import com.foc.vaadin.gui.layouts.FVTableWrapperLayout;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;

public class FVTreeGridCreator implements FocXMLGuiComponentCreator {

	@Override
	public FocXMLGuiComponent newGuiComponent(FocXMLLayout xmlLayout, IFocData focData, Attributes attributes, IFocData rootFocData, String dataPathFromRootFocData) {
		String displayDataPath = attributes != null ? attributes.getValue(FXML.ATT_CAPTION_PROPERTY) : null; 
		
    FVTreeGrid treeTable = new FVTreeGrid(displayDataPath, attributes);
    
    if(focData != null){
    	if(focData instanceof FocDataMap){
    		focData = ((FocDataMap)focData).getMainFocData();
    	}
    	
      treeTable.setFocData(focData);
      treeTable.getTableTreeDelegate().fillButtonsAndPopupMenus();//Has to called after the setFocData so that we have the FocDesc for rights
    }else{
      Globals.showNotification("NULL DATA", "For TreeTable", FocWebEnvironment.TYPE_WARNING_MESSAGE);
    }
    
    FVTableWrapperLayout tableWrapper = new FVTableWrapperLayout();
    tableWrapper.setTableOrTree(xmlLayout, treeTable);
    FocXMLGuiComponentStatic.setRootFocDataWithDataPath(tableWrapper, rootFocData, dataPathFromRootFocData);
    return tableWrapper;	
  }
}