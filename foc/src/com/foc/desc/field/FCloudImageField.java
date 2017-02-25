package com.foc.desc.field;

import java.sql.Blob;
import java.sql.Types;

import com.fab.model.table.FieldDefinition;
import com.foc.desc.FocObject;
import com.foc.property.FCloudImageProperty;
import com.foc.property.FProperty;

public class FCloudImageField extends FCloudStorageField{

	public FCloudImageField(String name, String title, int id, boolean key) {
		super(name, title, id, key, FField.NO_FIELD_ID);
	}
	
	public static int SqlType() {
    return Types.BLOB;
  }

  public int getSqlType() {
    return SqlType();
  }
  
  public int getFabType() {
    return FieldDefinition.SQL_TYPE_ID_IMAGE;
  }
  
  @Override
	public FProperty newProperty_ToImplement(FocObject masterObj, Object defaultValue) {
		return new FCloudImageProperty(masterObj, getID(), (Blob) defaultValue);
	}

}
