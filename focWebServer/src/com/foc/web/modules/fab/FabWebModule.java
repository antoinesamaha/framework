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

import java.util.Hashtable;

import com.fab.gui.xmlView.XMLViewDefinitionDesc;
import com.fab.model.filter.FilterDefinitionDesc;
import com.fab.model.filter.FilterFieldDefinitionDesc;
import com.fab.model.project.FabProjectDesc;
import com.fab.model.project.FabWorkspaceDesc;
import com.fab.model.table.FabDictionaryGroupDesc;
import com.fab.model.table.FabMultiChoiceSetDesc;
import com.fab.model.table.FabMultipleChoiceDesc;
import com.fab.model.table.FieldDefinitionDesc;
import com.fab.model.table.TableDefinition;
import com.fab.model.table.TableDefinitionDesc;
import com.fab.model.table.TableDefinitionTree;
import com.fab.model.table.underlyingCustomisation.UndCustFieldDesc;
import com.fab.model.table.underlyingCustomisation.UndCustTable;
import com.fab.model.table.underlyingCustomisation.UndCustTableDesc;
import com.fab.parameterSheet.ParameterSheetSelectorDesc;
import com.foc.ConfigInfo;
import com.foc.Globals;
import com.foc.business.printing.PrnLayoutDefinitionDesc;
import com.foc.fluttercodewriter.FlutterCodeWriterMain;
import com.foc.list.FocList;
import com.foc.menuStructure.FocMenuItem;
import com.foc.menuStructure.IFocMenuItemAction;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.FocWebModule;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.menuTree.FVMenuTree;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;

public class FabWebModule extends FocWebModule {
  
  public FabWebModule() {
  	super("APP_BUILDER", "Application builder");
    setAdminConsole(true);
  }

