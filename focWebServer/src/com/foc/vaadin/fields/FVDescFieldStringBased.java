package com.foc.vaadin.fields;

import org.xml.sax.Attributes;

import com.foc.property.FProperty;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.components.FVMultipleChoiceFocDesc;
import com.foc.vaadin.gui.layouts.FVWrapperLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;

public class FVDescFieldStringBased implements FocXMLGuiComponentCreator{

  @Override
  public FocXMLGuiComponent newGuiComponent(FocXMLLayout xmlLayout, IFocData focData, Attributes attributes, IFocData rootFocData, String dataPathFromRootFocData) {
    FVMultipleChoiceFocDesc mult = new FVMultipleChoiceFocDesc((FProperty) focData, attributes);
    FocXMLGuiComponentStatic.setRootFocDataWithDataPath(mult, rootFocData, dataPathFromRootFocData);
    return FVWrapperLayout.wrapIfNecessary(mult);
  }

}
