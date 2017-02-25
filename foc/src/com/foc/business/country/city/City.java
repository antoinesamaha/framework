package com.foc.business.country.city;

import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;

public class City extends FocObject{

  public City(FocConstructor constr) {
    super(constr);
    newFocProperties();
  }

  public String getCityName(){
  	return getPropertyString(CityDesc.FLD_CITY_NAME);
  }

  public void setCityName(String name){
  	setPropertyString(CityDesc.FLD_CITY_NAME, name);
  }
}
