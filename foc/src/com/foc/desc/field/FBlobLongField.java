package com.foc.desc.field;

import java.sql.Blob;

import com.foc.Globals;
import com.foc.db.DBManager;
import com.foc.desc.FocObject;
import com.foc.property.FBlobLongProperty;
import com.foc.property.FProperty;

public class FBlobLongField extends FBlobField{

  public FBlobLongField(String name, String title, int id, boolean key) {
    super(name, title, id, key);
    setIncludeInDBRequests(false);
  }
  
  @Override
  public String getCreationString(String name) {
    if (getProvider()== DBManager.PROVIDER_ORACLE){
      return " \"" + name + "\" BLOB";
    }else{
      return " " + name + " LONGBLOB";
    }
  }
  
  @Override
  public FProperty newProperty(FocObject masterObj, Object defaultValue) {
    return new FBlobLongProperty(masterObj, getID(), (Blob)defaultValue);
  }
}
