package com.fab.model.table.underlyingCustomisation;

import com.fab.model.table.FieldDefinitionDesc;
import com.foc.desc.field.FBoolField;
import com.foc.desc.field.FStringField;
import com.foc.list.FocList;

public class UndCustFieldDesc extends FieldDefinitionDesc {
	
	public static final String DB_TABLE_NAME = "UNDERLYING_CUST_FIELD";
	
	public static final int FLD_NOT_PHYSICAL_DIFFERENCE = FLD_SHIFT_FOR_UNDERLYING_FIELDS + 1; 
	public static final int FLD_IDENTIFICATION_PREFIX   = FLD_SHIFT_FOR_UNDERLYING_FIELDS + 2;
	public static final int FLD_IDENTIFICATION_SUFFIX   = FLD_SHIFT_FOR_UNDERLYING_FIELDS + 3;
	
	public UndCustFieldDesc(){
		super(UndCustField.class, UndCustTableDesc.getInstance(), UndCustTableDesc.getList(FocList.NONE), DB_TABLE_NAME);
		setGuiDetailsPanelClass(UndCustFieldGuiDetailsPanel.class);
		setGuiBrowsePanelClass(UndCustFieldGuiBrowsePanel.class);
		
		FBoolField bFld = new FBoolField("NOT_PHYSICAL_DIFFERENCE", "NOT_PHYSICAL_DIFF", FLD_NOT_PHYSICAL_DIFFERENCE, false);
		addField(bFld);
		
		FStringField cFld = new FStringField("IDENTIFICATION_PREFIX", "Identification Prefix", FLD_IDENTIFICATION_PREFIX, false, 30);
		addField(cFld);
		
		cFld = new FStringField("IDENTIFICATION_SUFFIX", "Identification Suffix", FLD_IDENTIFICATION_SUFFIX, false, 30);
		addField(cFld);
	}
	
  //ooooooooooooooooooooooooooooooooooo
  // oooooooooooooooooooooooooooooooooo
  // SINGLE INSTANCE
  // oooooooooooooooooooooooooooooooooo
  // oooooooooooooooooooooooooooooooooo

  private static UndCustFieldDesc focDesc = null;
  
  public static UndCustFieldDesc getInstance() {
    if(focDesc == null){
      focDesc = new UndCustFieldDesc();
    }
    return focDesc;
  }
}
