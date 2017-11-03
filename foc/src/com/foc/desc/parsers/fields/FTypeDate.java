package com.foc.desc.parsers.fields;

import java.lang.reflect.Field;

import com.foc.annotations.model.fields.FocDate;
import com.foc.desc.field.FDateField;
import com.foc.desc.field.FField;

public class FTypeDate extends FocFieldTypAbstract<FocDate> {

	@Override
	public String getTypeName() {
		return TYPE_DATE_FIELD;
	}

	@Override
	public FField newFField(Class focObjClass, Field f, FocDate a) {		
		FField focField = null;
		focField = new FDateField(getDBFieldName(f), getFieldTitle(f), FField.NO_FIELD_ID, false);
		focField.setDBResident(a.dbResident());
		return focField;
	}

}
