package com.foc.desc.pojo.fields;

import java.lang.reflect.Field;

import com.foc.annotations.model.fields.FocBlob;
import com.foc.desc.field.FBlobField;
import com.foc.desc.field.FField;

public class FTypeBlob extends FocFieldTypAbstract<FocBlob> {

	@Override
	public String getTypeName() {
		return TYPE_BLOB;
	}

	@Override
	public FField newFField(Class focObjClass, Field f, FocBlob a) {
		FField focField = null;
		focField = new FBlobField(getDBFieldName(f), getFieldTitle(f), FField.NO_FIELD_ID, false);
		focField.setMandatory(a.mandatory());
		return focField;
	}

}
