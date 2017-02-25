package com.foc.vaadin.gui.manipulators;

import org.xml.sax.Attributes;

import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.fields.FocXMLGuiComponentCreator;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.components.FVBlobDisplay;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;

public class FVBlobDisplayCreator implements FocXMLGuiComponentCreator {

	@Override
	public FocXMLGuiComponent newGuiComponent(FocXMLLayout xmlLayout, IFocData focData, Attributes attributes, IFocData rootFocData, String dataPathFromRootFocData) {
		FVBlobDisplay blobDisplay = new FVBlobDisplay(xmlLayout, focData, attributes);
		FocXMLGuiComponentStatic.setRootFocDataWithDataPath(blobDisplay, rootFocData, dataPathFromRootFocData);
		return blobDisplay;
	}

}
