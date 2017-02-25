package com.foc.vaadin.fields;

import org.xml.sax.Attributes;

import com.foc.property.FProperty;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.components.FVMultipleChoiceStringField;
import com.foc.vaadin.gui.components.FVXMLViewSelector;
import com.foc.vaadin.gui.layouts.FVWrapperLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;

public class FVXMLViewSelectorCreator implements FocXMLGuiComponentCreator{

  @Override
  public FocXMLGuiComponent newGuiComponent(FocXMLLayout xmlLayout, IFocData focData, Attributes attributes, IFocData rootFocData, String dataPathFromRootFocData) {
    FVXMLViewSelector fvMultipleChoiceFieldStringBased = new FVXMLViewSelector((FProperty) focData, attributes);
    FocXMLGuiComponentStatic.setRootFocDataWithDataPath(fvMultipleChoiceFieldStringBased, rootFocData, dataPathFromRootFocData);
    return FVWrapperLayout.wrapIfNecessary(fvMultipleChoiceFieldStringBased);
  }

}
