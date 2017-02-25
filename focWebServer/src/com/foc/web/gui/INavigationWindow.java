package com.foc.web.gui;

import com.foc.dataWrapper.FocListWrapper;
import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.IRightPanel;
import com.foc.vaadin.gui.menuTree.FVMenuTree;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.vaadin.ui.Component;

public interface INavigationWindow {
	public FVMenuTree getMenuTree(boolean createIfNeeded);
	public void changeCentralPanelContent(ICentralPanel newCentralPanel, boolean keepPrevious);
	public void changeCentralPanelContent(ICentralPanel newCentralPanel, int previousMode);
	public ICentralPanel changeCentralPanelContent_ToTableForFocList(FocList focList);
	public ICentralPanel changeCentralPanelContent_ToTableForFocListWrapper(FocListWrapper focList);
	public ICentralPanel changeCentralPanelContent_ToFormForFocObject(FocObject focObject);
	public void goBack(ICentralPanel iCentralPanelToRemove);
	public void refreshCentralPanelAndRightPanel();
	public void addUtilityPanel(IRightPanel utilityPanel);
	public void removeUtilityPanel(IRightPanel utilityPanel);
	public void fillHomepageShortcutMenu(FocXMLLayout centralPanel);
	public void showValidationLayout(Component validationLayout);
}
