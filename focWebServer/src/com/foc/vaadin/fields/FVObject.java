package com.foc.vaadin.fields;

import org.xml.sax.Attributes;

import com.foc.Globals;
import com.foc.property.FObject;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.components.FVObjectComboBox;
import com.foc.vaadin.gui.components.FVObjectPopupView;
import com.foc.vaadin.gui.components.FVObjectSelector;
import com.foc.vaadin.gui.layouts.FVWrapperLayout;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;

public class FVObject implements FocXMLGuiComponentCreator {

  @Override
  public FocXMLGuiComponent newGuiComponent(FocXMLLayout xmlLayout, IFocData property, Attributes attributes, IFocData rootFocData, String dataPathFromRootFocData) {
    //return new FVObjectComboBox((FObject) property, null);
  	FocXMLGuiComponent comp = null;
  	
  	boolean usePopupView = attributes != null && attributes.getValue(FXML.ATT_USE_POPUP_VIEW) != null && 
  			                                         attributes.getValue(FXML.ATT_USE_POPUP_VIEW).equalsIgnoreCase("true");
  	
  	if(usePopupView){
  		comp = new FVObjectPopupView((FObject) property, attributes);
  	}else{
//  		boolean gearEnabled = true;
  		boolean gearEnabled = !Globals.isValo();
	  	
	  	if(attributes != null){
	  		String gearStr = attributes.getValue(FXML.ATT_GEAR_ENABLED);
	  		if(gearStr != null){
	  			gearEnabled = gearStr.toLowerCase().equals("true");
	  		}
	  	}
//	  	if(gearEnabled){
//	  		if(Globals.isValo()){
//	  			FVObjectComboBox combo = new FVObjectComboBox((FObject) property, attributes);
//	  			comp = combo;
//	  		}else{
//	  			comp = new FVObjectSelector((FObject) property, attributes);
//	  			//comp = new FVObjectComboBox((FObject) property, attributes);
//	  		}
//	  	}else{
//	  		comp = new FVObjectComboBox((FObject) property, attributes);
//	  	}
	  	
	  	if(gearEnabled){
	  		comp = new FVObjectSelector((FObject) property, attributes);
	  	}else{
	  		comp = new FVObjectComboBox((FObject) property, attributes);
	  	}
  	}
  	
  	FocXMLGuiComponentStatic.setRootFocDataWithDataPath(comp, rootFocData, dataPathFromRootFocData);
  	return FVWrapperLayout.wrapIfNecessary(comp);
  }

}
