package com.foc.desc.parsers.predefinedFields;

import com.foc.annotations.model.predefinedFields.FocREVIEW_COMMENT;
import com.foc.desc.FocDesc;
import com.foc.desc.field.FField;

public class FTypeREVIEW_COMMENT extends FocPredefinedFieldTypeAbstract<FocREVIEW_COMMENT> {

	@Override
	public String getTypeName() {
		return TYPE_REVIEW_COMMENT;
	}

	@Override
	public FField newFField(FocDesc focDesc, FocREVIEW_COMMENT a) {
		FField fld = focDesc.addDescriptionField();
		if(a.size() > 0){
  		fld.setSize(a.size());
  	}
		return fld;
	}

}
