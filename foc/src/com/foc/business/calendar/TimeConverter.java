package com.foc.business.calendar;

import com.foc.Globals;
import com.foc.business.units.Unit;

public class TimeConverter {
	
  private static boolean errorMessageOnce = false;
	
	public static double convert(Unit unit1, Unit unit2, double d){
		double ret = 0;
		if(unit2.getName().equals(unit1.getName())){
			ret = d;
		}else if(unit1.isMonth() && unit2.isDay()){
			ret = d * 30;
		}else if(unit1.isDay() && unit2.isMonth()){
			ret = d / 30; 
		}else{
			if(!errorMessageOnce){
				Globals.getDisplayManager().popupMessage("Conversion not supported!");
				errorMessageOnce = true;
			}
		}
		return ret;
	}
}
