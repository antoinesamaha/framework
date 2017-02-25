package com.foc.vaadin;

import com.foc.menuStructure.FocMenuItem;
import com.foc.shared.IFocWebModuleShared;
import com.foc.shared.dataStore.IFocData;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.gui.menuTree.FVMenuTree;
import com.foc.web.gui.INavigationWindow;

public interface IFocWebModule extends IFocWebModuleShared {
  public void    menu_FillMenuTree(FVMenuTree menuTree, FocMenuItem fatherMenuITem);
//  public XMLViewKey popupView(IFocData iFocData, INavigationWindow mainWindow, String menuCode, int xmlViewType);
}
