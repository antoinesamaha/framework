package com.foc.desc;

import com.foc.Globals;
import com.foc.desc.xml.XMLDescFileScanner;
import com.foc.menu.FMenuList;

/**
 * @author 01Barmaja
 */
public abstract class FocModule {
  private boolean declared = false;
  
  public abstract void declareFocObjectsOnce();
  
  public void declareFocObjects(){
    if(!declared){
      declared = true;
      declareFocObjectsOnce();
    }
  }
  
  public void dispose(){
  	
  }

  public void scanPackageForDescDeclarations(String packageName){
  	XMLDescFileScanner scanner = new XMLDescFileScanner(this, packageName);
		scanner.scanDirectory();
		scanner.dispose();
  }
  
  public boolean isDeclared(){
  	return declared;
  }
  
  public void afterConstruction(){
  }
  
  public void addConfigurationMenu(FMenuList menuList){
  }
  
  public void addApplicationMenu(FMenuList menuList){
  }
  
  public void afterAdaptDataModel() {
  }

  public void afterApplicationEntry() {
  }

  public void afterApplicationLogin() {
  }

  public void beforeAdaptDataModel() {
  }
  
  public void declareSP(){
  	
  }
  
	public void declare() {
		Globals.getApp().declareModule(this);
	}
	
	public void declareFocDescClass(Class classObject){
		Globals.getApp().declaredObjectList_DeclareDescription(this, classObject);
	}
	
	public void declareFocDescClass_ForExistingInstance(Class classObject){
		Globals.getApp().declaredObjectList_DeclareObjectForExistingInstance(this, classObject);
	}
	
	public String getName(){
		String name = this.getClass().getName();
		int last = name.lastIndexOf(".");
		if(last+1 < name.length()){
			name = name.substring(last+1);
		}
		return name;  
	}
}
