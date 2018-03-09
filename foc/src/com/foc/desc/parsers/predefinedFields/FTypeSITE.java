package com.foc.desc.parsers.predefinedFields;

import com.foc.annotations.model.predefinedFields.FocSITE;
import com.foc.business.workflow.WFSiteDesc;
import com.foc.desc.FocDesc;
import com.foc.desc.field.FField;

public class FTypeSITE extends FocPredefinedFieldTypeAbstract<FocSITE> {

	@Override
	public String getTypeName() {
		return TYPE_SITE;
	}

	@Override
	public FField newFField(FocDesc focDesc, FocSITE a) {
		FField fld = WFSiteDesc.newSiteField(focDesc, FField.FNAME_SITE, focDesc.nextFldID(), FField.NO_FIELD_ID);
		focDesc.addField(fld);
		return fld;
	}

}
