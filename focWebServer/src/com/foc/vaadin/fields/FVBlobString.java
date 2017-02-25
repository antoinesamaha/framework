package com.foc.vaadin.fields;

import org.xml.sax.Attributes;

import com.foc.property.FProperty;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.components.FVRichTextArea;
import com.foc.vaadin.gui.layouts.FVWrapperLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;

public class FVBlobString implements FocXMLGuiComponentCreator {
  
  @Override
  public FocXMLGuiComponent newGuiComponent(FocXMLLayout xmlLayout, IFocData focData, Attributes attributes, IFocData rootFocData, String dataPathFromRootFocData) {
  	FProperty property = (FProperty) focData;
    FVRichTextArea tx = new FVRichTextArea(property, attributes);
    FocXMLGuiComponentStatic.setRootFocDataWithDataPath(tx, rootFocData, dataPathFromRootFocData);
    return FVWrapperLayout.wrapIfNecessary(tx);
  }
}
