package com.foc.gui.dateInterval;

import com.foc.desc.FocDesc;
import com.foc.desc.field.FDateField;
import com.foc.desc.field.FField;

public class StartEndDateDesc extends FocDesc{

  public static final int FLD_FDATE                = 1;
  public static final int FLD_LDATE                = 2;
  public static final int FLD_SUGGESTED_FIRST_DATE = 3;
  public static final int FLD_SUGGESTED_LAST_DATE  = 4;
  
  public StartEndDateDesc() {
    super(StartEndDate.class, FocDesc.NOT_DB_RESIDENT, "START_END_DATE", false);
    setGuiDetailsPanelClass(StartEndDateGuiDetailsPanel.class);
    
    FField fField = addReferenceField();
    
    fField = new FDateField("START_DATE", "Start Date", FLD_FDATE, false);
    addField(fField);
    
    fField = new FDateField("END_DATE", "End Date", FLD_LDATE, false);
    addField(fField);
    
    fField = new FDateField("SUGG_START_DATE", "Suggested Start", FLD_SUGGESTED_FIRST_DATE, false);
    addField(fField);
    
    fField = new FDateField("SUGG_END_DATE", "Suggested End", FLD_SUGGESTED_LAST_DATE, false);
    addField(fField);
  }

  private static StartEndDateDesc focDesc = null;
   
  public static StartEndDateDesc getInstance() {
    if(focDesc == null){
      focDesc = new StartEndDateDesc();
    }
    return focDesc;
  }
}
