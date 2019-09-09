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
package com.foc.vaadin.gui.layouts.validationLayout;

import java.util.List;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.MenuBar.MenuItem;
import com.foc.vaadin.FocWebVaadinWindow;
import com.foc.vaadin.gui.components.menuBar.FVMenuBar;
import com.foc.vaadin.gui.components.menuBar.FVMenuBarCommand;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.vaadin.ui.NativeButton;

@SuppressWarnings("serial")
public class FVOptionMobileLayout extends FocXMLLayout{

	private FocWebVaadinWindow focWebVaadinWindow             = null;
	private FVValidationLayout validationLayout               = null;
	private NativeButton       closeValidationLayoutForMobile = null;
	
	public FVOptionMobileLayout(FocWebVaadinWindow focWebVaadinWindow, FVValidationLayout validationLayout) {
		this.focWebVaadinWindow = focWebVaadinWindow;
		this.validationLayout   = validationLayout;
	}
	
	public void init(){
		setSpacing(true);
		addStyleName("slide");
		fill();
	}
	
	@Override
	public void dispose() {
		super.dispose();
		focWebVaadinWindow = null;
		validationLayout   = null;
	}
	
	private NativeButton getCloseButton(){
		if(closeValidationLayoutForMobile == null){
			closeValidationLayoutForMobile = new NativeButton("Close");
			closeValidationLayoutForMobile.addStyleName("focBannerButton");
			closeValidationLayoutForMobile.setHeight("40px");
			closeValidationLayoutForMobile.setWidth("100%");
			closeValidationLayoutForMobile.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					if(focWebVaadinWindow != null){
						focWebVaadinWindow.goBack(null);
					}
				}
			});
		}
		return closeValidationLayoutForMobile;
	}
	
	public void fill(){
		if(focWebVaadinWindow != null){
			focWebVaadinWindow.getCentralHeader().markAsDirty();
			if(validationLayout != null && validationLayout.getMenubar(false) != null){
				FVMenuBar menuBar = validationLayout.getMenubar(false);
				addMoreItemsAsButtons(menuBar);
			}
			NativeButton logoutButton = focWebVaadinWindow.getLogoutButton();
			if(logoutButton != null){
				logoutButton.setCaption("Logout");
				logoutButton.setWidth("100%");
				addComponent(logoutButton);
			}
			NativeButton adminButton = focWebVaadinWindow.getAdminButton();
			if(adminButton != null){
				adminButton.setCaption("Admin");
				adminButton.setWidth("100%");
				addComponent(adminButton);
			}
		}
		if(validationLayout != null){
			FVViewSelector_MenuBar viewSelector = validationLayout.getViewSelector(true);
			viewSelector.addStyleName("focBannerButton");
			viewSelector.setWidth("100%");
			addComponent(viewSelector);
		}
		NativeButton closeButton = getCloseButton();
		addComponent(closeButton);
	}
	
	private void addMoreItemsAsButtons(FVMenuBar menuBar){
		List<MenuItem> munItemList = menuBar.getItems();
		
		if(munItemList.size() > 0){
			MenuItem moreItem = munItemList.get(0);
			if(moreItem != null){
				List<MenuItem> children = moreItem.getChildren();
				for(int i=0; i<children.size(); i++){
					MenuItem menuItem = children.get(i);
					if(menuItem != null){
						String   title    = menuItem.getText();
						FVMenuBarCommand  menuBarCommand = (FVMenuBarCommand) menuItem.getCommand();
						if(menuBarCommand != null){
							NativeButton button = new NativeButton(title);
							button.setWidth("100%");
							button.setHeight("40px");
							button.addStyleName("focBannerButton");
							addComponent(button);
							button.addClickListener(new MenuItemButtonClickListener(menuItem, menuBarCommand));
						}
					}
				}
			}
		}
		
	}
	
	private class MenuItemButtonClickListener implements ClickListener{

		private FVMenuBarCommand menuBarCommand = null;
		private MenuItem         menuItem       = null;
		
		public MenuItemButtonClickListener(MenuItem menuItem, FVMenuBarCommand menuBarCommand) {
			this.menuItem = menuItem;
			this.menuBarCommand = menuBarCommand;
		}
		
		@Override
		public void buttonClick(ClickEvent event) {
			if(menuBarCommand != null && menuItem != null){
				menuBarCommand.menuSelected(menuItem);
			}
		}
	}
}
