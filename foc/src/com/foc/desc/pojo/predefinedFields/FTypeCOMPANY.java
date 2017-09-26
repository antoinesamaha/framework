package com.foc.desc.pojo.predefinedFields;

import com.foc.annotations.model.predefinedFields.FocCODE;
import com.foc.annotations.model.predefinedFields.FocCOMPANY;
import com.foc.desc.FocDesc;
import com.foc.desc.field.FCompanyField;
import com.foc.desc.field.FField;

public class FTypeCOMPANY extends FocPredefinedFieldTypeAbstract<FocCOMPANY> {

	@Override
	public String getTypeName() {
		return TYPE_COMPANY;
	}

	@Override
	public FField newFField(FocDesc focDesc, FocCOMPANY a) {
		FField fld = new FCompanyField(true, true);
		focDesc.addField(fld);
		return fld;
	}

}
