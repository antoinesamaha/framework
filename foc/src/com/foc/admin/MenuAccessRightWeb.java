package com.foc.admin;

import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;

public class MenuAccessRightWeb extends FocObject{

	private boolean    visited          = false;
	
  public MenuAccessRightWeb(FocConstructor constr) {
    super(constr);
    newFocProperties();
  }
  
  public void copy(MenuAccessRightWeb source){
    setGroup(source.getGroup());
    setFatherMenu(source.getFatherMenu());
    setCode(source.getCode());
    setTitle(source.getTitle());
    setRight(source.getRight());
    setCustomTitle(source.getCustomTitle());
  }
  
  public FocGroup getGroup(){
    return (FocGroup) getPropertyObject(MenuRightsDesc.FLD_GROUP);
  }
  
  public void setGroup(FocGroup group){
    setPropertyObject(MenuRightsDesc.FLD_GROUP, group);
  }
  
  public MenuAccessRightWeb getFatherMenu(){
    return (MenuAccessRightWeb) getPropertyObject(MenuRightsDesc.FLD_FATHER_MENU_RIGHT);
  }
 
  public void setFatherMenu(MenuAccessRightWeb menuRights){
    setPropertyObject(MenuRightsDesc.FLD_FATHER_MENU_RIGHT, menuRights);
  }
  
  public String getCode(){
    return getPropertyString(MenuRightsDesc.FLD_MENU_CODE);
  }

  public void setCode(String code){
    setPropertyString(MenuRightsDesc.FLD_MENU_CODE, code);
  }

  public String getTitle(){
    return getPropertyString(MenuRightsDesc.FLD_MENU_TITLE);
  }

  public void setTitle(String title){
    setPropertyString(MenuRightsDesc.FLD_MENU_TITLE, title);
  }

  public String getCustomTitle(){
    return getPropertyString(MenuRightsDesc.FLD_CUSTOM_TITLE);
  }

  public void setCustomTitle(String title){
    setPropertyString(MenuRightsDesc.FLD_CUSTOM_TITLE, title);
  }

  public String getTitleToDisplay(){
  	String str = getCustomTitle();
    return str.isEmpty() ? getTitle() : str;
  }

  public int getRight(){
    return getPropertyMultiChoice(MenuRightsDesc.FLD_RIGHT);
  }

  public void setRight(int right){
    setPropertyMultiChoice(MenuRightsDesc.FLD_RIGHT, right);
  }

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}
}
