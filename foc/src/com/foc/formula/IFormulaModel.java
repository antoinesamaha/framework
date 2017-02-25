package com.foc.formula;

import com.foc.property.FProperty;

public interface IFormulaModel {
  public String getViewName();
  public String getFilterCriteria();
  public FProperty getFormulaProperty(String objectRefs);
  public String getObjectRefs( FProperty property );
  
  public void dispose();
}
