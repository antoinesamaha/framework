package com.foc.desc.parsers.fields;

import java.lang.reflect.Field;

import com.foc.annotations.model.fields.FocTime;
import com.foc.desc.field.FField;
import com.foc.desc.field.FTimeField;

public class FTypeTime extends FocFieldTypAbstract<FocTime> {

	@Override
	public String getTypeName() {
		return TYPE_TIME_FIELD;
	}

	@Override
	public FField newFField(Class focObjClass, Field f, FocTime a) {
		FField focField = null;
		focField = new FTimeField(getDBFieldName(f), getFieldTitle(f), FField.NO_FIELD_ID, false);
		focField.setMandatory(a.mandatory());
		focField.setDBResident(a.dbResident());
		return focField;
	}
}
