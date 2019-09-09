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

public class PopupClickListener implements IPopupButtonClickListener {
	/**
	 * 
	 */
	private FVPopupButton_Menu fvPopupButton_Menu;
	private String menuCode = null;
	
	public PopupClickListener(FVPopupButton_Menu fvPopupButton_Menu, String menuCode){
		this.fvPopupButton_Menu = fvPopupButton_Menu;
		this.menuCode = menuCode;
	}
	
	@Override
	public void buttonClicked(FVPopupContentButton button) {
		if(this.fvPopupButton_Menu.getMenuTree() != null && menuCode != null && button != null && button.getWindow() != null){
			this.fvPopupButton_Menu.getMenuTree().clickMenuItem(button.getWindow(), menuCode);
		}
	}
}
