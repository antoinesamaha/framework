package com.foc.list.filter;

import com.fab.model.table.FieldDefinition;
import com.foc.desc.field.FField;
import com.foc.desc.field.FFieldPath;

public class FilterConditionFactory {
	
	public static FilterCondition newConditionForField(FField field, FFieldPath dateFieldPath, String fieldPrefix){
		FilterCondition condition = null;
		
		switch(field.getFabType()){
		case FieldDefinition.SQL_TYPE_ID_CHAR_FIELD:
		case FieldDefinition.SQL_TYPE_ID_MULTIPLE_CHOICE_FOC_DESC:
		case FieldDefinition.SQL_TYPE_ID_MULTIPLE_CHOICE_STRING_BASED:
			condition = new StringCondition(dateFieldPath, fieldPrefix); 
			break;			
		case FieldDefinition.SQL_TYPE_ID_BOOLEAN:
			condition = new BooleanCondition(dateFieldPath, fieldPrefix);
			break;
		case FieldDefinition.SQL_TYPE_ID_MULTIPLE_CHOICE:
			condition = new MultipleChoiceCondition(dateFieldPath, fieldPrefix);
			break;		
		case FieldDefinition.SQL_TYPE_ID_DATE:
			condition = new DateCondition(dateFieldPath, fieldPrefix);
			break;
		case FieldDefinition.SQL_TYPE_ID_DOUBLE:
			condition = new NumCondition(dateFieldPath, fieldPrefix);
			break;
		case FieldDefinition.SQL_TYPE_ID_OBJECT_FIELD:
			condition = new ObjectCondition(dateFieldPath, fieldPrefix);
			break;			
		case FieldDefinition.SQL_TYPE_ID_INT:
			condition = new IntegerCondition(dateFieldPath, fieldPrefix);
			break;
		case FieldDefinition.SQL_TYPE_ID_LONG:
			condition = new LongCondition(dateFieldPath, fieldPrefix);
			break;			
		}
		
		return condition;
	}
	
}

