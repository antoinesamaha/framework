package com.foc.web.modules.chat.join;

import com.foc.annotations.model.FocEntity;
import com.foc.annotations.model.FocGroupByField;
import com.foc.annotations.model.FocJoin;
import com.foc.annotations.model.FocJoinField;
import com.foc.desc.FocConstructor;
import com.foc.desc.parsers.ParsedFocDesc;
import com.foc.desc.parsers.pojo.PojoFocObject;

@SuppressWarnings("serial")
@FocEntity(allowAdaptDataModel=false,
    joins = { 
			@FocJoin(table = "FChat", alias = "C", fields = { 
					@FocJoinField(sourceFieldName="REF", groupByFormula="MAX"),
					@FocJoinField(sourceFieldName="Sender", groupByFormula="MAX"),
					@FocJoinField(sourceFieldName="Date", groupByFormula="MAX"),
					@FocJoinField(sourceFieldName="Time", groupByFormula="MAX"),
					@FocJoinField(sourceFieldName="Message", groupByFormula="MAX"),
					@FocJoinField(sourceFieldName="SubjectTableName", groupByFormula="MAX"),
					@FocJoinField(sourceFieldName="SubjectReference", groupByFormula="MAX"),
					@FocJoinField(sourceFieldName="SubjectType", groupByFormula="MAX"),
					@FocJoinField(sourceFieldName="SubjectSite", groupByFormula="MAX"),
					@FocJoinField(sourceFieldName="SubjectCode", groupByFormula="MAX")
			}), 
			@FocJoin(table = "FChatReceiver", alias = "R", on="C.REF=R.Chat", type="left", fields = { 
					@FocJoinField(sourceFieldName="REF", groupByFormula="MAX"),
			    @FocJoinField(sourceFieldName="Receiver", groupByFormula="MAX"),
			    @FocJoinField(sourceFieldName="Read", groupByFormula="MAX"),
			    @FocJoinField(sourceFieldName="ReadDate", groupByFormula="MAX"),
					@FocJoinField(sourceFieldName="ReadTime", groupByFormula="MAX")
					})
			},
    groupByFields = {
    		@FocGroupByField(name="C.REF")
    })

public class FChatJoin extends PojoFocObject {

	public static final String DBNAME         = "FChatJoin";
	
	public static final String FNAME_ChatRef  = "C-REF";
	public static final String FNAME_Date     = "C-InvestigationDate";
	public static final String FNAME_ReadDate = "R-ReadDate";	
	public static final String FNAME_SubjectTableName = "C-SubjectTableName";
	public static final String FNAME_SubjectReference = "C-SubjectReference";
	
	public FChatJoin(FocConstructor constr) {
		super(constr);
	}	

	public static ParsedFocDesc getFocDesc() {
		return ParsedFocDesc.getInstance(DBNAME);
	}
	
	public int getChatReference() {
		return getPropertyInteger(FNAME_ChatRef);
	}
	
	public String getSubjectTableName() {
		return getPropertyString(FNAME_SubjectTableName);
	}
	
	public int getSubjectReference() {
		return getPropertyInteger(FNAME_SubjectReference);
	}
}
