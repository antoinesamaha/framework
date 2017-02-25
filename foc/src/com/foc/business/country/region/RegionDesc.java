package com.foc.business.country.region;

import com.foc.business.country.CountryConst;
import com.foc.business.country.CountryDesc;
import com.foc.desc.FocDesc;
import com.foc.desc.field.FStringField;
import com.foc.desc.field.FField;
import com.foc.desc.field.FObjectField;
import com.foc.list.FocList;
import com.foc.list.FocListOrder;

public class RegionDesc extends FocDesc implements CountryConst {
  
  public static final String DB_TABLE_NAME = "COUNTRY_REGION";
  
  public RegionDesc() {
    super(Region.class, FocDesc.DB_RESIDENT, DB_TABLE_NAME, false);
    setGuiBrowsePanelClass(RegionGuiBrowsePanel.class);
    setGuiDetailsPanelClass(RegionGuiDetailsPanel.class);
    FField focFld = addReferenceField();
    
    focFld = new FStringField("REGION", "Region", FLD_REGION_NAME, false, 30);
    focFld.setMandatory(true);
    focFld.setLockValueAfterCreation(true);
    addField(focFld);
    
    FObjectField oFld = new FObjectField("COUNTRY", "Country", FLD_COUNTRY, true, CountryDesc.getInstance(), "COUNTRY_", this, FLD_REGION_LIST);
    oFld.setNullValueMode(FObjectField.NULL_VALUE_ALLOWED_AND_SHOWN);
    oFld.setSelectionList(CountryDesc.getList(FocList.NONE));
    oFld.setComboBoxCellEditor(FLD_COUNTRY_NAME);
    addField(oFld);
  }

  public static FocList getList(int mode){
    return getInstance().getFocList(mode);
  }
  
  @Override
  public FocList newFocList(){
    FocList list = super.newFocList();
    list.setDirectlyEditable(true);
    list.setDirectImpactOnDatabase(false);
    if(list.getListOrder() == null){
      FocListOrder order = new FocListOrder(FLD_REGION_NAME);
      list.setListOrder(order);
    }
    return list;
  }
  
  public static FocDesc getInstance() {
    return getInstance(DB_TABLE_NAME, RegionDesc.class);    
  }
}
