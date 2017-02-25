package com.foc.business.country.region;

import com.foc.business.country.CountryConst;
import com.foc.business.country.city.CityDesc;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.list.FocListOrder;

public class Region extends FocObject implements CountryConst {

  public Region(FocConstructor constr) {
    super(constr);
    newFocProperties();
  }

  public String getRegionName(){
  	return getPropertyString(RegionDesc.FLD_REGION_NAME);
  }

  public void setRegionName(String name){
  	setPropertyString(RegionDesc.FLD_REGION_NAME, name);
  }
  
  @Override
	public FocList getListPropertyInitialLoadedList(int fieldID) {
  	FocList list = super.getListPropertyInitialLoadedList(fieldID);
  	if(list == null && fieldID == FLD_CITY_LIST){
  		list = CityDesc.getList(FocList.LOAD_IF_NEEDED);
  	}
		return list;
	}

	public FocList getCityList(){
  	FocList list = getPropertyList(FLD_CITY_LIST);
  	if(list != null && list.getListOrder() == null){
  		list.setDirectlyEditable(true);
  		list.setDirectImpactOnDatabase(false);
  		list.setListOrder(new FocListOrder(FLD_CITY_NAME));
  	}
  	return list;
  }
}
