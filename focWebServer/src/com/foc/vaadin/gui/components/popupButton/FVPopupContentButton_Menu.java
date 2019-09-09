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

import com.foc.vaadin.gui.menuTree.FVMenuTree;
import com.foc.web.gui.INavigationWindow;

@SuppressWarnings("serial")
public class FVPopupContentButton_Menu extends FVPopupContentButton implements IPopupButtonClickListener {

	private FVMenuTree menuTree = null;
	private String menuCode = null;
	
	public FVPopupContentButton_Menu(FVPopupWindow popupWindow, INavigationWindow iNavigationWindow, FVMenuTree menuTree, String code, String content){
		super(popupWindow, iNavigationWindow, content);
		this.menuCode = code;
		this.menuTree = menuTree;
	}
		
	public void dispose(){
		super.dispose();
		menuCode = null;
		menuTree = null;
	}
	
	public String getMenuCode() {
		return menuCode;
	}

	@Override
	public void buttonClicked(FVPopupContentButton button) {
		if(menuTree != null && menuCode != null && getWindow() != null){
			menuTree.clickMenuItem(getWindow(), menuCode);
		}
	}
}
