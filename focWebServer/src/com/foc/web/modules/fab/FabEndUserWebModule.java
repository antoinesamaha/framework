package com.foc.web.modules.fab;

import com.fab.model.FabMain;
import com.fab.model.table.TableDefinition;
import com.fab.model.table.TableDefinitionDesc;
import com.foc.list.FocList;
import com.foc.menuStructure.FocMenuItem;
import com.foc.menuStructure.IFocMenuItemAction;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.FocWebModule;
import com.foc.vaadin.gui.menuTree.FVMenuTree;
import com.foc.vaadin.gui.menuTree.FocMenuItemAbstractAction_WithAddCommand;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;

public class FabEndUserWebModule extends FocWebModule {
  
  public FabEndUserWebModule() {
    super("APP_BUILDER_END_USER", "Application builder - End user");
  }

  public void declareXMLViewsInDictionary() {
  }

  public void menu_FillMenuTree(FVMenuTree menuTree, FocMenuItem fatherMenuItem) {
    FocMenuItem mainMenu1 = menuTree.pushRootMenu("Custom documents", "Custom documents");

    mainMenu1.setMenuAction(new FocMenuItemAbstractAction_WithAddCommand(mainMenu1, "Add Custom Document") {
      
      @Override
      public FocList getFocList() {
        return TableDefinitionDesc.getList(FocList.LOAD_IF_NEEDED);
      }
    });
    
    FocMenuItemAbstractAction_WithAddCommand menuAction = (FocMenuItemAbstractAction_WithAddCommand) mainMenu1.getMenuAction();
    menuAction.setPopup(false);
    
    FocList listOfTables = TableDefinitionDesc.getList(FocList.LOAD_IF_NEEDED);
    for(int i=0; i<listOfTables.size(); i++){
      TableDefinition tableDef = (TableDefinition) listOfTables.getFocObject(i);
      if(tableDef != null){
        if(tableDef.isShowInMenu()){
          FocMenuItem menuItem1 = mainMenu1.pushMenu(tableDef.getName(), tableDef.getTitle().isEmpty() ? tableDef.getName() : tableDef.getTitle());
          menuItem1.setMenuAction(new IFocMenuItemAction() {
            public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
              INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
              
              TableDefinition tableDef = TableDefinition.getTableDefinitionForFocDesc(menuItem.getCode());
              if(tableDef != null){
                FocList focList = FabMain.getFocList(tableDef.getName());
                if(focList != null){
                  focList.loadIfNotLoadedFromDB();
                  focList.setOrderByKeyFields();
                  
                  mainWindow.changeCentralPanelContent_ToTableForFocList(focList);
                }
              }
            }
          });
        }
        
        if(tableDef.isShowInMenu_Descendent()){
          XMLViewKey key_Form = new XMLViewKey(tableDef.getName(), XMLViewKey.TYPE_FORM);
          XMLViewDictionary.getInstance().get_CreateIfNeeded_WithValidationSettings(key_Form);
        }
        
        if(tableDef.isShowInMenu()){
          XMLViewKey key = new XMLViewKey(tableDef.getName(), XMLViewKey.TYPE_TABLE);
          XMLViewDictionary.getInstance().get_CreateIfNeeded_WithValidationSettings(key);
        }
      }
    }

  }

}