package com.foc.business.calendar;

import com.foc.desc.FocObject;
import com.foc.gui.FPanel;
import com.foc.gui.FValidationPanel;

@SuppressWarnings("serial")
public class WeekShiftGuiDetailsPanel extends FPanel {

	private WeekShift weekShift = null;
	public static final int VIEW_SELECTION = 1;

  public WeekShiftGuiDetailsPanel(FocObject c, int view){
  	super("Week Shift", FPanel.FILL_NONE);  	
  	this.weekShift = (WeekShift) c;

  	if(view == VIEW_SELECTION){
  		addField(weekShift, WeekShiftDesc.FLD_NAME, 0, 0);
  	}else{
	   	add(weekShift, WeekShiftDesc.FLD_NAME, 0, 0);
	   	FPanel shiftPanel = new FPanel();
	   	shiftPanel.setBorder("Daily shifts");
	   	
	   	int y = 1;
	   	
	   	shiftPanel.add(weekShift, WeekShiftDesc.FLD_MONDAY_SHIFT, 0, y);
	   	shiftPanel.add(weekShift, WeekShiftDesc.FLD_MONDAY_OVERTIME_FACTOR, 2, y++);
	   	
	   	shiftPanel.add(weekShift, WeekShiftDesc.FLD_TUSEDAY_SHIFT, 0, y);
	   	shiftPanel.add(weekShift, WeekShiftDesc.FLD_TUSEDAY_OVERTIME_FACTOR, 2, y++);
	   	
	   	shiftPanel.add(weekShift, WeekShiftDesc.FLD_WEDNESDAY_SHIFT, 0, y);
	   	shiftPanel.add(weekShift, WeekShiftDesc.FLD_WEDNESDAY_OVERTIME_FACTOR, 2, y++);
	   	
	   	shiftPanel.add(weekShift, WeekShiftDesc.FLD_THURSDAY_SHIFT, 0, y);
	   	shiftPanel.add(weekShift, WeekShiftDesc.FLD_THURSDAY_OVERTIME_FACTOR, 2, y++);
	   	
	   	shiftPanel.add(weekShift, WeekShiftDesc.FLD_FRIDAY_SHIFT, 0, y);
	   	shiftPanel.add(weekShift, WeekShiftDesc.FLD_FRIDAY_OVERTIME_FACTOR, 2, y++);
	   	
	   	shiftPanel.add(weekShift, WeekShiftDesc.FLD_SATURDAY_SHIFT, 0, y);
	   	shiftPanel.add(weekShift, WeekShiftDesc.FLD_SATURDAY_OVERTIME_FACTOR, 2, y++);
	   	
	   	shiftPanel.add(weekShift, WeekShiftDesc.FLD_SUNDAY_SHIFT, 0, y);
	   	shiftPanel.add(weekShift, WeekShiftDesc.FLD_SUNDAY_OVERTIME_FACTOR, 2, y++);
	   	
	   	add(shiftPanel, 0, 1, 2, 1);
	   	
	    FValidationPanel savePanel = showValidationPanel(true);
	    if(savePanel != null){
	      savePanel.addSubject(weekShift);
	    }
  	}
  }
  
  public void dispose(){
  	super.dispose();
  	weekShift = null;
  }
}
