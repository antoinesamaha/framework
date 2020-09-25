package com.foc.web.modules.restmodule;

import com.foc.annotations.model.FocEntity;
import com.foc.annotations.model.FocFilterCondition;
import com.foc.desc.FocConstructor;
import com.foc.desc.parsers.ParsedFocDesc;
import com.foc.desc.parsers.pojo.PojoFocObjectFilterBindedToList;
import com.foc.list.filter.FocListFilter;

@SuppressWarnings("serial")
@FocEntity(dbResident = false, filterOnTable = "LoginToken", filterLevel = FocListFilter.LEVEL_DATABASE,
	filterConditions = { 
		 @FocFilterCondition(fieldPath = "FocUser", captionProperty = "NAME", prefix = "FocUser", caption ="User"),
		 @FocFilterCondition(fieldPath = "DateTime", prefix = "DateTime", caption = "Date Time"),
		 @FocFilterCondition(fieldPath = "Token", prefix = "Token", caption ="Token"),
		 @FocFilterCondition(fieldPath = "Consumed", prefix = "Consumed", caption ="Consumed"),
		 @FocFilterCondition(fieldPath = "ConsumptionDateTime", prefix = "ConsumptionDateTime", caption ="Consumption Time"),
		 })
public class LoginTokenFilter extends PojoFocObjectFilterBindedToList {
	public static final String DBNAME = "LoginTokenFilter"; 

	
	public LoginTokenFilter(FocConstructor constr) {
		super(constr);
	}
	
	public static ParsedFocDesc getFocDesc() {
		return ParsedFocDesc.getInstance(DBNAME);
	}

}
