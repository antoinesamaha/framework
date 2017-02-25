package com.foc.vaadin.gui.manipulators;

import org.xml.sax.Attributes;

import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.fields.FocXMLGuiComponentCreator;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.components.FVLine;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;

public class FVLineCreator implements FocXMLGuiComponentCreator {

	@Override
	public FocXMLGuiComponent newGuiComponent(FocXMLLayout xmlLayout, IFocData focData, Attributes attributes, IFocData rootFocData, String dataPathFromRootFocData) {
	  FVLine line = new FVLine(attributes);
	  FocXMLGuiComponentStatic.setRootFocDataWithDataPath(line, rootFocData, dataPathFromRootFocData);
    return line;
	}
}
