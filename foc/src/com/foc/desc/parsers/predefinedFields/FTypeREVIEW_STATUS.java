package com.foc.desc.parsers.predefinedFields;

import com.foc.annotations.model.predefinedFields.FocREVIEW_STATUS;
import com.foc.desc.FocDesc;
import com.foc.desc.field.FField;

public class FTypeREVIEW_STATUS extends FocPredefinedFieldTypeAbstract<FocREVIEW_STATUS> {

	@Override
	public String getTypeName() {
		return TYPE_REVIEW_STATUS;
	}

	@Override
	public FField newFField(FocDesc focDesc, FocREVIEW_STATUS a) {
		FField fld = focDesc.addDescriptionField();		
		return fld;
	}

}
