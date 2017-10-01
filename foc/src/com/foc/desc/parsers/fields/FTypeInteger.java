package com.foc.desc.parsers.fields;

import java.lang.reflect.Field;

import com.foc.annotations.model.fields.FocInteger;
import com.foc.desc.field.FField;
import com.foc.desc.field.FIntField;

public class FTypeInteger extends FocFieldTypAbstract<FocInteger> {

	@Override
	public String getTypeName() {
		return TYPE_INTEGER;
	}

	@Override
	public FField newFField(Class focObjClass, Field f, FocInteger a) {
		FField focField = null;
		focField = new FIntField(getDBFieldName(f), getFieldTitle(f), FField.NO_FIELD_ID, false, a.size());
		focField.setMandatory(a.mandatory());
		return focField;
	}

}
