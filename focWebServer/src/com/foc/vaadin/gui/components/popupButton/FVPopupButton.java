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

import java.util.HashMap;

import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.vaadin.gui.components.FVNativeButton;
import com.foc.vaadin.gui.layouts.FVHorizontalLayout;
import com.foc.web.gui.INavigationWindow;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class FVPopupButton extends FVButton implements ClickListener {

	private FVPopupWindow popupWindow    = null;
	private String[]      styleNameArray = null;
	private HashMap<String, FVPopupContentButton> buttonMap = null;//Basically used for Unit testing
	
	public FVPopupButton(String content) {
		this(content, (String[]) null);
	}

	public FVPopupButton(String content, int styleIndex) {
		this(content, FocXMLGuiComponentStatic.getButtonStyleForIndex(styleIndex));
	}
	
	public FVPopupButton(String content, String styleName) {
		super(content);
		String[] styleNames = {styleName};
		init(styleNames);
	}
	
	public FVPopupButton(String content, String[] styleNames) {
		super(content);
		init(styleNames);
	}
	
	private void init(String[] styleName){
		this.styleNameArray = styleName;
		buttonMap = new HashMap<String, FVPopupContentButton>();
		
		popupWindow = new FVPopupWindow(this);
		if(styleNameArray != null && styleNameArray.length > 0){
			for(int i=0; i<styleNameArray.length; i++) {
				addStyleName(styleNameArray[i]);
			}
		}else{
			addStyleName("foc-button-orange");
		}
		addClickListener(this);
	}
	
	public void dispose(){
		super.dispose();
		if(popupWindow != null){
			popupWindow.dispose();
			popupWindow = null;
		}
		if(buttonMap != null){
			buttonMap.clear();
		}
	}

	@Override
	public void buttonClick(ClickEvent event) {
		if(getPopupWindow() != null){
			getPopupWindow().setPositionX(event.getClientX());
			getPopupWindow().setPositionY(event.getClientY() + 30);
			UI.getCurrent().addWindow(getPopupWindow());
			getPopupWindow().focus();
		}
	}

  public FVPopupContentButton newButton(INavigationWindow iNavigationWindow, String caption, String menuCode){
		FVHorizontalLayout menuItemHorizontalLayout = new FVHorizontalLayout(null);
		menuItemHorizontalLayout.setWidth("100%");
		
		FVPopupContentButton contentButton = new FVPopupContentButton(getPopupWindow(), iNavigationWindow, caption);
		if(styleNameArray != null) {
			for(int i=0; i<styleNameArray.length; i++) {
				contentButton.addStyleName(styleNameArray[i]);
			}
		}
		contentButton.setWidth("100%");
		contentButton.setHeight("40px");

		menuItemHorizontalLayout.addComponent(contentButton);
		if(buttonMap != null){
			buttonMap.put(menuCode, contentButton);
		}
		
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
	
	public FVPopupWindow getPopupWindow(){
		return popupWindow;
	}

	public String[] getStyleNameArray() {
		return styleNameArray;
	}
	
	public FVPopupContentButton getContentButton(String code){
		return buttonMap != null ? buttonMap.get(code) : null;
	}
}
