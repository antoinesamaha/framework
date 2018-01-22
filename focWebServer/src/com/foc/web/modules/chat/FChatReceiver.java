package com.foc.web.modules.chat;

import java.sql.Date;
import java.sql.Time;

import com.foc.Globals;
import com.foc.admin.FocUser;
import com.foc.annotations.model.FocEntity;
import com.foc.annotations.model.fields.FocBoolean;
import com.foc.annotations.model.fields.FocDate;
import com.foc.annotations.model.fields.FocForeignEntity;
import com.foc.annotations.model.fields.FocTime;
import com.foc.desc.FocConstructor;
import com.foc.desc.field.FField;
import com.foc.desc.parsers.ParsedFocDesc;
import com.foc.desc.parsers.pojo.PojoFocObject;
import com.foc.property.FProperty;

@SuppressWarnings("serial")
@FocEntity
public class FChatReceiver extends PojoFocObject {
	
	public static final String DBNAME = "FChatReceiver";
	
	@FocForeignEntity(table = "FUSER", cascade = false, cachedList = false)
	public static final String FIELD_Receiver = "Receiver";

	@FocDate()
	public static final String FIELD_ReadDate = "ReadDate";

	@FocTime()
	public static final String FIELD_ReadTime = "ReadTime";

	@FocForeignEntity(table = "FChat", cachedList = false, cascade=true)
	public static final String FIELD_Chat = "Chat";

	@FocBoolean()
	public static final String FIELD_Read = "Read";

	public FChatReceiver(FocConstructor constr) {
		super(constr);
	}
	
	public static ParsedFocDesc getFocDesc() {
		return ParsedFocDesc.getInstance(DBNAME);
	}

	@Override
	public int getPropertyAccessRight(int fieldID) {
  	int access = super.getPropertyAccessRight(fieldID);
  	FField fld = getThisFocDesc() != null ? getThisFocDesc().getFieldByID(fieldID) : null;
  	String fName = fld == null ? fld.getName() : "";
  	if(    fName.equals(FIELD_Receiver)
  			|| fName.equals(FIELD_ReadDate)
  			|| fName.equals(FIELD_ReadTime)) {
  		access = PROPERTY_RIGHT_READ;
  	}
  	return access;
  }

	public FocUser getReceiver() {
		return (FocUser) getPropertyObject(FIELD_Receiver);
	}

	public void setReceiver(FocUser value) {
		setPropertyObject(FIELD_Receiver, value);
	}

	public Date getReadDate() {
		return getPropertyDate(FIELD_ReadDate);
	}

	public void setReadDate(Date value) {
		setPropertyDate(FIELD_ReadDate, value);
	}

	public Time getReadTime() {
		return getPropertyTime(FIELD_ReadTime);
	}

	public void setReadTime(Time value) {
		setPropertyTime(FIELD_ReadTime, value);
	}

	public FChat getChat() {
		return (FChat) getPropertyObject(FIELD_Chat);
	}

	public boolean isRead() {
		return getPropertyBoolean(FIELD_Read);
	}

	public void setRead(boolean value) {
		setPropertyBoolean(FIELD_Read, value);
	}
	
	public void propertyChanged_Read(FProperty property) {
		if(isRead() && property != null && !property.isLastModifiedBySetSQLString()) {
			setReadDate(Globals.getApp().getSystemDate());
			Time time = new Time(Globals.getApp().getSystemDate().getTime());
			setReadTime(time);
		}
	}
}
