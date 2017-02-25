package com.foc.db.migration;

import com.foc.menu.FMenuAction;
import com.foc.menu.FMenuItem;
import com.foc.menu.FMenuList;

public class MigrationMenu {
	
  public static FMenuItem addMigrationSourceModeMenu(FMenuList menuList){
    FMenuItem paymentMode = new FMenuItem("Migration Source", 'S', new FMenuAction(MigrationSourceDesc.getInstance(), true));
    menuList.addMenu(paymentMode);
    return paymentMode;
  }
      
  public static void addMigrationMenus(FMenuList list){
  	addMigrationSourceModeMenu(list);
  }
}
