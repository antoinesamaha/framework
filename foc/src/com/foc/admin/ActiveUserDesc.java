// EXTERNAL PROPERTIES
// INSTANCE
//    MAIN
//    PANEL
// LIST
// DESCRIPTION

/*
 * Created on 20-May-2005
 */
package com.foc.admin;

import com.foc.business.company.CompanyDesc;
import com.foc.desc.FocDesc;
import com.foc.desc.field.FDateTimeField;
import com.foc.desc.field.FObjectField;
import com.foc.list.FocList;

/**
 * @author 01Barmaja
 */
public class ActiveUserDesc extends FocDesc{
	
	public final static int FLD_USER             =  1;
	public final static int FLD_COMPANY          =  2;
	public final static int FLD_LAST_HEART_BEAT  =  3;
	

  public static final String DB_TABLE_NAME = "ACTIVE_USER";
  
  public ActiveUserDesc() {
    super(ActiveUser.class, FocDesc.NOT_DB_RESIDENT, DB_TABLE_NAME, true);

    addReferenceField();
    
    FObjectField fObjectFld = new FObjectField("USER", "User", FLD_USER, FocUserDesc.getInstance());
    addField(fObjectFld);
    
    fObjectFld = new FObjectField("COMPANY", "Company", FLD_COMPANY, CompanyDesc.getInstance());
    addField(fObjectFld);
    
    FDateTimeField dtf = new FDateTimeField("LAST_HEART_BEAT", "Last Heart Beat", FLD_LAST_HEART_BEAT, false);
    addField(dtf);
  }
  
  public FocList newFocList(){
    return super.newFocList();
  }
  
  public static ActiveUserDesc getInstance(){
    return (ActiveUserDesc) getInstance(DB_TABLE_NAME, ActiveUserDesc.class);
  }
}
