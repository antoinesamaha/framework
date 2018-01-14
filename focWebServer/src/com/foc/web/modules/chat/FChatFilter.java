package com.foc.web.modules.chat;

import com.foc.annotations.model.FocEntity;
import com.foc.annotations.model.FocFilterCondition;
import com.foc.desc.FocConstructor;
import com.foc.desc.parsers.ParsedFocDesc;
import com.foc.desc.parsers.pojo.PojoFocObjectFilterBindedToList;
import com.foc.list.filter.IntegerCondition;
import com.foc.list.filter.LongCondition;
import com.foc.list.filter.StringCondition;

@SuppressWarnings("serial")
@FocEntity(dbResident = false, filterOnTable = "FChat", 
	filterConditions = { @FocFilterCondition(fieldPath = "REF", prefix = "REF", caption = "REF"), 
											 @FocFilterCondition(fieldPath = "Date", prefix = "Date", caption = "Date"), 
											 @FocFilterCondition(fieldPath = "Time", prefix = "Time", caption = "Time"), 
											 @FocFilterCondition(fieldPath = "Sender", prefix = "Sender", caption = "Sender"), 
											 @FocFilterCondition(fieldPath = "Message", prefix = "Message", caption = "Message"), 
											 @FocFilterCondition(fieldPath = "SubjectTableName", prefix = "SubjectTableName", caption = "SubjectTableName"),
											 @FocFilterCondition(fieldPath = "SubjectReference", prefix = "SubjectReference", caption = "SubjectReference")
										})
public class FChatFilter extends PojoFocObjectFilterBindedToList {

	public static final String DBNAME = "FChatFilter";
	
	public static final String COND_Date       = "Date";
	public static final String COND_Time       = "Time";
	public static final String COND_Sender     = "Sender";
	public static final String COND_Receiver   = "Receiver";
	public static final String COND_SubjectTableName = "SubjectTableName";
	public static final String COND_SubjectReference = "SubjectReference";

	public FChatFilter(FocConstructor constr) {
		super(constr);
	}

	public static ParsedFocDesc getFocDesc() {
		return ParsedFocDesc.getInstance(DBNAME);
	}	
	
	public void filterSubjectTable(String tableName) {
		StringCondition cond = (StringCondition) findFilterCondition(COND_SubjectTableName);
		if (cond != null) {
			cond.setToValue(this, StringCondition.OPERATION_EQUALS, tableName);
		}
	}

	public void filterSubjectReference(long ref) {
		IntegerCondition cond = (IntegerCondition) findFilterCondition(COND_SubjectReference);
		if (cond != null) {
			cond.setToValue(this, IntegerCondition.OPERATOR_EQUALS, ref, 0);
		}
	}
	
	public void filterSubject(String tableName, long ref) {
		filterSubjectTable(tableName);
		filterSubjectReference(ref);
	}
	
	public String getTableName() {
		String tableName = null;
		StringCondition cond = (StringCondition) findFilterCondition(COND_SubjectTableName);
		if (cond != null) {
			if(cond.getOperation(this) == StringCondition.OPERATION_EQUALS) {
				tableName = cond.getText(this);
			}
		}
		return tableName;
	}
	
	public long getSubjectReference() {
		long ref = -1;
		IntegerCondition cond = (IntegerCondition) findFilterCondition(COND_SubjectReference);
		if (cond != null) {
			if(cond.getOperator(this) == IntegerCondition.OPERATOR_EQUALS) {
				ref = (long) cond.getFirstValue(this);
			}
		}
		return ref;
	}
}
