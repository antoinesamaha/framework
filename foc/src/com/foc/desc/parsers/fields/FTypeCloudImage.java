package com.foc.desc.parsers.fields;

import java.lang.reflect.Field;

import com.foc.annotations.model.fields.FocCloudImage;
import com.foc.desc.field.FCloudImageField;
import com.foc.desc.field.FField;

public class FTypeCloudImage extends FocFieldTypAbstract<FocCloudImage> {

	@Override
	public String getTypeName() {
		return TYPE_CLOUD_IMAGE;
	}

	@Override
	public FField newFField(Class focObjClass, Field f, FocCloudImage a) {
		FField focField = null;
		focField = new FCloudImageField(getDBFieldName(f), getFieldTitle(f), FField.NO_FIELD_ID, false);
		focField.setDBResident(a.dbResident());
		return focField;
	}

}
