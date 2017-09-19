package com.foc.desc.pojo.fields;

import java.lang.reflect.Field;

import com.foc.annotations.model.FocField;
import com.foc.desc.field.FField;
import com.foc.desc.field.FStringField;

public class FTypeString extends FocFieldTypAbstract {

	@Override
	public String getTypeName() {
		return TYPE_STRING;
	}

	protected int getDefaultSize(){
		return 50;
	}

	@Override
	public FField newFField(Class focObjClass, Field f, FocField fieldAnnotation) {
		FField focField = null;
		focField = new FStringField(getDBFieldName(f), getFieldTitle(f), FField.NO_FIELD_ID, false, getSize(fieldAnnotation));
		return focField;
	}

}
