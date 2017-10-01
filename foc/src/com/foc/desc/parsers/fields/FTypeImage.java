package com.foc.desc.parsers.fields;

import java.lang.reflect.Field;

import com.foc.annotations.model.fields.FocImage;
import com.foc.desc.field.FField;
import com.foc.desc.field.FImageField;

public class FTypeImage extends FocFieldTypAbstract<FocImage> {

	@Override
	public String getTypeName() {
		return TYPE_IMAGE;
	}

	@Override
	public FField newFField(Class focObjClass, Field f, FocImage a) {
		FField focField = null;
		focField = new FImageField(getDBFieldName(f), getFieldTitle(f), FField.NO_FIELD_ID, 0, 0);
		return focField;
	}

}
