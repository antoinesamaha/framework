package com.foc.vaadin.fields;

import org.xml.sax.Attributes;

import com.foc.property.FProperty;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.components.FVTextField;
import com.foc.vaadin.gui.layouts.FVWrapperLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.vaadin.data.validator.IntegerValidator;

public class FVInteger implements FocXMLGuiComponentCreator {

  @Override
  public FocXMLGuiComponent newGuiComponent(FocXMLLayout xmlLayout, IFocData focData, Attributes attributes, IFocData rootFocData, String dataPathFromRootFocData) {
  	FProperty property = (FProperty) focData;
  	FVTextField component = new FVTextField(property, attributes);
    component.addValidator(new IntegerValidator("Must be an Integer"));
    FocXMLGuiComponentStatic.setRootFocDataWithDataPath(component, rootFocData, dataPathFromRootFocData);
    return FVWrapperLayout.wrapIfNecessary(component);
  }

}
