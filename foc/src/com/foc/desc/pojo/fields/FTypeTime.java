package com.foc.desc.pojo.fields;

import java.lang.reflect.Field;

import com.foc.annotations.model.FocField;
import com.foc.desc.field.FField;
import com.foc.desc.field.FStringField;
import com.foc.desc.field.FTimeField;

public class FTypeTime extends FocFieldTypAbstract {

	@Override
	public String getTypeName() {
		return TYPE_TIME_FIELD;
	}

	@Override
	public FField newFField(Class focObjClass, Field f, FocField fieldAnnotation) {
		FField focField = null;
		focField = new FTimeField(getDBFieldName(f), getFieldTitle(f), FField.NO_FIELD_ID, false);
		return focField;
	}
}
