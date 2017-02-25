package com.foc.admin;

import com.foc.desc.FocDesc;
import com.foc.list.FocList;

/**
 * @author 01Barmaja
 */
public class DocRightsGroupDesc extends FocDesc{
	
	public static final int FLD_DOC_RIGHTS_GROUP_USERS_LIST = 1;
	
  public static final String DB_TABLE_NAME = "DOC_RIGHTS_GROUP";
  
  public DocRightsGroupDesc() {
    super(DocRightsGroup.class, DB_RESIDENT, DB_TABLE_NAME, false);
    
    addReferenceField();
    addNameField();
  }
  
  public FocList newFocList(){
	  FocList list = super.newFocList();
	  list.setDirectlyEditable(false);
	  list.setDirectImpactOnDatabase(true);
	  return list;
	}
  
  public static DocRightsGroupDesc getInstance(){
    return (DocRightsGroupDesc) getInstance(DB_TABLE_NAME, DocRightsGroupDesc.class);
  }
}
