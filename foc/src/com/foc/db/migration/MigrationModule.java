package com.foc.db.migration;

import com.foc.Application;
import com.foc.Globals;
import com.foc.admin.FocVersion;
import com.foc.desc.FocModule;
import com.foc.menu.FMenuList;

public class MigrationModule extends FocModule {
	
	private MigrationModule(){
	}
  
	public void dispose(){
		super.dispose();
	}
	
  public void declare(){
    Application app = Globals.getApp();
    FocVersion.addVersion("Migration", "migration v1.0", 1000);
    app.declareModule(this);      
  }

  public void addApplicationMenu(FMenuList menuList) {
  }

  public void addConfigurationMenu(FMenuList menuList) {
  }
  
  public void afterAdaptDataModel() {
  }

  public void afterApplicationEntry() {
  }

  public void afterApplicationLogin() {
  }

  public void beforeAdaptDataModel() {
  }
  
	public void declareFocObjectsOnce() {
    declareFocDescClass(MigrationSourceDesc.class);
    declareFocDescClass(MigDataBaseDesc.class);
    declareFocDescClass(MigDirectoryDesc.class);
    declareFocDescClass(MigFieldMapDesc.class);
	}

  private static MigrationModule module = null;
  public static MigrationModule getInstance(){
    if(module == null){
      module = new MigrationModule();
    }
    return module;
  }
}
