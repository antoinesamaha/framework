package com.foc.vaadin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.foc.Globals;
import com.foc.admin.FocGroup;
import com.foc.admin.FocVersion;
import com.foc.admin.GrpWebModuleRightsDesc;
import com.foc.desc.FocModule;
import com.foc.menuStructure.FocMenuItem;
import com.foc.util.Utils;
import com.foc.web.server.xmlViewDictionary.xmlViewKeyGenerator.XmlViewFileScanner;

public abstract class FocWebModule extends FocModule implements IFocWebModule{
	
	private boolean adminConsole          = false;
	private String  name                  = null;
	private String  title                 = null;
	private int     versionID             = 0;
	private String  versionLabel          = null;

	private int     priorityInDeclaration = 0;
	private int     order                 = 0;
	
	private HashMap<String, FocMenuItem> menuDictionary = null;

	private ArrayList<FoldersToScan> packages2Scan = null;
	
	public FocWebModule(String name, String title){
	  setName(name);
	  setTitle(title);
	}
	
	public FocWebModule(String name, String title, String modelPackage, String guiPackage){
		this(name, title);
		if(!Utils.isStringEmpty(modelPackage) || !Utils.isStringEmpty(guiPackage)){
			addPackages(modelPackage, guiPackage);
		}
	}

	public FocWebModule(String name, String title, String modelPackage, String guiPackage, String versionLabel, int versionID){
	  this(name, title, modelPackage, guiPackage);
	  setVersionLabel(versionLabel);
	  setVersionID(versionID);
	}

	public void dispose(){
		super.dispose();
		dispose_Packages();
	}
	
	private void dispose_Packages(){
		if(packages2Scan != null){
			Iterator<FoldersToScan> iter = (Iterator<FoldersToScan>) packages2Scan.iterator();
			while(iter != null && iter.hasNext()){
				FoldersToScan f2s = iter.next();
				f2s.dispose();
			}
			packages2Scan.clear();
			packages2Scan = null;
		}
	}
	
	public void addPackages(String modelPackage, String guiPackage){
		if(packages2Scan == null){
			packages2Scan = new ArrayList<FoldersToScan>();
		}
		FoldersToScan f2s = new FoldersToScan(modelPackage, guiPackage);
		packages2Scan.add(f2s);
	}
	
	public int getVersionID() {
		return versionID;
	}

	public void setVersionID(int versionID) {
		this.versionID = versionID;
	}
	
	public String getVersionLabel() {
		return versionLabel;
	}

	public void setVersionLabel(String versionLabel) {
		this.versionLabel = versionLabel;
	}

	public void scanGuiPackage(String packageName){
		XmlViewFileScanner xmlViewFileScanner = new XmlViewFileScanner(this, packageName);
		xmlViewFileScanner.scanPush();
		xmlViewFileScanner.dispose();
	}
	
	@Override
	public void declareFocObjectsOnce(){
		if(getVersionID() > 0 && !Utils.isStringEmpty(getVersionLabel()) && !Utils.isStringEmpty(getName())){
			FocVersion.addVersion(getName(), getVersionLabel(), getVersionID());
		}
		
		if(packages2Scan != null){
			for(int i=0; i<packages2Scan.size(); i++){
				FoldersToScan f2s = packages2Scan.get(i);
				
				scanModelPackage(f2s.getModelPackage());
			}
		}
	}
	
	public void declareXMLViewsInDictionary() {
		if(packages2Scan != null){
			for(int i=0; i<packages2Scan.size(); i++){
				FoldersToScan f2s = packages2Scan.get(i);
				
				scanGuiPackage(f2s.getGuiPackage());
			}
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

  public class FoldersToScan {
  	private String modelPackage = null;
  	private String guiPackage   = null;

		public FoldersToScan(String modelPackage, String guiPackage){
  		this.modelPackage = modelPackage;
  		this.guiPackage   = guiPackage;
  	}
  	
  	public void dispose(){
  		
  	}
  	
  	public String getModelPackage() {
			return modelPackage;
		}

		public String getGuiPackage() {
			return guiPackage;
		}
  }
  
}
