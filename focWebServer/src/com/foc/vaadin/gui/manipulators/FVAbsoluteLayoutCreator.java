package com.foc.vaadin.gui.manipulators;

import org.xml.sax.Attributes;

import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.fields.FocXMLGuiComponentCreator;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.layouts.FVAbsoluteLayout;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;

public class FVAbsoluteLayoutCreator implements FocXMLGuiComponentCreator {

	@Override
	public FocXMLGuiComponent newGuiComponent(FocXMLLayout xmlLayout, IFocData focData, Attributes attributes, IFocData rootFocData, String dataPathFromRootFocData) {
		FVAbsoluteLayout layout = new FVAbsoluteLayout(attributes);
    layout.setType(FXML.TAG_ABSOLUTE_LAYOUT);
    FocXMLGuiComponentStatic.setRootFocDataWithDataPath(layout, rootFocData, dataPathFromRootFocData);
    return layout;
	}
}
