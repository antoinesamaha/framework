package com.foc.formula;

import com.foc.desc.FocConstructor;
import com.foc.property.FProperty;

public class CompositeKeyPropertyFormula extends PropertyFormula {

	private FProperty property = null;
	
  public CompositeKeyPropertyFormula(FocConstructor constr) {
    super(constr);
  }
  
  public void dispose(){
  	super.dispose();
  	property = null;
  }
  
  public void setProperty(FProperty property){
  	this.property = property;
  }

  public FProperty getProperty(){
  	return this.property;
  }

  public void setFilterCriteria(String filterCriteria){
    setPropertyString(CompositeKeyPropertyFormulaDesc.FLD_FILTER_CRITERIA, filterCriteria);
  }
  
  public void setViewName( String viewName ){
    setPropertyString(CompositeKeyPropertyFormulaDesc.FLD_VIEW_NAME, viewName);
  }
  
  public void setObjectRefs( String objectRefs ){
    setPropertyString(CompositeKeyPropertyFormulaDesc.FLD_OBJECT_REFS, objectRefs);
  }
  
  public String getObjectRefs(){
    return getPropertyString(CompositeKeyPropertyFormulaDesc.FLD_OBJECT_REFS);
  }

  public void updateFatherFormulaProperties() {
    CompositeKeyPropertyFormulaEnvironment compositeKeyPropertyFormulaEnvironment  = (CompositeKeyPropertyFormulaEnvironment)formulaEnvironment;
    if(compositeKeyPropertyFormulaEnvironment != null){
      IFormulaModel formulaModel = compositeKeyPropertyFormulaEnvironment.getFormulaModel();
      FProperty installedFormulaProperty = formulaModel.getFormulaProperty(getObjectRefs());
      
      if(installedFormulaProperty != null){
        if(installedFormulaProperty.isWithFormula()){
          String expression = getExpression(); 
          if(expression.equals("")){
            installedFormulaProperty.removeFormula();
            setDeleted(true);
          }else{
            formulaEnvironment.applyFormulaToProperty(installedFormulaProperty, expression);
          }
        }
      }
    }
  }
  
  public void updateUsingProperty(){
    CompositeKeyPropertyFormulaEnvironment compositeKeyPropertyFormulaEnvironment  = (CompositeKeyPropertyFormulaEnvironment)formulaEnvironment;
    if(compositeKeyPropertyFormulaEnvironment != null){
      IFormulaModel formulaModel = compositeKeyPropertyFormulaEnvironment.getFormulaModel();

      if(property != null){
      	setFilterCriteria(formulaModel.getFilterCriteria());
      	setObjectRefs(formulaModel.getObjectRefs(property));
      }
    }
  }
}
