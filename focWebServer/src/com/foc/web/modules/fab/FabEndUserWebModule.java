/*******************************************************************************
 * Copyright 2016 Antoine Nicolas SAMAHA
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
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
          XMLViewDictionary.getInstance().get_CreateIfNeeded_WithValidationSettings(key_Form, tableDef);
        }
        
        if(tableDef.isShowInMenu()){
          XMLViewKey key = new XMLViewKey(tableDef.getName(), XMLViewKey.TYPE_TABLE);
          XMLViewDictionary.getInstance().get_CreateIfNeeded_WithValidationSettings(key, tableDef);
        }
      }
    }

  }

}
