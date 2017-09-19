package com.foc.desc.pojo.fields;

import java.lang.reflect.Field;

import com.foc.annotations.model.FocField;
import com.foc.desc.field.FField;
import com.foc.desc.field.FIntField;

public class FTypeInteger extends FocFieldTypAbstract {

	@Override
	public String getTypeName() {
		return TYPE_INTEGER;
	}

	@Override
	public FField newFField(Class focObjClass, Field f, FocField fieldAnnotation) {
		FField focField = null;
		focField = new FIntField(getDBFieldName(f), getFieldTitle(f), FField.NO_FIELD_ID, false, fieldAnnotation.size());
		return focField;
	}

}
