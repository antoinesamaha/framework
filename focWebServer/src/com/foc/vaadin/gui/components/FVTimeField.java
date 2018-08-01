package com.foc.vaadin.gui.components;

import org.xml.sax.Attributes;

import com.foc.property.FProperty;
import com.foc.property.FTime;

@SuppressWarnings({ "serial"})
public class FVTimeField extends FVTextField {

	public FVTimeField(FProperty property, Attributes attributes) {
		super(property, attributes);
	}
	
	@Override
  public boolean copyGuiToMemory() {
		String value = getValue();
	  if(getFocData() instanceof FTime){
	  	if(!value.contains(":")){
	  		 if(value.length() == 4){
	  			 value = value.substring(0,2) + ":" + value.substring(2,4);
	  		 }else if(value.length() == 3){
	  			 value = "0" + value.substring(0,1) + ":" + value.substring(1,3);
	  		 }
	  	}
	  }
  	((FProperty)getFocData()).setString(value);
	  return false;
  }  
}
