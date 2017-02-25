package com.foc.vaadin.gui.components.report;

import org.xml.sax.Attributes;

import com.foc.Globals;
import com.foc.access.FocDataMap;
import com.foc.list.FocList;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.FocWebEnvironment;
import com.foc.vaadin.fields.FocXMLGuiComponentCreator;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;

public class FVReportCreator implements FocXMLGuiComponentCreator {

	@Override
	public FocXMLGuiComponent newGuiComponent(FocXMLLayout xmlLayout, IFocData focData, Attributes attributes, IFocData rootFocData, String dataPathFromRootFocData) {
		FVReport fvReport = new FVReport(attributes, xmlLayout);
		
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
    	fvReport.setFocData(focData);
    }else{
      Globals.showNotification("NULL DATA", "For table", FocWebEnvironment.TYPE_WARNING_MESSAGE);
    }
		
		FocXMLGuiComponentStatic.setRootFocDataWithDataPath(fvReport, rootFocData, dataPathFromRootFocData);
		fvReport.setAttributes(attributes);
		
		return fvReport;
	}
}
