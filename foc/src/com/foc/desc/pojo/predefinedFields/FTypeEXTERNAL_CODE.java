package com.foc.desc.pojo.predefinedFields;

import com.foc.annotations.model.predefinedFields.FocEXTERNAL_CODE;
import com.foc.desc.FocDesc;
import com.foc.desc.field.FField;

public class FTypeEXTERNAL_CODE extends FocPredefinedFieldTypeAbstract<FocEXTERNAL_CODE> {

	@Override
	public String getTypeName() {
		return TYPE_EXTERNAL_CODE;
	}

	@Override
	public FField newFField(FocDesc focDesc, FocEXTERNAL_CODE a) {
		FField fld = focDesc.addExternalCodeField();
		if(a.size() > 0){
  		fld.setSize(a.size());
  	}
		return fld;
	}

}
