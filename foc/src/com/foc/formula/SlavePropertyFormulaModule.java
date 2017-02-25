package com.foc.formula;

import java.awt.event.ActionListener;
import javax.swing.JMenuItem;

import com.foc.*;
import com.foc.desc.*;
import com.foc.gui.FPopupMenu;
import com.foc.menu.FMenuList;

/**
 * @author 01Barmaja
 */
public class SlavePropertyFormulaModule extends FocModule {

  public SlavePropertyFormulaModule() {
  }
  
  public static JMenuItem addPopUpMenu(FPopupMenu popupMenu, ActionListener listener){
    
    JMenuItem fieldFormulas = new JMenuItem("Field Formulas");
    fieldFormulas.addActionListener(listener);
    
    popupMenu.add(fieldFormulas);
    return fieldFormulas;
  }

  @Override
  public void declareFocObjectsOnce() {
    declareFocDescClass(SlavePropertyFormulaDesc.class);
  }
  
  public void declare() {
    Application app = Globals.getApp();
    app.declareModule(this);
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
  
  private static SlavePropertyFormulaModule module = null;
  public static SlavePropertyFormulaModule getInstance(){
    if(module == null){
      module = new SlavePropertyFormulaModule();
    }
    return module;
  }
}
