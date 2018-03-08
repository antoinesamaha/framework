package com.foc.desc.parsers.fields;

import java.lang.reflect.Field;

import com.foc.annotations.model.fields.FocPassword;
import com.foc.desc.field.FField;
import com.foc.desc.field.FPasswordField;

public class FTypePassword extends FocFieldTypAbstract<FocPassword> {

	@Override
	public String getTypeName() {
		return TYPE_PASSWORD;
	}

	@Override
	public FField newFField(Class focObjClass, Field f, FocPassword a) {
		FField focField = null;
		focField = new FPasswordField(getDBFieldName(f), getFieldTitle(f), FField.NO_FIELD_ID, false, a.size());
		focField.setMandatory(a.mandatory());
		focField.setDBResident(a.dbResident());
		return focField;
	}

}
