package com.foc.web.modules.chat.join;

import com.foc.annotations.model.FocEntity;
import com.foc.annotations.model.FocFilterCondition;
import com.foc.desc.FocConstructor;
import com.foc.desc.parsers.ParsedFocDesc;
import com.foc.desc.parsers.pojo.PojoFocObjectFilterBindedToList;
import com.foc.list.filter.LongCondition;
import com.foc.list.filter.StringCondition;

@SuppressWarnings("serial")
@FocEntity(dbResident = false, filterOnTable = "FChatJoin", 
	filterConditions = { @FocFilterCondition(fieldPath = "C-REF", prefix = "REF", caption = "REF"), 
											 @FocFilterCondition(fieldPath = "C-Date", prefix = "Date", caption = "Date"), 
											 @FocFilterCondition(fieldPath = "C-Time", prefix = "Time", caption = "Time"), 
											 @FocFilterCondition(fieldPath = "C-Sender", prefix = "Sender", caption = "Sender"), 
											 @FocFilterCondition(fieldPath = "C-Message", prefix = "Message", caption = "Message"), 
											 @FocFilterCondition(fieldPath = "C-SubjectTableName", prefix = "SubjectTableName", caption = "SubjectTableName"),
											 @FocFilterCondition(fieldPath = "C-SubjectReference", prefix = "SubjectReference", caption = "SubjectReference"),
											 @FocFilterCondition(fieldPath = "R-Receiver", prefix = "Receiver", caption = "Receiver"), 
											 @FocFilterCondition(fieldPath = "R-Read", prefix = "Read", caption = "Read"), 
											 @FocFilterCondition(fieldPath = "R-ReadDate", prefix = "ReadDate", caption = "ReadDate"), 
											 @FocFilterCondition(fieldPath = "R-ReadTime", prefix = "ReadTime", caption = "ReadTime")
										})

public class FChatJoinFilter extends PojoFocObjectFilterBindedToList {

	public static final String DBNAME = "FChatJoinFilter";
	
	public static final String COND_Date       = "Date";
	public static final String COND_Time       = "Time";
	public static final String COND_Sender     = "Sender";
	public static final String COND_Receiver   = "Receiver";
	public static final String COND_SubjectTableName = "SubjectTableName";
	public static final String COND_SubjectReference = "SubjectReference";

	public FChatJoinFilter(FocConstructor constr) {
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
		LongCondition cond = (LongCondition) findFilterCondition(COND_SubjectReference);
		if (cond != null) {
			cond.setToValue(this, LongCondition.OPERATOR_EQUALS, ref, 0);
		}
	}
	
	public void filterSubject(String tableName, long ref) {
		filterSubjectTable(tableName);
		filterSubjectReference(ref);
	}
}
