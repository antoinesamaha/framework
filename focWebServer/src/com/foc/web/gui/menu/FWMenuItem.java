/*
 * Created on 22-May-2005
 */
package com.foc.web.gui.menu;

import com.foc.business.multilanguage.LanguageKey;

/**
 * @author 01Barmaja
 */
public class FWMenuItem extends FWMenu{
  
  IFWMenuAction action = null;
  
  public FWMenuItem(LanguageKey langKey, String code, IFWMenuAction action){
    super(langKey, code);
    this.action = action ;
  }
  
  public FWMenuItem(String title, int mnemonic, String code, IFWMenuAction action){
    super(title, mnemonic, code);
    this.action = action ;
  }
  
  public FWMenuItem(LanguageKey langKey, IFWMenuAction action){
    super(langKey);
    this.action = action ;
  }
  
  public FWMenuItem(String title, int mnemonic, IFWMenuAction action){
    super(title, mnemonic);
    this.action = action ;
  }
  
  public boolean isList(){
    return false;
  }
  
  public boolean isSeparator(){
  	return false;
  }
  
  public IFWMenuAction getAction() {
    return action;
  }
}
