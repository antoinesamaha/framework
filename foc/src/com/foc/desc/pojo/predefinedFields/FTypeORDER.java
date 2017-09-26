package com.foc.desc.pojo.predefinedFields;

import com.foc.annotations.model.predefinedFields.FocEXTERNAL_CODE;
import com.foc.annotations.model.predefinedFields.FocORDER;
import com.foc.desc.FocDesc;
import com.foc.desc.field.FField;

public class FTypeORDER extends FocPredefinedFieldTypeAbstract<FocORDER> {

	@Override
	public String getTypeName() {
		return TYPE_ORDER;
	}

	@Override
	public FField newFField(FocDesc focDesc, FocORDER a) {
		FField fld = focDesc.addOrderField();
		return fld;
	}

}
