package com.foc.desc.parsers.fields;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.foc.Globals;

public abstract class FocFieldTypAbstract<A extends Annotation> implements IFocFieldType<A> {

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
	
}
