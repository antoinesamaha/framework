package com.foc.business.division;

import com.foc.desc.FocDesc;
import com.foc.desc.field.FBoolField;
import com.foc.desc.field.FCompanyField;
import com.foc.desc.field.FField;
import com.foc.list.FocList;

public class DivisionDesc extends FocDesc{

	public static final int FLD_NAME           = FField.FLD_NAME;
  public static final int FLD_DESCRIPTION    = FField.FLD_DESCRIPTION;
  public static final int FLD_END_DIVISION = 1;
  
  public static final String DB_NAME = "DIVISION";
  
  public static final String FNAME_END_DIVISION = "END_DIVISION";
  
  public DivisionDesc() {
    super(Division.class, FocDesc.DB_RESIDENT, DB_NAME, true);

    addReferenceField();
    setWithObjectTree();
    
    addNameField();
    addDescriptionField();
    
    FCompanyField compField = new FCompanyField(true, true);
    addField(compField);
    
    FBoolField bFld = new FBoolField(FNAME_END_DIVISION, "End Division", FLD_END_DIVISION, false);
    addField(bFld);
  }
  
  public static FocList getList(int mode) {
  	return getInstance().getFocList(mode);
  }
  
  public FocList newFocList(){
  	return new DivisionList(false);
  }

  public static DivisionDesc getInstance() {
  	return (DivisionDesc) getInstance(DB_NAME, DivisionDesc.class);
  }
}
