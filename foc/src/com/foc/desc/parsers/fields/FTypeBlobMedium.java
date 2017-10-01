package com.foc.desc.parsers.fields;

import java.lang.reflect.Field;

import com.foc.annotations.model.fields.FocBlobMedium;
import com.foc.desc.field.FBlobMediumField;
import com.foc.desc.field.FField;

public class FTypeBlobMedium extends FocFieldTypAbstract<FocBlobMedium> {

	@Override
	public String getTypeName() {
		return TYPE_BLOB_MEDIUM;
	}

	@Override
	public FField newFField(Class focObjClass, Field f, FocBlobMedium a) {
		FField focField = null;
		focField = new FBlobMediumField(getDBFieldName(f), getFieldTitle(f), FField.NO_FIELD_ID, false);
		focField.setMandatory(a.mandatory());
		return focField;
	}

}
