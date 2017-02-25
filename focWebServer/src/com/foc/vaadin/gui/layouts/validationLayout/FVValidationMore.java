package com.foc.vaadin.gui.layouts.validationLayout;

public class FVValidationMore {

  private IValidationLayoutMoreMenuFiller iValidationLayoutMenuFiller = null;

  public IValidationLayoutMoreMenuFiller getIValidationLayoutMoreMenuFiller() {
    return iValidationLayoutMenuFiller;
  }

  public void setValidationLayoutMoreMenuFiller(IValidationLayoutMoreMenuFiller iValidationLayoutMenuFiller) {
    this.iValidationLayoutMenuFiller = iValidationLayoutMenuFiller;
  }

  //----------------------------------------------
  // Static
  //----------------------------------------------
  
  private static FVValidationMore validationMore = null;
  public static FVValidationMore getInstance(){
    if(validationMore == null){
      validationMore = new FVValidationMore();
    }
    return validationMore; 
  }
}