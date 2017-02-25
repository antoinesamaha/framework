package com.foc.vaadin.gui.components.popupButton;

import java.util.HashMap;

import com.vaadin.ui.Button.ClickListener;
import com.foc.util.Utils;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.components.FVNativeButton;
import com.foc.vaadin.gui.layouts.FVHorizontalLayout;
import com.foc.web.gui.INavigationWindow;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class FVPopupButton extends FVNativeButton implements ClickListener {

	private FVPopupWindow popupWindow = null;
	private String        styleName   = null;
	private HashMap<String, FVPopupContentButton> buttonMap = null;//Basically used for Unit testing
	
	public FVPopupButton(String content) {
		this(content, null);
	}

	public FVPopupButton(String content, int styleIndex) {
		this(content, FocXMLGuiComponentStatic.getButtonStyleForIndex(styleIndex));
	}
	
	public FVPopupButton(String content, String styleName) {
		super(content);
		init(styleName);
	}
	
	private void init(String styleName){
		this.styleName = styleName;
		buttonMap = new HashMap<String, FVPopupContentButton>();
		
		popupWindow = new FVPopupWindow(this);
		if(!Utils.isStringEmpty(styleName)){
			addStyleName(styleName);
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
		contentButton.addStyleName(styleName);
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

	public String getStyleName() {
		return styleName;
	}
	
	public FVPopupContentButton getContentButton(String code){
		return buttonMap != null ? buttonMap.get(code) : null;
	}
}
