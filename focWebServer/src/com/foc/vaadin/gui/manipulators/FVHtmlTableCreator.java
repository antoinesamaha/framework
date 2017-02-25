package com.foc.vaadin.gui.manipulators;

import org.xml.sax.Attributes;

import com.foc.Globals;
import com.foc.access.FocDataMap;
import com.foc.list.FocList;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.FocWebEnvironment;
import com.foc.vaadin.fields.FocXMLGuiComponentCreator;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.components.htmlTable.FVHtmlTable;
import com.foc.vaadin.gui.layouts.FVTableWrapperLayout;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;

public class FVHtmlTableCreator implements FocXMLGuiComponentCreator {

	@Override
	public FocXMLGuiComponent newGuiComponent(FocXMLLayout xmlLayout, IFocData focData, Attributes attributes, IFocData rootFocData, String dataPathFromRootFocData) {
    FVHtmlTable table = new FVHtmlTable(attributes);
    table.setSizeFull();

    if(focData != null){
    	if(focData instanceof FocDataMap){
    		focData = ((FocDataMap)focData).getMainFocData();
    	}
    	if(attributes != null){
    		String reloadAtt = attributes.getValue(FXML.ATT_RELOAD);
    		if(reloadAtt != null && reloadAtt.toLowerCase().equals("true")){
    			if(focData instanceof FocList){
    				((FocList)focData).reloadFromDB();
    			}
    		}
    	}
      table.setFocData(focData);
//      table.getTableTreeDelegate().fillButtonsAndPopupMenus();//Has to called after the setFocData so that we have the FocDesc for rights
    }else{
      Globals.showNotification("NULL DATA", "For table", FocWebEnvironment.TYPE_WARNING_MESSAGE);
    }

    FVTableWrapperLayout tableWrapper = new FVTableWrapperLayout();
  	FocXMLGuiComponentStatic.setRootFocDataWithDataPath(table, rootFocData, dataPathFromRootFocData);
    
    tableWrapper.setTableOrTree(xmlLayout, table);
    //This line has to be after the set table because the Layout is calling the table to set the attribute
    tableWrapper.setAttributes(attributes);
    
    return tableWrapper;
	}
}