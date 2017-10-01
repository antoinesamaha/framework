package com.foc.desc.parsers.predefinedFields;

import com.foc.annotations.model.predefinedFields.FocNAME;
import com.foc.desc.FocDesc;
import com.foc.desc.field.FField;

public class FTypeNAME extends FocPredefinedFieldTypeAbstract<FocNAME> {

	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}

	@Override
	public FField newFField(FocDesc focDesc, FocNAME a) {
		FField fld = focDesc.addNameField();
		if(a.size() > 0){
  		fld.setSize(a.size());
  	}
		return fld;
	}

}
