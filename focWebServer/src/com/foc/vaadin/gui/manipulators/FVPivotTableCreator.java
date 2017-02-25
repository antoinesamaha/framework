package com.foc.vaadin.gui.manipulators;

import org.xml.sax.Attributes;

import com.foc.Globals;
import com.foc.access.FocDataMap;
import com.foc.list.FocList;
import com.foc.shared.dataStore.IFocData;
import com.foc.tree.objectTree.FObjectTree;
import com.foc.vaadin.FocWebEnvironment;
import com.foc.vaadin.fields.FocXMLGuiComponentCreator;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.components.IPivotGrid;
import com.foc.vaadin.gui.components.pivot.FVPivotGrid;
import com.foc.vaadin.gui.components.pivot.FVPivotTable;
import com.foc.vaadin.gui.layouts.FVTableWrapperLayout;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;

public class FVPivotTableCreator implements FocXMLGuiComponentCreator {

	@Override
	public FocXMLGuiComponent newGuiComponent(FocXMLLayout xmlLayout, IFocData focData, Attributes attributes, IFocData rootFocData, String dataPathFromRootFocData) {
		
		IPivotGrid fvPivotTable = null;
		if(attributes != null && attributes.getValue(FXML.ATT_PIVOT_GRID) != null && attributes.getValue(FXML.ATT_PIVOT_GRID).toLowerCase().equals("true")){
			fvPivotTable = new FVPivotGrid(attributes);
		}else{
			fvPivotTable = new FVPivotTable(attributes);
		}
//	  FVPivotTable fvPivotTable = new FVPivotTable(attributes);
		fvPivotTable.setSizeFull();
		fvPivotTable.setSelectable(true);
    
    if(focData != null){
    	if(focData instanceof FocDataMap){
    		focData = ((FocDataMap)focData).getMainFocData();
    	}
    	
    	if(focData instanceof FocList){
    		fvPivotTable.setDataFocList((FocList) focData);
    	}else if(focData instanceof FObjectTree){
    		fvPivotTable.setDataFocList(((FObjectTree)focData).getFocList());
    	}
    	
    	fvPivotTable.getTableTreeDelegate().fillButtonsAndPopupMenus();
    }else{
      Globals.showNotification("NULL DATA", "For table", FocWebEnvironment.TYPE_WARNING_MESSAGE);
    }

    FVTableWrapperLayout tableWrapper = new FVTableWrapperLayout();
    FocXMLGuiComponentStatic.setRootFocDataWithDataPath(tableWrapper, rootFocData, dataPathFromRootFocData);
    
    tableWrapper.setTableOrTree(xmlLayout, fvPivotTable);
    //This line has to be after the set table because the Layout is calling the table to set the attribute
    tableWrapper.setAttributes(attributes);
    
    return tableWrapper;
	}
}