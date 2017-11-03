package com.foc.desc.parsers.fields;

import java.lang.reflect.Field;

import com.foc.annotations.model.fields.FocFile;
import com.foc.desc.field.FCloudStorageField;
import com.foc.desc.field.FField;

public class FTypeFile extends FocFieldTypAbstract<FocFile> {

	@Override
	public String getTypeName() {
		return TYPE_FILE;
	}

	@Override
	public FField newFField(Class focObjClass, Field f, FocFile a) {
		FField focField = null;
		focField = new FCloudStorageField(getDBFieldName(f), getFieldTitle(f), FField.NO_FIELD_ID, false, a.fileNameField());
		focField.setDBResident(a.dbResident());
		return focField;
	}

}
