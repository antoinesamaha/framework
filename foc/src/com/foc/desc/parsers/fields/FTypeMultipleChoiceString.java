package com.foc.desc.parsers.fields;

import java.lang.reflect.Field;

import com.foc.annotations.model.fields.FocMultipleChoiceString;
import com.foc.desc.field.FField;
import com.foc.desc.field.FMultipleChoiceStringField;

public class FTypeMultipleChoiceString extends FocFieldTypAbstract<FocMultipleChoiceString> {

	@Override
	public String getTypeName() {
		return TYPE_MULTIPLE_CHOICE_STRING;
	}

	@Override
	public FField newFField(Class focObjClass, Field f, FocMultipleChoiceString a) {
//  	fld = new FMultipleChoiceStringField(getName(att), getTitle(att), xmlFocDesc.nextFldID(), isKey(att), getSize(att));
//  	String sameCol = getString(att, ATT_SAME_COLUMN);
//  	if(sameCol == null || sameCol.trim().toLowerCase().equals("true")){
//  		((FMultipleChoiceStringField)fld).setChoicesAreFromSameColumn(xmlFocDesc);
//  	}
//  	xmlFocDesc.addField(fld);
		
		FMultipleChoiceStringField focField = null;
		focField = new FMultipleChoiceStringField(getDBFieldName(f), getFieldTitle(f), FField.NO_FIELD_ID, false, a.size());
		focField.setDBResident(a.dbResident());
		focField.setUseSameColumnValues(a.useColumnValues());
		focField.setAllowOutofListSelection(a.allowNewValues());
		if(a.choices() != null){
			for(String title: a.choices()){
				focField.addChoice(title);
			}
		}
		return focField;
	}

}
