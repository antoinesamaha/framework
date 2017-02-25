package com.foc.desc.dataModelTree;

import com.foc.desc.FocDesc;
import com.foc.desc.field.FDescFieldStringBased;
import com.foc.desc.field.FIntField;

public class DataModelEntryDesc extends FocDesc {
  public static final int FLD_FOC_DESC  = 1;
  public static final int FLD_MAX_LEVEL = 2;
  
  public DataModelEntryDesc() {
    super(DataModelEntry.class, FocDesc.NOT_DB_RESIDENT, "DM_ENTRY", false);
    
    FDescFieldStringBased descFld = new FDescFieldStringBased("OBJECT", "Initial object", FLD_FOC_DESC, false);
    descFld.fillWithAllDeclaredFocDesc();
    addField(descFld);
    
    FIntField focFld = new FIntField("MAX_LEVEL", "maximum level", FLD_MAX_LEVEL, false, 50);
    addField(focFld);
  }
  
	protected void afterConstruction(){
		FDescFieldStringBased descFld = (FDescFieldStringBased)getFieldByID(FLD_FOC_DESC);
		if(descFld != null){
			descFld.fillWithAllDeclaredFocDesc();
		}
	}
  
  // ooooooooooooooooooooooooooooooooooo
  // oooooooooooooooooooooooooooooooooo
  // SINGLE INSTANCE
  // oooooooooooooooooooooooooooooooooo
  // oooooooooooooooooooooooooooooooooo
  
  private static DataModelEntryDesc focDesc = null;
  public static DataModelEntryDesc getInstance() {
    if (focDesc == null){
      focDesc = new DataModelEntryDesc();
    }
    return focDesc;
  }
}
