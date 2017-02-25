package com.foc.vaadin.gui.manipulators;

import org.xml.sax.Attributes;

import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.FocXMLGuiComponent;

public interface FVGUICreator {
  public FocXMLGuiComponent create(String name, IFocData focData, ICentralPanel centralPanel, Attributes attributes);
}
