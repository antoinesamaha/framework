package com.foc.ecomerce;

import com.foc.Globals;
import com.foc.desc.FocModule;
import com.foc.menu.FMenuList;

public class ECommerceModule extends FocModule {
	
  private ECommerceModule(){
	}

	public void dispose(){
  }
	
  public void declare(){
  	Globals.getApp().declareModule(this);
  }
	
	public void declareFocObjectsOnce() {
		declareFocDescClass(EComConfigurationDesc.class);
		declareFocDescClass(EComAccountDesc.class);
		declareFocDescClass(EComMaterialDesc.class);
	}
  
  public void addApplicationMenu(FMenuList menuList) {
  }

  public void addConfigurationMenu(FMenuList menuList) {
  }

	@Override
	public void beforeAdaptDataModel() {
	}

  public void afterAdaptDataModel() {
  }

  public void afterApplicationEntry() {
  }

  public void afterApplicationLogin() {
  }

	private static ECommerceModule module = null;
	
	public static ECommerceModule getInstance(){
		if(module == null){
			module = new ECommerceModule();
		}
		return module;
	}
}
