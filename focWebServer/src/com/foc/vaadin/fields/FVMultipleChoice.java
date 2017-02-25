package com.foc.vaadin.fields;

import org.xml.sax.Attributes;

import com.foc.property.FProperty;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.components.FVMultipleChoiceComboBox;
import com.foc.vaadin.gui.components.FVMultipleChoiceOptionGroup;
import com.foc.vaadin.gui.components.FVMultipleChoiceOptionGroupPopupView;
import com.foc.vaadin.gui.layouts.FVWrapperLayout;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;

public class FVMultipleChoice implements FocXMLGuiComponentCreator {

  @Override
  public FocXMLGuiComponent newGuiComponent(FocXMLLayout xmlLayout, IFocData focData, Attributes attributes, IFocData rootFocData, String dataPathFromRootFocData) {
  	FProperty property = (FProperty) focData;
  	FocXMLGuiComponent comp = null;
  	
  	boolean usePopupView = attributes != null && attributes.getValue(FXML.ATT_USE_POPUP_VIEW) != null && 
                                                 attributes.getValue(FXML.ATT_USE_POPUP_VIEW).equalsIgnoreCase("true");

		if(usePopupView){
			comp = new FVMultipleChoiceOptionGroupPopupView(property, attributes);
		}else{
	  	boolean optionGroup = false;
	  	if(attributes != null){
	  		String optGroup = attributes.getValue(FXML.ATT_OPTION_GROUP);
	  		if(optGroup != null && optGroup.toLowerCase().equals("true")){
	  			optionGroup = true;
	  		}
	  	}
	  	
	  	if(optionGroup){
	  		comp = new FVMultipleChoiceOptionGroup(property, attributes);
	  	}else{
	  		comp = new FVMultipleChoiceComboBox(property, attributes);
	  	}
		}
		FocXMLGuiComponentStatic.setRootFocDataWithDataPath(comp, rootFocData, dataPathFromRootFocData);
    return FVWrapperLayout.wrapIfNecessary(comp);
  }
  
}
