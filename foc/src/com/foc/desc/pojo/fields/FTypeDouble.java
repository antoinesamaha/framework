package com.foc.desc.pojo.fields;

import java.lang.reflect.Field;

import com.foc.annotations.model.FocField;
import com.foc.desc.field.FField;
import com.foc.desc.field.FNumField;

public class FTypeDouble extends FocFieldTypAbstract {

	@Override
	public String getTypeName() {
		return TYPE_DOUBLE;
	}
	
	@Override
	protected int getDefaultSize() {
		return 10;
	}

	@Override
	public FField newFField(Class focObjClass, Field f, FocField fieldAnnotation) {
		FField focField = null;
		focField = new FNumField(getDBFieldName(f), getFieldTitle(f), FField.NO_FIELD_ID, false, getSize(fieldAnnotation), fieldAnnotation.decimal());
		return focField;
	}

}
