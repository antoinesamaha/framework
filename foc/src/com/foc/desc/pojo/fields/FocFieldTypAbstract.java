package com.foc.desc.pojo.fields;

import java.lang.reflect.Field;

import com.foc.Globals;
import com.foc.annotations.model.FocField;

public abstract class FocFieldTypAbstract implements IFocFieldType {

	protected String getDBFieldName(Field field){
		String strValue = null;
		try{
			strValue = (String) field.get (null);
		}catch (Exception e){
			Globals.logException(e);
		}
		return strValue;
	}
	
	protected String getFieldTitle(Field field){
		return getDBFieldName(field);
	}
	
	protected int getDefaultSize(){
		return 0;
	}
	
	protected int getSize(FocField fieldAnnotation){
		int size = fieldAnnotation.size();  
		if(size == 0) size = getDefaultSize();
		return size;
	}
}
