package com.foc.desc.pojo.predefinedFields;

import com.foc.annotations.model.predefinedFields.FocDATE;
import com.foc.desc.FocDesc;
import com.foc.desc.field.FField;

public class FTypeDATE extends FocPredefinedFieldTypeAbstract<FocDATE> {

	@Override
	public String getTypeName() {
		return TYPE_DATE;
	}

	@Override
	public FField newFField(FocDesc focDesc, FocDATE a) {
		FField fld = focDesc.addDateField();
		return fld;
	}

}
