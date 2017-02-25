package com.foc.formula;

import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;

public class PropertyFormula extends FocObject {

  protected AbstractFormulaEnvironment formulaEnvironment = null;
  
  public PropertyFormula(FocConstructor constr) {
    super(constr);
    newFocProperties();
  }
  
  public void dispose(){
    super.dispose();
    formulaEnvironment = null;
  }
  
  public String getFieldName(){
    return getPropertyString(PropertyFormulaDesc.FLD_FIELD_NAME);
  }
  
  public void setFieldName( String fieldName ){
    setPropertyString(PropertyFormulaDesc.FLD_FIELD_NAME, fieldName);
  }
  
  public String getExpression(){
    return getPropertyString(PropertyFormulaDesc.FLD_EXPRESSION);
  }
  
  public void setExpression( String expression ){
    setPropertyString(PropertyFormulaDesc.FLD_EXPRESSION, expression);
  }
  
  public Formula getFormula(){
    return new Formula(getExpression());
  }
  
  public void updateFatherFormulaProperties(){
    
  }
  
  public void setFormulaEnvironment(AbstractFormulaEnvironment formulaEnvironment) {
    this.formulaEnvironment = formulaEnvironment;
  }
}
