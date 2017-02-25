package com.foc.vaadin.fields;

import org.xml.sax.Attributes;

import com.foc.property.FProperty;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.components.FVEmailField;
import com.foc.vaadin.gui.layouts.FVWrapperLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;

public class FVEmail implements FocXMLGuiComponentCreator {

  @Override
  public FocXMLGuiComponent newGuiComponent(FocXMLLayout xmlLayout, IFocData focData, Attributes attributes, IFocData rootFocData, String dataPathFromRootFocData) {
  	FProperty property = (FProperty) focData;
//  	FVTextField tx = new FVTextField(property, attributes);
  	FVEmailField tx = new FVEmailField(property, attributes);
  	FocXMLGuiComponentStatic.setRootFocDataWithDataPath(tx, rootFocData, dataPathFromRootFocData);
    return FVWrapperLayout.wrapIfNecessary(tx);
  }

}
