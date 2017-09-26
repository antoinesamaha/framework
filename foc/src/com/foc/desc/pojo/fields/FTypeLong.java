package com.foc.desc.pojo.fields;

import java.lang.reflect.Field;

import com.foc.annotations.model.fields.FocLong;
import com.foc.desc.field.FField;
import com.foc.desc.field.FLongField;

public class FTypeLong extends FocFieldTypAbstract<FocLong> {

	@Override
	public String getTypeName() {
		return TYPE_LONG;
	}

	@Override
	public FField newFField(Class focObjClass, Field f, FocLong a) {
		FField focField = null;
		focField = new FLongField(getDBFieldName(f), getFieldTitle(f), FField.NO_FIELD_ID, false, a.size());
		focField.setMandatory(a.mandatory());
		return focField;
	}

}
