package com.fab;

import com.fab.model.table.FieldDefinition;
import com.foc.desc.FocDesc;
import com.foc.desc.field.FField;
import com.foc.desc.field.FMultipleChoiceField;

public interface IFabExtender {
	public void   predefinedFields_addChoices(FMultipleChoiceField mFld);
	public FField predefinedFields_addField(FocDesc focDesc, FField field, int predefinedType, FieldDefinition fieldDefinition);
	public void   predefinedFields_fillDefaultValues(int predefinedType, FieldDefinition fieldDefinition);
}
