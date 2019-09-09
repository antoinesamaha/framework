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
package com.foc.vaadin.gui.components.popupButton;

import com.foc.menuStructure.FocMenuItem;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.menuTree.FVMenuTree;
import com.foc.web.gui.INavigationWindow;

@SuppressWarnings("serial")
public class FVPopupButton_Menu extends FVPopupButton {

	private INavigationWindow window = null;
	
	public FVPopupButton_Menu(INavigationWindow window, String content, int styleIndex) {
		this(window, content, FocXMLGuiComponentStatic.getButtonStyleForIndex(styleIndex));
	}
	
	public FVPopupButton_Menu(INavigationWindow window, String content, String styleName) {
		super(content, styleName);
		this.window = window;
	}
	
	public void dispose(){
		super.dispose();
		window = null;
	}
	
  /*
  public FVPopupContentButton_Menu newButton(INavigationWindow iNavigationWindow, FocMenuItem menuItem){
  	FVPopupContentButton_Menu button = null;
  	if(menuItem != null){
  		button = newButton(iNavigationWindow, menuItem.getTitle(), menuItem.getCode());
  	}
  	return button;
  }
  public FVPopupContentButton_Menu newButton(INavigationWindow iNavigationWindow, String caption, String menuCode){
		FVHorizontalLayout menuItemHorizontalLayout = new FVHorizontalLayout(null);
		menuItemHorizontalLayout.setWidth("100%");
		
		FVPopupContentButton_Menu contentButton = new FVPopupContentButton_Menu(getPopupWindow(), iNavigationWindow, menuTree, menuCode, caption);
		contentButton.addStyleName(styleName);
		contentButton.setWidth("100%");
		contentButton.setHeight("40px");

		menuItemHorizontalLayout.addComponent(contentButton);
		
//		if(withAddShortCut){
//			FVPopupContentButton addShortcutButton = new FVPopupContentButton(mainFocData, getPopupWindow(), "", iNavigationWindow, moduleCode, menuCode, withAddShortCut);
//			
//			menuItemHorizontalLayout.addComponent(addShortcutButton);
//			menuItemHorizontalLayout.setComponentAlignment(addShortcutButton, Alignment.BOTTOM_LEFT);
//			addShortcutButton.setStyleName(BaseTheme.BUTTON_LINK);
//			addShortcutButton.setIcon(FVIconFactory.getInstance().getFVIcon_Small(FVIconFactory.ICON_ADD));
//		}
		
		getPopupWindow().addComponentToVerticalLayout(menuItemHorizontalLayout);
		return contentButton;
  }
	*/
	
	public static FVPopupButton_Menu newPopupButton_ForMenu(INavigationWindow window, String menuTitle, String[] menuItemCodes, int buttonIndex){
		FVPopupButton_Menu button = null;
		
		if(menuItemCodes != null && menuItemCodes.length > 0){
			FocMenuItem[] menuItems = new FocMenuItem[menuItemCodes.length];
			
			FVMenuTree menuTree = window != null ? window.getMenuTree(true) : null;
			if(menuTree != null){
				for(int i=0; i<menuItemCodes.length; i++){
					menuItems[i] = menuTree.findMenuItem(menuItemCodes[i]);
					
					if(menuItems[i] != null){
	//					if(button == null) button = new FVPopupButton_Menu(menuTree, menuTitle, buttonIndex);
	//					button.newButton(window, menuItems[i]);
						if(button == null) button = new FVPopupButton_Menu(window, menuTitle, buttonIndex);
						FVPopupContentButton subMenu = button.newButton(window, menuItems[i].getTitle(), menuItems[i].getCode());
						subMenu.setPopupButtonClickListener(new PopupClickListener(button, menuItems[i].getCode()));
						subMenu.addStyleName(button.getStyleName());
					}
				}
			}
		}
		
		return button;
	}

	public FVMenuTree getMenuTree() {
		return window != null ? window.getMenuTree(true) : null;
	}
}
