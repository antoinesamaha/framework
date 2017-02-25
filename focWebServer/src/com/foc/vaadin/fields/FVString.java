package com.foc.vaadin.fields;

import org.xml.sax.Attributes;

import com.foc.property.FProperty;
import com.foc.shared.dataStore.IFocData;
import com.foc.util.FocMath;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.components.FVExpandingTextAreaPopupView;
import com.foc.vaadin.gui.components.FVTextArea;
import com.foc.vaadin.gui.components.FVTextField;
import com.foc.vaadin.gui.components.FVTextFieldAutoComplete;
import com.foc.vaadin.gui.layouts.FVWrapperLayout;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;

public class FVString implements FocXMLGuiComponentCreator {

  @Override
  public FocXMLGuiComponent newGuiComponent(FocXMLLayout xmlLayout, IFocData focData, Attributes attributes, IFocData rootFocData, String dataPathFromRootFocData) {
  	FocXMLGuiComponent comp = null;
    
    FProperty property = null;
    if(focData instanceof FProperty){
    	property = (FProperty) focData;
    }
    
    boolean autoComplete    = false;
    boolean useTextArea     = false;
    boolean wrapText        = false;
    int     nbrOfRowsInWrap = 0;
    
    if(attributes != null){
    	String txtAreaValue  = attributes.getValue(FXML.ATT_TEXT_AREA);
    	String wraptextValue = attributes.getValue(FXML.ATT_WRAP_TEXT);
    	String autoCompleteValue = attributes.getValue(FXML.ATT_AUTOCOMPLETE);
    	
    	if(autoCompleteValue != null && autoCompleteValue.toLowerCase().equals("true")){
    		autoComplete = true;
    	}
    	
    	if(txtAreaValue != null && txtAreaValue.toLowerCase().equals("true")){
    		useTextArea = true;
    	}
    	
    	if(wraptextValue != null){
    		if(wraptextValue.toLowerCase().equals("true")){
    			wrapText = true;
    		}else{
    			int nbrOfRows = FocMath.parseInteger(wraptextValue);
    			if(nbrOfRows > 0){
    			  nbrOfRowsInWrap = nbrOfRows;
    				wrapText = true;
    			}
    		}
    	}
    }
    
    if(autoComplete){
    	comp = new FVTextFieldAutoComplete((FProperty) focData, attributes);
    } else if (useTextArea || wrapText) {
    	if(wrapText){
//    		comp = new FVExpandingTextArea(property, attributes);
    		comp = new FVExpandingTextAreaPopupView(property, attributes);
    	}else{
    		comp = new FVTextArea(property, attributes);    		
    	}
    } else {
    	comp = new FVTextField(property, attributes);
    }
  	FocXMLGuiComponentStatic.setRootFocDataWithDataPath(comp, rootFocData, dataPathFromRootFocData);

    return FVWrapperLayout.wrapIfNecessary(comp);
  }

}
