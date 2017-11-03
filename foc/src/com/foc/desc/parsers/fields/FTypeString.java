package com.foc.desc.parsers.fields;

import java.lang.reflect.Field;

import com.foc.annotations.model.fields.FocString;
import com.foc.desc.field.FField;
import com.foc.desc.field.FStringField;

public class FTypeString extends FocFieldTypAbstract<FocString> {

	@Override
	public String getTypeName() {
		return TYPE_STRING;
	}

	@Override
	public FField newFField(Class focObjClass, Field f, FocString a) {
		FField focField = null;
		focField = new FStringField(getDBFieldName(f), getFieldTitle(f), FField.NO_FIELD_ID, false, a.size());
		focField.setMandatory(a.mandatory());
		focField.setDBResident(a.dbResident());
		return focField;
	}

}
