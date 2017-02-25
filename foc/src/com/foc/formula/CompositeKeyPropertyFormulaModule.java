package com.foc.formula;

import com.foc.desc.*;
import com.foc.menu.FMenuList;

/**
 * @author 01Barmaja
 */
public class CompositeKeyPropertyFormulaModule extends FocModule {

  public CompositeKeyPropertyFormulaModule() {
  }

  @Override
  public void declareFocObjectsOnce() {
    declareFocDescClass(CompositeKeyPropertyFormulaDesc.class);
  }
  
  public void addApplicationMenu(FMenuList menuList) {
  }

  public void addConfigurationMenu(FMenuList menuList) {
  }

  public void afterAdaptDataModel() {
  }

  public void afterApplicationEntry() {
  }

  public void afterApplicationLogin() {
  }

  public void beforeAdaptDataModel() {
  }

  public void dispose() {
  }
  
  private static CompositeKeyPropertyFormulaModule module = null;
  public static CompositeKeyPropertyFormulaModule getInstance(){
    if(module == null){
      module = new CompositeKeyPropertyFormulaModule();
    }
    return module;
  }
}
