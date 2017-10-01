package com.foc.desc.parsers.fields;

import java.lang.reflect.Field;

import com.foc.annotations.model.FocChoice;
import com.foc.annotations.model.fields.FocMultipleChoice;
import com.foc.desc.field.FField;
import com.foc.desc.field.FMultipleChoiceField;

public class FTypeMultipleChoice extends FocFieldTypAbstract<FocMultipleChoice> {

	@Override
	public String getTypeName() {
		return TYPE_MULTIPLE_CHOICE;
	}

	@Override
	public FField newFField(Class focObjClass, Field f, FocMultipleChoice a) {
		FMultipleChoiceField focField = null;
		focField = new FMultipleChoiceField(getDBFieldName(f), getFieldTitle(f), FField.NO_FIELD_ID, false, a.size());
		if(a.choices() != null){
			for(FocChoice c: a.choices()){
				focField.addChoice(c.id(), c.title());
			}
		}
		return focField;
	}

}
