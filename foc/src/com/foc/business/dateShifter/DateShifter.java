package com.foc.business.dateShifter;

import java.util.Calendar;
import java.util.Date;

import com.foc.Globals;
import com.foc.business.calendar.FCalendar;
import com.foc.desc.FocObject;
import com.foc.shared.dataStore.IFocData;

public class DateShifter implements IFocData {

	private DateShifterDesc dateShifterDesc = null;
	private FocObject focObject = null;
	
	public DateShifter(FocObject focObject, DateShifterDesc dateShifterDesc) {
		this.focObject       = focObject;
		this.dateShifterDesc = dateShifterDesc;
	}
	
	public void dispose(){
		focObject       = null;
		dateShifterDesc = null;
	}
	
	public void adjustDate(){
		if(!isSpecificDate()){
			//Cal will hold the Date
			Calendar cal = FCalendar.getInstanceOfJavaUtilCalandar();
			
			int currentDayOfMonth = -1;
			int currentMonth      = -1;
			int currentYear       = -1;
			
			Date systemDate = Globals.getApp().getSystemDate();
			if(systemDate != null){
				cal.setTime(systemDate);
				currentDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
				currentMonth      = cal.get(Calendar.MONTH);
				currentYear       = cal.get(Calendar.YEAR);
			}			
			
			cal.set(Calendar.DAY_OF_MONTH, 1);
			
			//Start by computing the year			
			int year = getYear();
			boolean yearIsShifter = year < 1800; 
			if(yearIsShifter){
				int currYear = currentYear;
				cal.set(Calendar.YEAR, currYear + year);
			}else{
				cal.set(Calendar.YEAR, year);
			}

			//Compute the month			
			int month = getMonth();
			if(month == DateShifterDesc.MONTH_KEY_CURRENT_MONTH){
				month = currentMonth;
			}
			cal.set(Calendar.MONTH, month);
			cal.add(Calendar.MONTH, getMonthShift());			
			
			//Compute the day
			int day = getDay();
			if(day == DateShifterDesc.DAY_KEY_END_OF_MONTH){
				day = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			}else if(day == DateShifterDesc.DAY_KEY_CURRENT_DAY_OF_MONTH){
				day = currentDayOfMonth;
				if(day > cal.getActualMaximum(Calendar.DAY_OF_MONTH)){
					day = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
				}
			}
			cal.set(Calendar.DAY_OF_MONTH, day);
			cal.add(Calendar.DATE, getDayShift());

			java.sql.Date sqlDate = new java.sql.Date(cal.getTime().getTime());
			getFocObject().setPropertyDate(getDateShifterDesc().getDateFieldID(), sqlDate);
			Globals.logString("-- Setting the Datefield: "+getFocObject().getThisFocDesc().getFieldByID(getDateShifterDesc().getDateFieldID()).getName()+" Value:"+sqlDate.toString());
		}
	}
	
	public int getYear(){
		return getFocObject() != null ? getFocObject().getPropertyInteger(getFieldsShift() + DateShifterDesc.FLD_YEAR) : null;
	}
	
	public void setYear(int value){
		if(getFocObject() != null){
			getFocObject().setPropertyInteger(getFieldsShift() + DateShifterDesc.FLD_YEAR, value);
		}
	} 
	
	public int getMonth(){
		return getFocObject() != null ? getFocObject().getPropertyInteger(getFieldsShift() + DateShifterDesc.FLD_MONTH) : null;
	}
	
	public void setMonth(int value){
		if(getFocObject() != null){
			getFocObject().setPropertyInteger(getFieldsShift() + DateShifterDesc.FLD_MONTH, value);
		}
	}
	
	public int getMonthShift(){
		return getFocObject() != null ? getFocObject().getPropertyInteger(getFieldsShift() + DateShifterDesc.FLD_MONTH_SHIFT) : null;
	}
	
	public void setMonthShift(int value){
		if(getFocObject() != null){
			getFocObject().setPropertyInteger(getFieldsShift() + DateShifterDesc.FLD_MONTH_SHIFT, value);
		}
	}
	
	public int getDay(){
		return getFocObject() != null ? getFocObject().getPropertyInteger(getFieldsShift() + DateShifterDesc.FLD_DAY) : null;
	}
	
	public void setDay(int value){
		if(getFocObject() != null){
			getFocObject().setPropertyInteger(getFieldsShift() + DateShifterDesc.FLD_DAY, value);
		}
	}
	
	public int getDayShift(){
		return getFocObject() != null ? getFocObject().getPropertyInteger(getFieldsShift() + DateShifterDesc.FLD_DAY_SHIFT) : null;
	}
	
	public void setDayShift(int value){
		if(getFocObject() != null){
			getFocObject().setPropertyInteger(getFieldsShift() + DateShifterDesc.FLD_DAY_SHIFT, value);
		}
	}
	
	public boolean isSpecificDate(){
		boolean specific = false;
		if(getFocObject() != null){
			specific = getFocObject().getPropertyInteger(getFieldsShift() + DateShifterDesc.FLD_IS_SPECIFIC_DATE) == 1;		
		}
		return specific;
	}
	
	public void setSpecificDate(boolean value){
		if(getFocObject() != null){
			getFocObject().setPropertyBoolean(getFieldsShift() + DateShifterDesc.FLD_IS_SPECIFIC_DATE, value);
		}
	}
	
	/*public int getDayShift(){
		return getFocObject() != null ? getFocObject().getPropertyInteger(getFieldsShift() + DateShifterDesc.FLD_DAY_SHIFT) : null;
	}
	
	public void setDayShift(int value){
		if(getFocObject() != null){
			getFocObject().setPropertyInteger(getFieldsShift() + DateShifterDesc.FLD_DAY_SHIFT, value);
		}
	}*/
	
	private int getFieldsShift(){
		return getDateShifterDesc() != null ? getDateShifterDesc().getFieldsShift() : 0;
	}
	
	private FocObject getFocObject(){
		return focObject;
	}

	public DateShifterDesc getDateShifterDesc() {
		return dateShifterDesc;
	}

	@Override
	public boolean iFocData_isValid() {
		return true;
	}

	@Override
	public boolean iFocData_validate() {
		return false;
	}

	@Override
	public void iFocData_cancel() {
	}

	@Override
	public IFocData iFocData_getDataByPath(String path) {
		return (getFocObject() != null && getDateShifterDesc() != null) ? getFocObject().iFocData_getDataByPath(getDateShifterDesc().adjustFieldName(path)) : null;
	}

	@Override
	public Object iFocData_getValue() {
		return null;
	}
}
