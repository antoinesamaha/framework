package com.foc.vaadin.gui.components.menuBar;

import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.vaadin.ui.MenuBar;

@SuppressWarnings("serial")
public class FVMenuBar extends MenuBar {

  private FVValidationLayout validationLayout = null;
  
  public FVMenuBar(FVValidationLayout validationLayout){
    this(validationLayout, false);
  }
  
  public FVMenuBar(FVValidationLayout validationLayout, boolean setAutoOpen){
    this.validationLayout = validationLayout;
    setAutoOpen(setAutoOpen);
  }
  
  public void dispose(){
    validationLayout = null;
  }

  public FVValidationLayout getValidationLayout() {
    return validationLayout;
  }
  
}
