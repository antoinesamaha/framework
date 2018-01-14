package com.foc.desc.parsers.fields;

import java.lang.reflect.Field;

import com.foc.annotations.model.fields.FocLong;
import com.foc.annotations.model.fields.FocReference;
import com.foc.desc.field.FField;
import com.foc.desc.field.FLongField;
import com.foc.desc.field.FReferenceField;

public class FTypeReference extends FocFieldTypAbstract<FocReference> {

	@Override
	public String getTypeName() {
		return TYPE_Reference;
	}

	@Override
	public FField newFField(Class focObjClass, Field f, FocReference a) {
		FField focField = null;
		focField = new FReferenceField(getDBFieldName(f), getFieldTitle(f));
		focField.setId(FField.NO_FIELD_ID);
		focField.setMandatory(a.mandatory());
		focField.setDBResident(a.dbResident());
		return focField;
	}

}
