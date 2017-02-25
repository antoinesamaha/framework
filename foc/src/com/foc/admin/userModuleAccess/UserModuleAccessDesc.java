// EXTERNAL PROPERTIES
// INSTANCE
//    MAIN
//    PANEL
// LIST
// DESCRIPTION

/*
 * Created on 20-May-2005
 */
package com.foc.admin.userModuleAccess;

import com.foc.admin.FocGroupDesc;
import com.foc.admin.FocUserDesc;
import com.foc.business.company.CompanyDesc;
import com.foc.desc.FocDesc;
import com.foc.desc.field.FStringField;
import com.foc.desc.field.FCompanyField;
import com.foc.desc.field.FObjectField;
import com.foc.list.FocList;

/**
 * @author 01Barmaja
 */
public class UserModuleAccessDesc extends FocDesc{
	
	public final static int FLD_USER     =  1;
	public final static int FLD_GROUP    =  2;
	public final static int FLD_MODULE   =  3;

  public static final String DB_TABLE_NAME = "USER_MODULE_ACCESS";
  
  public UserModuleAccessDesc() {
    super(UserModuleAccess.class, FocDesc.NOT_DB_RESIDENT, DB_TABLE_NAME, true);

    addReferenceField();

    FObjectField fObjectFld = new FCompanyField(false, false);
    addField(fObjectFld);

    fObjectFld = new FObjectField("USER", "User", FLD_USER, FocUserDesc.getInstance());
    fObjectFld.setWithList(false);
    fObjectFld.setAllowLoadListFromFocDesc(false);
    addField(fObjectFld);

    fObjectFld = new FObjectField("GROUP", "Group", FLD_GROUP, FocGroupDesc.getInstance());
    fObjectFld.setWithList(false);
    fObjectFld.setAllowLoadListFromFocDesc(false);
    addField(fObjectFld);
    
    FStringField cFld = new FStringField("MODULE", "Module", FLD_MODULE, false, 100);
    addField(cFld);
  }
  
  public FocList newFocList(){
    return super.newFocList();
  }
  
  public static UserModuleAccessDesc getInstance(){
    return (UserModuleAccessDesc) getInstance(DB_TABLE_NAME, UserModuleAccessDesc.class);
  }
}
