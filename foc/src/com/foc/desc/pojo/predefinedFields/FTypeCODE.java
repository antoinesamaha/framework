package com.foc.desc.pojo.predefinedFields;

import com.foc.annotations.model.predefinedFields.FocCODE;
import com.foc.desc.FocDesc;
import com.foc.desc.field.FField;

public class FTypeCODE extends FocPredefinedFieldTypeAbstract<FocCODE> {

	@Override
	public String getTypeName() {
		return TYPE_CODE;
	}

	@Override
	public FField newFField(FocDesc focDesc, FocCODE a) {
		FField fld = focDesc.addCodeField();
		if(a.size() > 0){
  		fld.setSize(a.size());
  	}
		return fld;
	}

}
