package com.foc.desc.parsers.predefinedFields;

import com.foc.annotations.model.predefinedFields.FocDESCRIPTION;
import com.foc.desc.FocDesc;
import com.foc.desc.field.FField;

public class FTypeDESCRIPTION extends FocPredefinedFieldTypeAbstract<FocDESCRIPTION> {

	@Override
	public String getTypeName() {
		return TYPE_DESCRIPTION;
	}

	@Override
	public FField newFField(FocDesc focDesc, FocDESCRIPTION a) {
		FField fld = focDesc.addDescriptionField();
		if(a.size() > 0){
  		fld.setSize(a.size());
  	}
		return fld;
	}

}
