package com.foc.business.calendar;

import java.sql.Date;

import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;

public class Holiday extends FocObject {

	private long startDateTime = 0;
	private long endDateTime   = 0;
	
	public Holiday(FocConstructor constr){
	  super(constr);
	  newFocProperties();
	} 
	
	public void setStartDate(Date date){
		setPropertyDate(HolidayDesc.FLD_START_DATE, date);
	}
	
	public Date getStartDate(){
		return getPropertyDate(HolidayDesc.FLD_START_DATE);
	}

	public void setEndDate(Date date){
		setPropertyDate(HolidayDesc.FLD_END_DATE, date);
	}
	
	public Date getEndDate(){
		return getPropertyDate(HolidayDesc.FLD_END_DATE);
	}	
	
	public void setReason(String reason){
		setPropertyString(HolidayDesc.FLD_REASON, reason);
	}
	
	public String getReason(){
		return getPropertyString(HolidayDesc.FLD_REASON);
	}

	public void recomputeStartEndDatesTime(){
		startDateTime = FCalendar.getTimeAtMidnight(getStartDate());
		endDateTime   = FCalendar.getTimeAtMidnight(getEndDate());
	}
	
	public long getStartDateTime() {
		return startDateTime;
	}

	public long getEndDateTime() {
		return endDateTime;
	}
}
