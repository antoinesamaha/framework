package com.foc.vaadin.fields;

import org.xml.sax.Attributes;

import com.foc.property.FProperty;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.components.FVCheckBox;
import com.foc.vaadin.gui.layouts.FVWrapperLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;

public class FVBoolean implements FocXMLGuiComponentCreator {
  
  @Override
  public FocXMLGuiComponent newGuiComponent(FocXMLLayout xmlLayout, IFocData focData, Attributes attributes, IFocData rootFocData, String dataPathFromRootFocData) {
  	FProperty  property = (FProperty) focData;
    FVCheckBox checkBox = new FVCheckBox(property, attributes);
    FocXMLGuiComponentStatic.setRootFocDataWithDataPath(checkBox, rootFocData, dataPathFromRootFocData);
    return FVWrapperLayout.wrapIfNecessary(checkBox);
  }

}
