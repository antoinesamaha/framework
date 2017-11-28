package com.foc.desc.parsers.predefinedFields;

import com.foc.annotations.model.predefinedFields.FocDEPRECATED;
import com.foc.desc.FocDesc;
import com.foc.desc.field.FField;

public class FTypeDEPRECATED extends FocPredefinedFieldTypeAbstract<FocDEPRECATED> {

	@Override
	public String getTypeName() {
		return TYPE_DEPRECATED;
	}

	@Override
	public FField newFField(FocDesc focDesc, FocDEPRECATED a) {
		FField fld = focDesc.addDeprecatedField();
		return fld;
	}

}
