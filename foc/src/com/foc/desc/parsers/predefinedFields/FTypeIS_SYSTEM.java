package com.foc.desc.parsers.predefinedFields;

import com.foc.annotations.model.predefinedFields.FocIS_SYSTEM;
import com.foc.desc.FocDesc;
import com.foc.desc.field.FField;

public class FTypeIS_SYSTEM extends FocPredefinedFieldTypeAbstract<FocIS_SYSTEM> {

	@Override
	public String getTypeName() {
		return TYPE_IS_SYSTEM;
	}

	@Override
	public FField newFField(FocDesc focDesc, FocIS_SYSTEM a) {
		FField fld = focDesc.addIsSystemObjectField();
		return fld;
	}

}
