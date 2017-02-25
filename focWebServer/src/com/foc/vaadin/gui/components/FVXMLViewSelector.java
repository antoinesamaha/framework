package com.foc.vaadin.gui.components;

import org.xml.sax.Attributes;

import com.foc.desc.field.FField;
import com.foc.desc.field.FXMLViewSelectorField;
import com.foc.property.FProperty;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;

@SuppressWarnings("serial")
public class FVXMLViewSelector extends FVMultipleChoiceStringField {

  public FVXMLViewSelector(FProperty property, Attributes attributes) {
    super(property, attributes);
    FField fld = property != null ? property.getFocField() : null;
    if(fld != null && fld instanceof FXMLViewSelectorField){
    	XMLViewKey key = ((FXMLViewSelectorField) fld).getXmlViewKey();
    	String[] viewsArray = XMLViewDictionary.getInstance().getXmlViews(key);
    	if(viewsArray != null){
	    	for(int i=0; i<viewsArray.length; i++){
					addItem(viewsArray[i]);
				}
    	}
    }
  }
}