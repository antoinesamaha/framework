package com.foc.business.calendar;

import com.foc.desc.FocObject;
import com.foc.gui.FPanel;
import com.foc.gui.FValidationPanel;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class DayShiftGuiDetailsPanel extends FPanel {

	private DayShift dayShift = null;
	public static final int VIEW_SELECTION = 1; 

  public DayShiftGuiDetailsPanel(FocObject c, int view){
  	super("Day Shift", FPanel.FILL_VERTICAL);  	
  	this.dayShift = (DayShift) c;

  	if(view == VIEW_SELECTION){
  		addField(dayShift, DayShiftDesc.FLD_NAME, 0, 0);
  	}else{
	   	add(dayShift, DayShiftDesc.FLD_NAME, 0, 0);
	  	
	  	FocList list           = dayShift.getShiftList();
	  	FPanel  shiftListPanel = new WorkShiftGuiBrowsePanel(list, FocObject.DEFAULT_VIEW_ID);
	  	add(shiftListPanel, 0, 3, 2, 1);
	
	    FValidationPanel savePanel = showValidationPanel(true);
	    if (savePanel != null) {
	      savePanel.addSubject(dayShift);
	    }
  	}
  }
  
  public void dispose(){
  	super.dispose();
  	dayShift = null;
  }
}
