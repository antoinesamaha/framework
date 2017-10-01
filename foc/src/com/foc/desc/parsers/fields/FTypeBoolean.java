package com.foc.desc.parsers.fields;

import java.lang.reflect.Field;

import com.foc.annotations.model.fields.FocBoolean;
import com.foc.desc.field.FBoolField;
import com.foc.desc.field.FField;

public class FTypeBoolean extends FocFieldTypAbstract<FocBoolean> {

	@Override
	public String getTypeName() {
		return TYPE_BOOLEAN;
	}

	@Override
	public FField newFField(Class focObjClass, Field f, FocBoolean a) {
		FField focField = null;
		focField = new FBoolField(getDBFieldName(f), getFieldTitle(f), FField.NO_FIELD_ID, false);
		focField.setMandatory(a.mandatory());
		return focField;
	}
	
}
