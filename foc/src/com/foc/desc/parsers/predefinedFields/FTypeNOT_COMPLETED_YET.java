package com.foc.desc.parsers.predefinedFields;

import com.foc.annotations.model.predefinedFields.FocNOT_COMPLETED_YET;
import com.foc.desc.FocDesc;
import com.foc.desc.field.FField;

public class FTypeNOT_COMPLETED_YET extends FocPredefinedFieldTypeAbstract<FocNOT_COMPLETED_YET> {

	@Override
	public String getTypeName() {
		return TYPE_NOT_COMPLETED;
	}

	@Override
	public FField newFField(FocDesc focDesc, FocNOT_COMPLETED_YET a) {
		FField fld = focDesc.addNotCompletedField();
		return fld;
	}

}
