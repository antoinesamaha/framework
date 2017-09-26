package com.foc.desc.pojo.fields;

import java.lang.reflect.Field;

import com.foc.annotations.model.fields.FocDouble;
import com.foc.desc.field.FField;
import com.foc.desc.field.FNumField;

public class FTypeDouble extends FocFieldTypAbstract<FocDouble> {

	@Override
	public String getTypeName() {
		return TYPE_DOUBLE;
	}

	@Override
	public FField newFField(Class focObjClass, Field f, FocDouble a) {
		FField focField = null;
		focField = new FNumField(getDBFieldName(f), getFieldTitle(f), FField.NO_FIELD_ID, false, a.size(), a.decimal());
		focField.setMandatory(a.mandatory());
		return focField;
	}

}
