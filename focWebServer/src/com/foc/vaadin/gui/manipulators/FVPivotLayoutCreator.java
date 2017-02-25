package com.foc.vaadin.gui.manipulators;

import org.xml.sax.Attributes;

import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.fields.FocXMLGuiComponentCreator;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.components.chart.FVPivotLayout;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;

public class FVPivotLayoutCreator implements FocXMLGuiComponentCreator {

	@Override
	public FocXMLGuiComponent newGuiComponent(FocXMLLayout xmlLayout, IFocData focData, Attributes attributes, IFocData rootFocData, String dataPathFromRootFocData) {
		FVPivotLayout pivotLayout = new FVPivotLayout(attributes);
		pivotLayout.setType(FXML.TAG_PIVOT_LAYOUT);
		FocXMLGuiComponentStatic.setRootFocDataWithDataPath(pivotLayout, rootFocData, dataPathFromRootFocData);		
		
		if(focData != null){
			rootFocData = focData;
		}
		
		pivotLayout.setFocData(rootFocData);
		pivotLayout.createDictionaryResolver(xmlLayout);
		
		return pivotLayout;
	}
}
