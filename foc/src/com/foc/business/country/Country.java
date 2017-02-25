package com.foc.business.country;

import com.foc.business.country.region.RegionDesc;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.list.FocListOrder;

@SuppressWarnings("serial")
public class Country extends FocObject implements CountryConst {

  public Country(FocConstructor constr) {
    super(constr);
    newFocProperties();
  }

  public String getCountryName(){
  	return getPropertyString(FLD_COUNTRY_NAME);
  }

  public void setCountryName(String name){
  	setPropertyString(FLD_COUNTRY_NAME, name);
  }

  @Override
	public FocList getListPropertyInitialLoadedList(int fieldID) {
  	FocList list = super.getListPropertyInitialLoadedList(fieldID);
  	if(list == null && fieldID == FLD_REGION_LIST){
  		list = RegionDesc.getList(FocList.LOAD_IF_NEEDED);
  	}
		return list;
	}

	public FocList getRegionList(){
  	FocList list = getPropertyList(FLD_REGION_LIST);
  	if(list != null && list.getListOrder() == null){
  		list.setDirectlyEditable(false);
  		list.setDirectImpactOnDatabase(true);
  		list.setListOrder(new FocListOrder(FLD_REGION_NAME));
  	}
  	return list;
  }
}
