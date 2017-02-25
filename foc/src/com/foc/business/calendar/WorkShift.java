// CONVERSION IN TIME UNITS

package com.foc.business.calendar;

import java.sql.Time;

import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;

public class WorkShift extends FocObject {
	
	public WorkShift(FocConstructor constr){
	  super(constr);
		newFocProperties();
	} 
	
	public void dispose(){
		super.dispose();
	}

	public Time getStartTime(){
		return getPropertyTime(WorkShiftDesc.FLD_START_TIME); 
	}
	
	public void setStartTime(Time startTime){
		setPropertyTime(WorkShiftDesc.FLD_START_TIME, startTime); 
	}
	
	public void setStartTime(String startTime){
		setPropertyTime(WorkShiftDesc.FLD_START_TIME, startTime); 
	}

	public Time getEndTime(){
		return getPropertyTime(WorkShiftDesc.FLD_END_TIME); 
	}
	
	public void setEndTime(Time endTime){
		setPropertyTime(WorkShiftDesc.FLD_END_TIME, endTime); 
	}
	
	public void setEndTime(String endTime){
		setPropertyTime(WorkShiftDesc.FLD_END_TIME, endTime); 
	}
	
	public double getNbrOfHours(){
		double num   = getEndTime().getTime() - getStartTime().getTime();
		double denom = 3600000;//1000 * 60 * 60;
		return num / denom;
	}
}