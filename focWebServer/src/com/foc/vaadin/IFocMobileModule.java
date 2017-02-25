package com.foc.vaadin;

import com.foc.menuStructure.FocMenuItem;
import com.foc.vaadin.gui.menuTree.FVMenuTree;

public interface IFocMobileModule {
  public int     getOrder();
  public int     getPriorityInDeclaration();
//	public boolean isAdminConsole();
	public String  getName();
	public String  getTitle();
//  public void    declareXMLViewsInDictionary();
  public void    declareLeafMenuItems();
//  public void    declareUnitTestingSuites();
//  public void    menu_FillMenuTree(FVMenuTree menuTree, FocMenuItem fatherMenuITem);
}
