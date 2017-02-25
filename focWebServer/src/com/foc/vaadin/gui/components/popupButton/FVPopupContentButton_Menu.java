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
