package com.foc.vaadin.gui.xmlForm;

import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;

public interface IValidationListener {
  public void    validationDiscard(FVValidationLayout validationLayout);
  public boolean validationCheckData(FVValidationLayout validationLayout);
  public boolean validationCommit(FVValidationLayout validationLayout);
  public void    validationAfter(FVValidationLayout validationLayout, boolean commited);
}
