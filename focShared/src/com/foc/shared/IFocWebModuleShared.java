package com.foc.shared;

public interface IFocWebModuleShared {
  public int     getOrder();
  public int     getPriorityInDeclaration();
	public boolean isAdminConsole();
	public String  getName();
	public String  getTitle();
  public void    declareXMLViewsInDictionary();
  public void    declareLeafMenuItems();
  public void    declareUnitTestingSuites();
}
