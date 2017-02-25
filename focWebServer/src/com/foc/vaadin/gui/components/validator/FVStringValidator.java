package com.foc.vaadin.gui.components.validator;

import com.foc.Globals;
import com.vaadin.data.Validator;

@SuppressWarnings("serial")
public class FVStringValidator implements Validator{

	private boolean capital  = false;
	private boolean noSpaces = false;
	
	public FVStringValidator(){
		
	}
	
	@Override
	public void validate(Object value) throws InvalidValueException {
		try{
			String str = (String) value;
			if(isCapital())  str = str.toUpperCase();
			if(isNoSpaces()) str.replaceAll(" ", "_");
		}catch(Exception e){
			Globals.logException(e);
		}
	}

	public boolean isCapital() {
		return capital;
	}

	public void setCapital(boolean capital) {
		this.capital = capital;
	}

	public boolean isNoSpaces() {
		return noSpaces;
	}

	public void setNoSpaces(boolean noSpaces) {
		this.noSpaces = noSpaces;
	}
	
}
