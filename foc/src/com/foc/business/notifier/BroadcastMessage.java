package com.foc.business.notifier;

import java.sql.Date;
import java.sql.Time;

import com.foc.Globals;
import com.foc.admin.FocUser;
import com.foc.annotations.model.FocEntity;
import com.foc.annotations.model.fields.FocForeignEntity;
import com.foc.annotations.model.fields.FocString;
import com.foc.annotations.model.fields.FocTime;
import com.foc.annotations.model.predefinedFields.FocDATE;
import com.foc.desc.FocConstructor;
import com.foc.desc.parsers.ParsedFocDesc;
import com.foc.desc.parsers.pojo.PojoFocObject;

@FocEntity
@FocDATE
public class BroadcastMessage extends PojoFocObject {

  public static final String DBNAME = "BroadcastMessage";
  
	@FocString(mandatory = true, size = 1000)
	public static final String FIELD_Message = "Message";

	@FocTime()
	public static final String FIELD_Time = "Time";

	@FocForeignEntity(mandatory = true, table = "FUSER")
	public static final String FIELD_User = "User";
  
  public BroadcastMessage(FocConstructor constr) {
		super(constr);
  }
  
	public static ParsedFocDesc getFocDesc() {
		return ParsedFocDesc.getInstance(DBNAME);
	}

	public String getMessage() {
		return getPropertyString(FIELD_Message);
	}

	public void setMessage(String value) {
		setPropertyString(FIELD_Message, value);
	}

	public Time getTime() {
		return getPropertyTime(FIELD_Time);
	}

	public void setTime(Time value) {
		setPropertyTime(FIELD_Time, value);
	}

	public FocUser getUser() {
		return (FocUser) getPropertyObject(FIELD_User);
	}

	public void setUser(FocUser value) {
		setPropertyObject(FIELD_User, value);
	}
}
