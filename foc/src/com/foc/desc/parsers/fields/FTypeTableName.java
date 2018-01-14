package com.foc.desc.parsers.fields;

import java.lang.reflect.Field;

import com.foc.annotations.model.fields.FocString;
import com.foc.annotations.model.fields.FocTableName;
import com.foc.desc.field.FDescFieldStringBased;
import com.foc.desc.field.FField;
import com.foc.desc.field.FStringField;

public class FTypeTableName extends FocFieldTypAbstract<FocTableName> {

	@Override
	public String getTypeName() {
		return TYPE_TABLE_NAME;
	}

	@Override
	public FField newFField(Class focObjClass, Field f, FocTableName a) {
		FField focField = null;
		focField = new FDescFieldStringBased(getDBFieldName(f), getFieldTitle(f), FField.NO_FIELD_ID, false);
		focField.setMandatory(a.mandatory());
		focField.setDBResident(a.dbResident());
		focField.setSize(a.size());
		return focField;
	}

}
