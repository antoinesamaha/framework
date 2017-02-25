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