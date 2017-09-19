package com.foc.desc.pojo.fields;

import java.lang.reflect.Field;

import com.foc.annotations.model.FocChoice;
import com.foc.annotations.model.FocField;
import com.foc.desc.field.FField;
import com.foc.desc.field.FMultipleChoiceField;

public class FTypeMultipleChoice extends FocFieldTypAbstract {

	@Override
	public String getTypeName() {
		return TYPE_MULTIPLE_CHOICE;
	}

	protected int getDefaultSize(){
		return 4;
	}

	@Override
	public FField newFField(Class focObjClass, Field f, FocField fieldAnnotation) {
		FMultipleChoiceField focField = null;
		focField = new FMultipleChoiceField(getDBFieldName(f), getFieldTitle(f), FField.NO_FIELD_ID, false, getSize(fieldAnnotation));
		if(fieldAnnotation.choices() != null){
			for(FocChoice c: fieldAnnotation.choices()){
				focField.addChoice(c.id(), c.title());
			}
		}
		return focField;
	}

}
