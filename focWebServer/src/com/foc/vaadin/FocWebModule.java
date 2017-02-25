package com.foc.vaadin;

import java.util.HashMap;

import com.foc.Globals;
import com.foc.admin.FocGroup;
import com.foc.admin.FocVersion;
import com.foc.admin.GrpWebModuleRightsDesc;
import com.foc.desc.FocModule;
import com.foc.desc.xml.XMLDescFileScanner;
import com.foc.menuStructure.FocMenuItem;
import com.foc.shared.dataStore.IFocData;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.util.Utils;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.xmlViewKeyGenerator.XmlViewFileScanner;

public abstract class FocWebModule extends FocModule implements IFocWebModule{
	
	private boolean adminConsole          = false;
	private String  name                  = null;
	private String  title                 = null;
	private int     priorityInDeclaration = 0;
	private int     order                 = 0;
	
	private HashMap<String, FocMenuItem> menuDictionary = null;
	
	private String modelPackage = null;
	private String guiPackage   = null;
	
	public FocWebModule(String name, String title){
	  setName(name);
	  setTitle(title);
	}
	
	public FocWebModule(String name, String title, String modelPackage, String guiPackage){
		this(name, title);
		this.modelPackage = modelPackage;
		this.guiPackage = guiPackage;
	}
	
	protected void scanPackage(String packageName){
		XmlViewFileScanner xmlViewFileScanner = new XmlViewFileScanner(this, packageName);
		xmlViewFileScanner.scanPush();
		xmlViewFileScanner.dispose();
	}
	
  public void declareXMLViewsInDictionary() {
  	if(!Utils.isStringEmpty(guiPackage)){
  		String xmlPath = guiPackage.replace(".", "/");
  		xmlPath = "/" + xmlPath + "/xml/"; 
  		
  		XmlViewFileScanner xmlViewFileScanner = new XmlViewFileScanner(this, xmlPath, guiPackage);
  		xmlViewFileScanner.scanPush();
  		xmlViewFileScanner.dispose();
  	}
  }
	
	@Override
	public void declareFocObjectsOnce(){
  	if(!Utils.isStringEmpty(modelPackage)){
  		String xmlPath = modelPackage.replace(".", "/");
  		xmlPath = "/" + xmlPath + "/xml/"; 
  		
  		XMLDescFileScanner scanner = new XMLDescFileScanner(this, xmlPath, modelPackage);
  		scanner.scanDirectory();
  		scanner.dispose();
  	}		
	}
	
	@Override
	public boolean isAdminConsole() {
		return adminConsole;
	}

	public void setAdminConsole(boolean adminConsole) {
		this.adminConsole = adminConsole;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void putMenu(FocMenuItem menuItem){
	  if(menuItem != null && menuItem.getCode() != null && !menuItem.getCode().isEmpty()){
  	  if(menuDictionary == null){
  	    menuDictionary = new HashMap<String, FocMenuItem>();
  	  }
  	  menuDictionary.put(menuItem.getCode(), menuItem);
	  }
	}
	
	public FocMenuItem getMenu(String menuCode){
	  return menuDictionary != null ? menuDictionary.get(menuCode) : null;
	}
	
	public void declareLeafMenuItems() {
	  
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * To be implemented in the specific modules 
	 */
	public void declareUnitTestingSuites(){
	}

  public int getPriorityInDeclaration() {
    return priorityInDeclaration;
  }

  public void setPriorityInDeclaration(int priorityInDeclaration) {
    this.priorityInDeclaration = priorityInDeclaration;
  }

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
	
	public static boolean allowAccessToModule(String moduleName) {
		FocGroup focGroup = FocWebApplication.getFocUser() != null ? FocWebApplication.getFocUser().getGroup() : null;
		return allowAccessToModule(focGroup, moduleName);
	}
	
	public static boolean allowAccessToModule(FocGroup focGroup, String moduleName) {
		int access = focGroup != null ? focGroup.getWebModuleRights(moduleName) : GrpWebModuleRightsDesc.ACCESS_NONE;
		return access == GrpWebModuleRightsDesc.ACCESS_FULL || access == GrpWebModuleRightsDesc.ACCESS_FULL_WITH_CONFIGURTION;
	}
	
  public FocGroup getFocGroup(){
		FocGroup group = null;
		if(Globals.getApp() != null && Globals.getApp().getUser_ForThisSession() != null && Globals.getApp().getUser_ForThisSession().getGroup() != null){
			group = Globals.getApp().getUser_ForThisSession().getGroup();
		}
		return group;
	}
}
