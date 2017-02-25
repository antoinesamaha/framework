/*
 * Created on 22-May-2005
 */
package com.foc.web.gui.menu;

import java.util.*;

import com.foc.business.multilanguage.LanguageKey;

/**
 * @author 01Barmaja
 */
public class FWMenuList extends FWMenu{
  
  private LinkedList<FWMenu> list = null;

  public FWMenuList(LanguageKey langKey, String code){
    super(langKey, code);
    list = new LinkedList<FWMenu>();
  }

  public FWMenuList(String title, int mnemonic, String code){
    super(title, mnemonic, code);
    list = new LinkedList<FWMenu>();
  }
  
  public FWMenuList(LanguageKey langKey){
    super(langKey);
    list = new LinkedList<FWMenu>();
  }

  public FWMenuList(String title, int mnemonic){
    super(title, mnemonic);
    list = new LinkedList<FWMenu>();
  }
  
  public void addMenu(FWMenu menu){
    list.add(menu);
  }

  public void removeMenu(FWMenu menu){
    list.remove(menu);
  }
  
  public int menuCount(){
    return list.size();
  }
  
  public FWMenu getMenu(int i){
    return (FWMenu) list.get(i);
  }
  
  public char getMnemonicForMenu(FWMenu menu){
  	char mnemonic = ' ';
  	String menuTitle = menu.getTitle();
  	if(menuTitle != null){
  		boolean similarMnemoniFoundInPreviousMenus = true;
  		int i = -1;
  		while(similarMnemoniFoundInPreviousMenus && i < menuTitle.length()){
  			i++;
	  		mnemonic = menuTitle.charAt(i);
	  		//Iterator<String> iter = getHelpMenuesIterator();
	  		boolean found = false;
	  		boolean finish = false;
	  		//while(iter != null && iter.hasNext() && !found && !finish){
	  		for(int j = 0; j < menuCount() && !found && !finish; j++ ){
	  			FWMenu aMenu = getMenu(j);
	  			String aMenuTitle = aMenu.getTitle();
	  			if(aMenuTitle.equals(menuTitle)){
	  				finish = true;
	  			}
	  			if(!finish){
		  			char aMenuMnemonic = getMnemonicForMenu(aMenu);
		  			found = aMenuMnemonic == mnemonic;
	  			}
	  		}
	  		similarMnemoniFoundInPreviousMenus = found;
  		}
  	}
  	return mnemonic;
  }
  
  public boolean isList(){
    return true;
  }
}
