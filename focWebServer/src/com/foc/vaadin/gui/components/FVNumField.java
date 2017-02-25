package com.foc.vaadin.gui.components;

import org.xml.sax.Attributes;

import com.foc.Globals;
import com.foc.property.FDouble;
import com.foc.property.FProperty;

@SuppressWarnings({ "serial"})
public class FVNumField extends FVTextField {

	public FVNumField(FProperty property, Attributes attributes) {
		super(property, attributes);
		addStyleName("numerical");
	}
	
	@Override
  protected boolean isWithSelectAllListenerOnFocus(){
		//DANGER - Keep
		//We stop selecting all characters in NumField and we will settle with the fact
		//That users can double click for that effect because:
		//1- This is causing the focus to jump to a specific unexpected cell
		//2- It is causing a focus call to the server for nothing
//  	return true;
		return false;
  }	
	
  @Override
  public void copyMemoryToGui() {
  	try{
  		if(getFocData() instanceof FDouble){
  			setValue((String) ((FDouble)getFocData()).getTableDisplayObject());
  		}
    }catch(Exception e){
    	Globals.logException(e);
    }
  }
}
