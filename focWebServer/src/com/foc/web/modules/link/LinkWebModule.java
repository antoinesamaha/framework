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
package com.foc.web.modules.link;

import com.foc.admin.FocUserDesc;
import com.foc.link.FocLinkInRightsDesc;
import com.foc.link.FocLinkOutBoxDesc;
import com.foc.link.FocLinkOutRightsDesc;
import com.foc.list.FocList;
import com.foc.menuStructure.FocMenuItem;
import com.foc.menuStructure.IFocMenuItemAction;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.FocWebModule;
import com.foc.vaadin.gui.menuTree.FVMenuTree;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;

public class LinkWebModule extends FocWebModule {

	public static final String MODULE_NAME = "LINK_MODULE";
	public static final String CTXT_POST   = "CTXT_POST";
	
	public LinkWebModule() {
		super(MODULE_NAME, "Link");
		setAdminConsole(false);
	}

	@Override
	public void declareXMLViewsInDictionary() {
    XMLViewDictionary.getInstance().put(
        FocLinkOutBoxDesc.getInstance().getStorageName(),
        XMLViewKey.TYPE_TABLE,
        XMLViewKey.CONTEXT_DEFAULT,
        XMLViewKey.VIEW_DEFAULT,
        "/xml/com/foc/link/FocLink_Out_Log_Table.xml", 0, null);

    XMLViewDictionary.getInstance().put(
        FocLinkOutBoxDesc.getInstance().getStorageName(),
        XMLViewKey.TYPE_FORM,
        XMLViewKey.CONTEXT_DEFAULT,
        XMLViewKey.VIEW_DEFAULT,
        "/xml/com/foc/link/FocLink_Out_Log_Form.xml", 0, null);
    
    XMLViewDictionary.getInstance().put(
        FocLinkOutBoxDesc.getInstance().getStorageName(),
        XMLViewKey.TYPE_FORM,
        CTXT_POST,
        XMLViewKey.VIEW_DEFAULT,
        "/xml/com/foc/link/FocLink_Out_Log_Post_Form.xml", 0, FocLink_Out_Log_Post_Form.class.getName());
    
    XMLViewDictionary.getInstance().put(
        FocUserDesc.getInstance().getStorageName(),
        XMLViewKey.TYPE_TABLE,
        CTXT_POST,
        XMLViewKey.VIEW_DEFAULT,
        "/xml/com/foc/link/FocLink_Out_Log_User_Selection_Table.xml", 0, null);
    
    XMLViewDictionary.getInstance().put(
        FocLinkOutRightsDesc.getInstance().getStorageName(),
        XMLViewKey.TYPE_TABLE,
        XMLViewKey.CONTEXT_DEFAULT,
        XMLViewKey.VIEW_DEFAULT,
        "/xml/com/foc/link/FocLinkOutRights_Table.xml", 0, null);

    XMLViewDictionary.getInstance().put(
    		FocLinkOutRightsDesc.getInstance().getStorageName(),
        XMLViewKey.TYPE_FORM,
        XMLViewKey.CONTEXT_DEFAULT,
        XMLViewKey.VIEW_DEFAULT,
        "/xml/com/foc/link/FocLinkOutRights_Form.xml", 0, FocLink_Out_Rights_Form.class.getName());
    
    XMLViewDictionary.getInstance().put(
        FocLinkInRightsDesc.getInstance().getStorageName(),
        XMLViewKey.TYPE_TABLE,
        XMLViewKey.CONTEXT_DEFAULT,
        XMLViewKey.VIEW_DEFAULT,
        "/xml/com/foc/link/FocLinkInRights_Table.xml", 0, null);

    XMLViewDictionary.getInstance().put(
    		FocLinkInRightsDesc.getInstance().getStorageName(),
        XMLViewKey.TYPE_FORM,
        XMLViewKey.CONTEXT_DEFAULT,
        XMLViewKey.VIEW_DEFAULT,
        "/xml/com/foc/link/FocLinkInRights_Form.xml", 0, null);
		
	}

	@Override
	public void menu_FillMenuTree(FVMenuTree menuTree, FocMenuItem fatherMenuITem) {
		FocMenuItem mainMenu = menuTree.pushRootMenu("Link", "Link");
		
		FocMenuItem menuItem = mainMenu.pushMenu("FOC_LINK_OUT_BOX", "Outgoing link box");
		menuItem.setMenuAction(new IFocMenuItemAction() {
			
			@Override
			public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
				INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
				FocList focList = FocLinkOutBoxDesc.getList();
				mainWindow.changeCentralPanelContent_ToTableForFocList(focList);
			}
		});
		
		menuItem = mainMenu.pushMenu("FOC_LINK_OUT_RIGHTS", "Out Rights");
		menuItem.setMenuAction(new IFocMenuItemAction() {
			
			@Override
			public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
				INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
				FocList focList = FocLinkOutRightsDesc.getInstance().getFocList(FocList.LOAD_IF_NEEDED);
				mainWindow.changeCentralPanelContent_ToTableForFocList(focList);
			}
		});
		
		menuItem = mainMenu.pushMenu("FOC_LINK_IN_RIGHTS", "In Rights");
		menuItem.setMenuAction(new IFocMenuItemAction() {
			
			@Override
			public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
				INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
				FocList focList = FocLinkInRightsDesc.getInstance().getFocList(FocList.LOAD_IF_NEEDED);
				mainWindow.changeCentralPanelContent_ToTableForFocList(focList);
			}
		});
		
	}

}
