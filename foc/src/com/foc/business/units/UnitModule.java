package com.foc.business.units;

import com.foc.desc.FocModule;
import com.foc.menu.FMenuAction;
import com.foc.menu.FMenuItem;
import com.foc.menu.FMenuList;

public class UnitModule extends FocModule {

  public static FMenuItem addDimensionMenu(FMenuList list){
		FMenuItem dimensionItem = new FMenuItem("Dimension", 'D', new FMenuAction(DimensionDesc.getInstance(), true)); 
    list.addMenu(dimensionItem);
    return dimensionItem;
	}
  
  public static UnitModule module = null;
  public static UnitModule getInstance(){
  	if(module == null){
  		module = new UnitModule();
  	}
  	return module;
  }
  
	@Override
	public void declareFocObjectsOnce() {
		declareFocDescClass(DimensionDesc.class);
		declareFocDescClass(UnitDesc.class);
	}
}