  public void declareXMLViewsInDictionary() {
  	XMLViewDictionary.getInstance().put(
			XMLViewDefinitionDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/fab/XMLViewDefinition_Table.xml", 0, XMLViewDefinition_Table.class.getName());
  	
  	XMLViewDictionary.getInstance().put(
			XMLViewDefinitionDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/fab/XMLViewDefinition_Form.xml", 0, XMLViewDefinition_Form.class.getName());
  	
  	XMLViewDictionary.getInstance().put(
			PrnLayoutDefinitionDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/fab/PrnLayoutDefinition_Table.xml", 0, PrnLayoutDefinition_Table.class.getName());
  	
  	XMLViewDictionary.getInstance().put(
  		PrnLayoutDefinitionDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/fab/PrnLayoutDefinition_Form.xml", 0, PrnLayoutDefinition_Form.class.getName());
  	
  	XMLViewDictionary.getInstance().put(
			ParameterSheetSelectorDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/fab/ParameterSheetSelector_Table.xml", 0, ParameterSheetSelector_Table.class.getName());
  	
    XMLViewDictionary.getInstance().put(
      TableDefinitionDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/fab/TableDefinition_Table.xml", 0, null);

    XMLViewDictionary.getInstance().put(
      TableDefinitionDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TREE,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/fab/TableDefinition_Tree.xml", 0, null);
    
    XMLViewDictionary.getInstance().put(
  		TableDefinitionDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/fab/TableDefinition_Form.xml", 0, FabTableDefinition_Form.class.getName());

    XMLViewDictionary.getInstance().put(
      FieldDefinitionDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/fab/FieldDefinition_Table.xml", 0, null);

    XMLViewDictionary.getInstance().put(
      FieldDefinitionDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/fab/FieldDefinition_Form.xml", 0, FieldDefinition_Form.class.getName());

    XMLViewDictionary.getInstance().put(
      FabMultipleChoiceDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/fab/FabMultipleChoice_Table.xml", 0, null);

    XMLViewDictionary.getInstance().put(
      FabMultipleChoiceDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/fab/FabMultipleChoice_Form.xml", 0, null);

    XMLViewDictionary.getInstance().put(
      FabMultiChoiceSetDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/fab/FabMultiChoiceSet_Table.xml", 0, null);

    XMLViewDictionary.getInstance().put(
      FabMultiChoiceSetDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/fab/FabMultiChoiceSet_Form.xml", 0, null);
    
    XMLViewDictionary.getInstance().put(
      FabWorkspaceDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/fab/FabWorkspace_Table.xml", 0, null);

    XMLViewDictionary.getInstance().put(
      FabWorkspaceDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/fab/FabWorkspace_Form.xml", 0, null);

    XMLViewDictionary.getInstance().put(
      FabProjectDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/fab/FabProject_Table.xml", 0, null);

    XMLViewDictionary.getInstance().put(
      FabProjectDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/fab/FabProject_Form.xml", 0, null);

    XMLViewDictionary.getInstance().put(
      UndCustTableDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/fab/UndCustTable_Table.xml", 0, null);

    XMLViewDictionary.getInstance().put(
      UndCustTableDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/fab/UndCustTable_Form.xml", 0, null);

    XMLViewDictionary.getInstance().put(
      UndCustFieldDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/fab/UndCustField_Table.xml", 0, null);

    XMLViewDictionary.getInstance().put(
      UndCustFieldDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/fab/UndCustField_Form.xml", 0, null);

    XMLViewDictionary.getInstance().put(
      FilterDefinitionDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/fab/FilterDefinition_Table.xml", 0, null);

    XMLViewDictionary.getInstance().put(
      FilterDefinitionDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/fab/FilterDefinition_Form.xml", 0, null);

    XMLViewDictionary.getInstance().put(
      FilterFieldDefinitionDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/fab/FilterFieldDefinition_Table.xml", 0, null);

    XMLViewDictionary.getInstance().put(
      FilterFieldDefinitionDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/fab/FilterFieldDefinition_Form.xml", 0, null);

    XMLViewDictionary.getInstance().put(
      FabDictionaryGroupDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/fab/FabDictionaryGroup_Table.xml", 0, null);

    XMLViewDictionary.getInstance().put(
      FabDictionaryGroupDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/fab/FabDictionaryGroup_Form.xml", 0, null);
  }

  public void menu_FillMenuTree(FVMenuTree menuTree, FocMenuItem fatherMenuItem) {
    FocMenuItem mainMenu = menuTree.pushMenu(null, "FAB_APPLICATION_BUILDER", "Application Builder");

    FocMenuItem menuItem = mainMenu.pushMenu("FAB_TABLE_DEFINITION", "Custom Business Objects (Database Tables)");
    menuItem.setMenuAction(new IFocMenuItemAction() {
      public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
        INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
        FocList focList = TableDefinitionDesc.getList(FocList.LOAD_IF_NEEDED);
        TableDefinitionTree tree = new TableDefinitionTree(focList);
        
        XMLViewKey key = new XMLViewKey(TableDefinitionDesc.getInstance().getStorageName(), XMLViewKey.TYPE_TREE);
        ICentralPanel central = XMLViewDictionary.getInstance().newCentralPanel(mainWindow, key, tree);
        mainWindow.changeCentralPanelContent(central, true);
      }
    });

    menuItem = mainMenu.pushMenu("FAB_MULTIPLE_CHOICE_SET", "Data List Sets (Multiple Choice Fields)");
    menuItem.setMenuAction(new IFocMenuItemAction() {
      public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
         INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
         FocList focList = FabMultiChoiceSetDesc.getList(FocList.LOAD_IF_NEEDED);
         mainWindow.changeCentralPanelContent_ToTableForFocList(focList);
      }
    });

    menuItem = mainMenu.pushMenu("FAB_UND_USER_FIELDS", "Underlying User Fields");
    menuItem.setMenuAction(new IFocMenuItemAction() {
      public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
        INavigationWindow mainWindow = (INavigationWindow) navigationWindow;

				UndCustTable undCustTable = UndCustTable.getInstance();
        
        XMLViewKey key = new XMLViewKey(UndCustTableDesc.getInstance().getStorageName(), XMLViewKey.TYPE_FORM);
        ICentralPanel central = XMLViewDictionary.getInstance().newCentralPanel(mainWindow, key, undCustTable);
        mainWindow.changeCentralPanelContent(central, true);
      }
    });

		menuItem = mainMenu.pushMenu("FAB_PARAMETER_OBJECTS_TABLE", "Parameter Objects/Table");
		menuItem.setMenuAction(new IFocMenuItemAction() {
			public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
				INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
				FocList focList = ParameterSheetSelectorDesc.getInstance().getFocList();
				XMLViewKey key = new XMLViewKey(ParameterSheetSelectorDesc.getInstance().getStorageName(), XMLViewKey.TYPE_TABLE);
				if (key != null) {
					ICentralPanel central = XMLViewDictionary.getInstance().newCentralPanel(mainWindow, key, focList);
					mainWindow.changeCentralPanelContent(central, true);
				}
			}
		});
    
    menuItem = mainMenu.pushMenu("FAB_XML_VIEW_DEFINITION", "XML View Definition");
    menuItem.setMenuAction(new IFocMenuItemAction() {
      public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
        INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
        FocList focList = XMLViewDefinitionDesc.getInstance().getFocList(FocList.LOAD_IF_NEEDED);
        XMLViewKey key = new XMLViewKey(XMLViewDefinitionDesc.getInstance().getStorageName(), XMLViewKey.TYPE_TABLE);
        ICentralPanel central = XMLViewDictionary.getInstance().newCentralPanel(mainWindow, key, focList);
        mainWindow.changeCentralPanelContent(central, true);
      }
    });
    
    menuItem = mainMenu.pushMenu("FAB_PRN_REPORT_DEFINITION", "Jasper Report Definition");
    menuItem.setMenuAction(new IFocMenuItemAction() {
      public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
        INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
        FocList focList = PrnLayoutDefinitionDesc.getInstance().getFocList(FocList.LOAD_IF_NEEDED);
        XMLViewKey key = new XMLViewKey(PrnLayoutDefinitionDesc.getInstance().getStorageName(), XMLViewKey.TYPE_TABLE);
        ICentralPanel central = XMLViewDictionary.getInstance().newCentralPanel(mainWindow, key, focList);
        mainWindow.changeCentralPanelContent(central, true);
      }
    });

    menuItem = mainMenu.pushMenu("FAB_GENERATE_FOCDESC", "Reverse Engineering");
    menuItem.setMenuAction(new IFocMenuItemAction() {
      public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
//        INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
//        FocList focList = PrnLayoutDefinitionDesc.getInstance().getFocList(FocList.LOAD_IF_NEEDED);
//        XMLViewKey key = new XMLViewKey(PrnLayoutDefinitionDesc.getInstance().getStorageName(), XMLViewKey.TYPE_TABLE);
//        ICentralPanel central = XMLViewDictionary.getInstance().newCentralPanel(mainWindow, key, focList);
//        mainWindow.changeCentralPanelContent(central, true);
//        TableDefinition tableDef = TableDefinition.getTableDefinitionForFocDesc(menuItem.getCode());

      	Globals.getApp().getDataSource().command_DataModel2Code();
      	
      	
      }
    });

    if (ConfigInfo.isDevMode()) {
	    menuItem = mainMenu.pushMenu("FAB_FLUTTER_CODE_GEN", "Generate Flutter Code");
	    menuItem.setMenuAction(new IFocMenuItemAction() {
	      public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
	      	FlutterCodeWriterMain main = new FlutterCodeWriterMain();
	      }
	    });
    }
    
    /*
    menuItem = mainMenu.pushMenu("Fab Workspace", "_Fab Workspace");
    menuItem.setMenuAction(new IFocMenuItemAction() {
      public void actionPerformed(Object navigationWindow, FocMenuItem menuItem) {
         INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
         //FocList focList = FabWorkspaceDesc.getList(FocList.LOAD_IF_NEEDED);
         //mainWindow.changeCentralPanelContent_ToTableForFocList(focList);
      }
    });

    menuItem = mainMenu.pushMenu("Fab Project", "_Fab Project");
    menuItem.setMenuAction(new IFocMenuItemAction() {
      public void actionPerformed(Object navigationWindow, FocMenuItem menuItem) {
         INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
         //FocList focList = FabProjectDesc.getList(FocList.LOAD_IF_NEEDED);
         //mainWindow.changeCentralPanelContent_ToTableForFocList(focList);
      }
    });
    
    menuItem = mainMenu.pushMenu("Und Cust Table", "_Und Cust Table");
    menuItem.setMenuAction(new IFocMenuItemAction() {
      public void actionPerformed(Object navigationWindow, FocMenuItem menuItem) {
         INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
         //FocList focList = UndCustTableDesc.getList(FocList.LOAD_IF_NEEDED);
         //mainWindow.changeCentralPanelContent_ToTableForFocList(focList);
      }
    });

    menuItem = mainMenu.pushMenu("Und Cust Field", "_Und Cust Field");
    menuItem.setMenuAction(new IFocMenuItemAction() {
      public void actionPerformed(Object navigationWindow, FocMenuItem menuItem) {
         INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
         //FocList focList = UndCustFieldDesc.getList(FocList.LOAD_IF_NEEDED);
         //mainWindow.changeCentralPanelContent_ToTableForFocList(focList);
      }
    });

    menuItem = mainMenu.pushMenu("Menu Definition", "_Menu Definition");
    menuItem.setMenuAction(new IFocMenuItemAction() {
      public void actionPerformed(Object navigationWindow, FocMenuItem menuItem) {
         INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
         //FocList focList = MenuDefinitionDesc.getList(FocList.LOAD_IF_NEEDED);
         //mainWindow.changeCentralPanelContent_ToTableForFocList(focList);
      }
    });

    menuItem = mainMenu.pushMenu("Filter Definition", "_Filter Definition");
    menuItem.setMenuAction(new IFocMenuItemAction() {
      public void actionPerformed(Object navigationWindow, FocMenuItem menuItem) {
         INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
         //FocList focList = FilterDefinitionDesc.getList(FocList.LOAD_IF_NEEDED);
         //mainWindow.changeCentralPanelContent_ToTableForFocList(focList);
      }
    });

    menuItem = mainMenu.pushMenu("Filter Field Definition", "_Filter Field Definition");
    menuItem.setMenuAction(new IFocMenuItemAction() {
      public void actionPerformed(Object navigationWindow, FocMenuItem menuItem) {
         INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
         //FocList focList = FilterFieldDefinitionDesc.getList(FocList.LOAD_IF_NEEDED);
         //mainWindow.changeCentralPanelContent_ToTableForFocList(focList);
      }
    });

    menuItem = mainMenu.pushMenu("Fab Dictionary Group", "_Fab Dictionary Group");
    menuItem.setMenuAction(new IFocMenuItemAction() {
      public void actionPerformed(Object navigationWindow, FocMenuItem menuItem) {
         INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
         //FocList focList = FabDictionaryGroupDesc.getList(FocList.LOAD_IF_NEEDED);
         //mainWindow.changeCentralPanelContent_ToTableForFocList(focList);
      }
    });
    */
  }
}
