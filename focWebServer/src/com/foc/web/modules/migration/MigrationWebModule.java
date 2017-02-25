package com.foc.web.modules.migration;

import com.foc.db.migration.MigDataBaseDesc;
import com.foc.db.migration.MigDirectoryDesc;
import com.foc.db.migration.MigFieldMapDesc;
import com.foc.db.migration.MigrationSourceDesc;
import com.foc.menuStructure.FocMenuItem;
import com.foc.menuStructure.IFocMenuItemAction;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.FocWebModule;
import com.foc.vaadin.gui.menuTree.FVMenuTree;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;

public class MigrationWebModule extends FocWebModule {
	public MigrationWebModule(){
	  super("MIGRATION", "Migration tools (Import, Export)");
		setAdminConsole(true);
	}
	
	public void declareXMLViewsInDictionary() {
    XMLViewDictionary.getInstance().put(
      MigrationSourceDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/b01/foc/migration/MigrationSource_Table.xml", 0, null);

    XMLViewDictionary.getInstance().put(
      MigrationSourceDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/b01/foc/migration/MigrationSource_Form.xml", 0, null);

    XMLViewDictionary.getInstance().put(
      MigDataBaseDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/b01/foc/migration/MigDataBase_Table.xml", 0, null);

    XMLViewDictionary.getInstance().put(
      MigDataBaseDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/b01/foc/migration/MigDataBase_Form.xml", 0, null);

    XMLViewDictionary.getInstance().put(
      MigDirectoryDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/b01/foc/migration/MigDirectory_Table.xml", 0, null);

    XMLViewDictionary.getInstance().put(
      MigDirectoryDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/b01/foc/migration/MigDirectory_Form.xml", 0, null);

    XMLViewDictionary.getInstance().put(
      MigFieldMapDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/b01/foc/migration/MigFieldMap_Table.xml", 0, null);

    XMLViewDictionary.getInstance().put(
      MigFieldMapDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/b01/foc/migration/MigFieldMap_Form.xml", 0, null);

  }

  public void menu_FillMenuTree(FVMenuTree menuTree, FocMenuItem fatherMenuItem) {
    FocMenuItem mainMenu = menuTree.pushMenu(null, "Migration", "Migration");
    FocMenuItem menuItem = null;    menuItem = mainMenu.pushMenu("Migration Source", "_Migration Source");
    menuItem.setMenuAction(new IFocMenuItemAction() {
      public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
         INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
         //FocList focList = MigrationSourceDesc.getList(FocList.LOAD_IF_NEEDED);
         //mainWindow.changeCentralPanelContent_ToTableForFocList(focList);
      }
    });

    menuItem = mainMenu.pushMenu("Mig Data Base", "_Mig Data Base");
    menuItem.setMenuAction(new IFocMenuItemAction() {
      public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
         INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
         //FocList focList = MigDataBaseDesc.getList(FocList.LOAD_IF_NEEDED);
         //mainWindow.changeCentralPanelContent_ToTableForFocList(focList);
      }
    });

    menuItem = mainMenu.pushMenu("Mig Directory", "_Mig Directory");
    menuItem.setMenuAction(new IFocMenuItemAction() {
      public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
         INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
         //FocList focList = MigDirectoryDesc.getList(FocList.LOAD_IF_NEEDED);
         //mainWindow.changeCentralPanelContent_ToTableForFocList(focList);
      }
    });

    menuItem = mainMenu.pushMenu("Mig Field Map", "_Mig Field Map");
    menuItem.setMenuAction(new IFocMenuItemAction() {
      public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
         INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
         //FocList focList = MigFieldMapDesc.getList(FocList.LOAD_IF_NEEDED);
         //mainWindow.changeCentralPanelContent_ToTableForFocList(focList);
      }
    });
  }
}