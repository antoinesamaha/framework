package com.foc.business.calendar;

import com.foc.desc.FocDesc;
import com.foc.desc.field.FObjectField;
import com.foc.desc.field.FTimeField;
import com.foc.list.FocList;

public class WorkShiftDesc extends FocDesc {
	public static final int FLD_START_TIME = 1;
	public static final int FLD_END_TIME   = 2;
	public static final int FLD_DAY_SHIFT  = 4;
		
	public WorkShiftDesc(){
		super(WorkShift.class, FocDesc.DB_RESIDENT, "WORK_SHIFT", false);
		setGuiBrowsePanelClass(WorkShiftGuiBrowsePanel.class);	
		
    addReferenceField();

    FObjectField objFld = new FObjectField("DAY_SHIFT", "Day Shift", FLD_DAY_SHIFT, false, DayShiftDesc.getInstance(), "DAY_SHIFT_", this, DayShiftDesc.FLD_SHIFT_LIST);
    objFld.setMandatory(true);
    objFld.setSelectionList(DayShiftDesc.getList(FocList.NONE));
    objFld.setDisplayField(DayShiftDesc.FLD_NAME);
    objFld.setComboBoxCellEditor(DayShiftDesc.FLD_NAME);
    addField(objFld);
    
    FTimeField timeFld = new FTimeField("START_TIME", "Start time", FLD_START_TIME, false);
    addField(timeFld);

    timeFld = new FTimeField("END_TIME", "End time", FLD_END_TIME, false);
    addField(timeFld);
	}
	
	//ooooooooooooooooooooooooooooooooooo
  // oooooooooooooooooooooooooooooooooo
  // SINGLE INSTANCE
  // oooooooooooooooooooooooooooooooooo
  // oooooooooooooooooooooooooooooooooo

  private static FocDesc focDesc = null;
  
  public static FocDesc getInstance() {
  	if(focDesc == null){
  		focDesc = new WorkShiftDesc();
  	}
	  return focDesc;
	}
}
