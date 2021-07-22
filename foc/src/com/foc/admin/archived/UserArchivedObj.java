package com.foc.admin.archived;

import com.foc.annotations.model.FocEntity;
import com.foc.annotations.model.fields.FocForeignEntity;
import com.foc.annotations.model.fields.FocInteger;
import com.foc.annotations.model.fields.FocString;
import com.foc.annotations.model.predefinedFields.FocDATE;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;
import com.foc.desc.parsers.ParsedFocDesc;
import com.foc.desc.parsers.pojo.PojoFocObject;

@SuppressWarnings("serial")
@FocEntity
@FocDATE
public class UserArchivedObj extends PojoFocObject {
	public static final String DBNAME = "UserArchivedObj";

	public UserArchivedObj(FocConstructor constr) {
		super(constr);
	}

	@FocForeignEntity(table = "FUSER")
	public static final String FIELD_User = "User";

	@FocString(mandatory = true, size = 50)
	public static final String FIELD_ObjectType = "ObjectType";

	@FocInteger(size = 18)
	public static final String FIELD_ObjectRef = "Object_REF";

	public FocObject getUser_REF() {
		return getPropertyObject(FIELD_User);
	}

	public void setUser_REF(FocObject value) {
		setPropertyObject(FIELD_User, value);
	}
	
	public String getObjectType() {
		return getPropertyString(FIELD_ObjectType);
	}

	public void setObjectType(String value) {
		setPropertyString(FIELD_ObjectType, value);
	}

	public int getObjectRef() {
		return getPropertyInteger(FIELD_ObjectRef);
	}

	public void setObjectRef(int value) {
		setPropertyInteger(FIELD_ObjectRef, value);
	}
	
	public static ParsedFocDesc getFocDesc() {
		return ParsedFocDesc.getInstance(DBNAME);
	}
}
