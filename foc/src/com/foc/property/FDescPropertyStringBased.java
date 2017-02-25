package com.foc.property;

import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.desc.field.FDescFieldStringBased;

public class FDescPropertyStringBased extends FMultipleChoiceString implements IFDescProperty {

	private FocDesc lastFetchedFocDesc = null;  
	
	public FDescPropertyStringBased(FocObject focObj, int fieldID, String str) {
		super(focObj, fieldID, (String) (str == null ? "" : str));
	}

	public void dispose(){
		super.dispose();
	}

	public FocDesc getSelectedFocDesc(){
		String focDescName = getString();
		if(lastFetchedFocDesc == null || !lastFetchedFocDesc.getStorageName().equals(focDescName)){
			FDescFieldStringBased descField = (FDescFieldStringBased)getFocField();
			lastFetchedFocDesc = descField != null ? descField.getFocDesc(focDescName) : null; 
		}
		return lastFetchedFocDesc;
	}
	
	public void setString(String str){
		super.setString(str);
	}

}
