package com.foc.business.units;

import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;

public class Unit extends FocObject {
	public Unit(FocConstructor constr){
    super(constr);
 	  newFocProperties();
  }
	
	public void dispose(){
		super.dispose();
	}
	
	public void setName(String name){
		setPropertyString(UnitDesc.FLD_NAME, name);
	}
	
	public String getName(){
		return getPropertyString(UnitDesc.FLD_NAME);
	}
	
	public void setSymbol(String symbol){
		setPropertyString(UnitDesc.FLD_SYMBOL, symbol);
	}
	
	public String getSymbole(){
		return getPropertyString(UnitDesc.FLD_SYMBOL);
	}
  
  public double getFactor(){
    return getPropertyDouble(UnitDesc.FLD_FACTOR);
  }
  
  public void setFactor(double factor){
    setPropertyDouble(UnitDesc.FLD_FACTOR, factor);
  }

  public boolean isSecond(){
  	return getName().equals(UnitDesc.TIME_UNIT_SECOND);
  }
  
  public boolean isMinute(){
  	return getName().equals(UnitDesc.TIME_UNIT_MINUTE);
  }
  
  public boolean isHour(){
  	return getName().equals(UnitDesc.TIME_UNIT_HOUR);
  }

  public boolean isDay(){
  	return getName().equals(UnitDesc.TIME_UNIT_DAY);
  }
  
  public boolean isWeek(){
  	return getName().equals(UnitDesc.TIME_UNIT_WEEK);
  }

  public boolean isMonth(){
  	return getName().equals(UnitDesc.TIME_UNIT_MONTH);
  }
  
  public boolean isTimeRelevant(){
  	return isHour() || isMinute();
  }
  
  public Dimension getDimension(){
  	return (Dimension) getPropertyObject(UnitDesc.FLD_DIMENSION);
  }
}
