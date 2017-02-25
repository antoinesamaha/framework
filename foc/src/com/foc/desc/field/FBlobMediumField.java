package com.foc.desc.field;

import java.sql.Blob;

import com.foc.Globals;
import com.foc.db.DBManager;
import com.foc.desc.FocObject;
import com.foc.property.FBlobMediumProperty;
import com.foc.property.FProperty;

public class FBlobMediumField extends FBlobField{

  public FBlobMediumField(String name, String title, int id, boolean key) {
    super(name, title, id, key);
    setIncludeInDBRequests(false);
  }
  
  @Override
  public String getCreationString(String name) {
    if (getProvider()== DBManager.PROVIDER_ORACLE){
      return " \"" + name + "\" BLOB";
    }else if (getProvider()== DBManager.PROVIDER_MSSQL){
    	return " " + name + " varbinary";
    }else{
      return " " + name + " MEDIUMBLOB";
    }
  }
  
  @Override
  public FProperty newProperty_ToImplement(FocObject masterObj, Object defaultValue) {
    return new FBlobMediumProperty(masterObj, getID(), (Blob)defaultValue);
  }
  
  @Override
  public FProperty newProperty_ToImplement(FocObject masterObj) {
    return newProperty(masterObj, null);
  }

  /*
  @Override
  public FProperty newProperty(FocObject masterObj, Object defaultValue) {
    return new FBlobMediumProperty(masterObj, getID(), (Blob)defaultValue);
  }
  */
}
