package com.foc.web.modules.restmodule;

import java.sql.Date;

import com.foc.admin.FocUser;
import com.foc.annotations.model.FocEntity;
import com.foc.annotations.model.fields.FocBoolean;
import com.foc.annotations.model.fields.FocDateTime;
import com.foc.annotations.model.fields.FocForeignEntity;
import com.foc.annotations.model.fields.FocString;
import com.foc.desc.FocConstructor;
import com.foc.desc.parsers.ParsedFocDesc;
import com.foc.desc.parsers.pojo.PojoFocDesc;
import com.foc.desc.parsers.pojo.PojoFocObject;

@FocEntity
public class LoginToken extends PojoFocObject {
	
	public static final String DBNAME = "LoginToken";

	public static int TOKEN_LENGTH = 20;
	
	@FocForeignEntity(table = "FUSER")
	public static final String FIELD_FocUser = "FocUser";

	@FocDateTime()
	public static final String FIELD_DateTime = "DateTime";
	
	@FocString(size = 50)
	public static final String FIELD_Token = "Token";

	@FocBoolean()
	public static final String FIELD_Consumed = "Consumed";

	@FocDateTime()
	public static final String FIELD_ConsumptionDateTime = "ConsumptionDateTime";
	
	public LoginToken(FocConstructor constr) {
		super(constr);
	}

	public static PojoFocDesc getFocDesc() {
		return (PojoFocDesc) ParsedFocDesc.getInstance(DBNAME);
	}

	public String getToken() {
		return getPropertyString(FIELD_Token);
	}

	public void setToken(String value) {
		setPropertyString(FIELD_Token, value);
	}

	public boolean getConsumed() {
		return getPropertyBoolean(FIELD_Consumed);
	}

	public void setConsumed(boolean value) {
		setPropertyBoolean(FIELD_Consumed, value);
	}

	public FocUser getFocUser() {
		return (FocUser) getPropertyObject(FIELD_FocUser);
	}

	public void setFocUser(FocUser value) {
		setPropertyObject(FIELD_FocUser, value);
	}

	public Date getDateTime() {
		return getPropertyDate(FIELD_DateTime);
	}

	public void setDateTime(Date value) {
		setPropertyDate(FIELD_DateTime, value);
	}

	public Date getConsumptionDateTime() {
		return getPropertyDate(FIELD_ConsumptionDateTime);
	}

	public void setConsumptionDateTime(Date value) {
		setPropertyDate(FIELD_ConsumptionDateTime, value);
	}
	
}