package com.foc.vaadin.gui.manipulators;

import java.lang.reflect.Method;

import org.xml.sax.Attributes;

import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.fields.FocXMLGuiComponentCreator;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.vaadin.gui.components.FVButtonClickEvent;
import com.foc.vaadin.gui.components.FVButton_Reflection;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;

public class FVButtonCreator implements FocXMLGuiComponentCreator {

	@Override
	public FocXMLGuiComponent newGuiComponent(FocXMLLayout xmlLayout, IFocData focData, Attributes attributes, IFocData rootFocData, String dataPathFromRootFocData) {
		Method method = null;
    
    String name = attributes.getValue(FXML.ATT_NAME);
    if(name != null && !name.isEmpty()){
    	String methodName = "button_"+name+"_Clicked";
    	try {
				method = xmlLayout.getClass().getMethod(methodName, FVButtonClickEvent.class);
			} catch (Exception e) {
				method = null;
			}
    }
    
    FVButton button = null;
    if(method != null){
    	button = new FVButton_Reflection(attributes, xmlLayout, method);
    }else{
    	button = new FVButton(attributes);	
    }
    FocXMLGuiComponentStatic.setRootFocDataWithDataPath(button, rootFocData, dataPathFromRootFocData);
    return button;
	}
}
