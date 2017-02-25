package com.foc.db.migration;

import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;
import com.foc.desc.field.FField;

public class MigDataBase extends FocObject {

  public MigDataBase(FocConstructor constr) {
    super(constr);
    newFocProperties();
  }
  
  public String getName(){
    return getPropertyString(FField.FLD_NAME);
  }
  
  public String getDescription(){
    return getPropertyString(MigDataBaseDesc.FLD_DESCRIPTION);
  }
  
  public String getURL(){
    return getPropertyString(MigDataBaseDesc.FLD_URL);
  }

  public String getUserName(){
    return getPropertyString(MigDataBaseDesc.FLD_USER_NAME);
  }
  
  public String getJDBCDriver(){
    return getPropertyString(MigDataBaseDesc.FLD_JDBC_DRIVER);
  }

  public String getPassword(){
    return getPropertyString(MigDataBaseDesc.FLD_PASSWORD);
  }
}
