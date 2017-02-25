package com.foc.db.migration;

import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;
import com.foc.desc.field.FField;

public class MigDirectory extends FocObject {

  public MigDirectory(FocConstructor constr) {
    super(constr);
    newFocProperties();
  }
  
  public String getName(){
    return getPropertyString(FField.FLD_NAME);
  }
  
  public String getDirectoryPath(){
    return getPropertyString(MigDirectoryDesc.FLD_DIR_PATH);
  }
}
