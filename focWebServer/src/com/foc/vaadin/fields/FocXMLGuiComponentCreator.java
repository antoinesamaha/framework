package com.foc.vaadin.fields;

import org.xml.sax.Attributes;

import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;

public interface FocXMLGuiComponentCreator {
  public FocXMLGuiComponent newGuiComponent(FocXMLLayout xmlLayout, IFocData focData, Attributes attributes, IFocData rootFocData, String dataPathFromRootFocData);
}
