package com.foc.formula;

import com.foc.FIterator;
import com.foc.desc.FocFieldEnum;
import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.property.FProperty;

public class SlavePropertyFormulaEnvironment extends AbstractFormulaEnvironment {

  private FIterator iterator = null;
  
  public SlavePropertyFormulaEnvironment(FIterator iterator){
    this.iterator = iterator;
  }
  
  public void dispose() {
    super.dispose();
    if(iterator != null){
    	iterator.dispose();
    }
    iterator = null;
  }
  
  @Override
  public void loadAllFormulas() {
    /*while( iterator != null && iterator.hasNext() ) {
      FocObject focObj = (FocObject)iterator.next();
      //This is to load the formulas into memory
      focObj.getPropertyFormulaList();
    }*/
  }
  
  @Override
  public void applyAllFormulas() {
    
   while( iterator != null && iterator.hasNext() ) {
     FocObject focObj = (FocObject)iterator.next();
     FocList propertyFormulaList = focObj.getPropertyFormulaList();
     FocFieldEnum iter = new FocFieldEnum(focObj.getThisFocDesc(), focObj, FocFieldEnum.CAT_ALL, FocFieldEnum.LEVEL_PLAIN);
     while(iter != null && iter.hasNext()){
       iter.next();
       FProperty prop = (FProperty) iter.getProperty();
       if( !prop.isWithFormula() ){
         String fieldName = prop.getFocField().getDBName();
         PropertyFormula propertyFormula = (PropertyFormula)propertyFormulaList.searchByPropertyStringValue(PropertyFormulaDesc.FLD_FIELD_NAME, fieldName, true);
         if( propertyFormula != null ){
           propertyFormula.setFormulaEnvironment(this);
           prop.setPropertyFormulaContext(newPropertyFormulaContext(prop, propertyFormula));
         }  
       }
     }
   }
    
  }
  
  @Override
  public void applyFormulaToProperty(FProperty property, String expression) {
    FocObject focObj = property.getFocObject();
    FocList propertyFormulaList = focObj.getPropertyFormulaList();
    if( propertyFormulaList != null ){
      String fieldName = property.getFocField().getDBName();
      PropertyFormula propertyFormula = (PropertyFormula)propertyFormulaList.searchByPropertyStringValue(PropertyFormulaDesc.FLD_FIELD_NAME, fieldName, true);
      if( propertyFormula != null ){
        propertyFormula.setExpression(expression);
        property.removeFormula();
        property.setPropertyFormulaContext(newPropertyFormulaContext(property, propertyFormula));
      }else{
        newFormula(expression, property);
      }
    }
  }
  
  private void newFormula(String expression, FProperty property){
    FocObject focObj = property.getFocObject();
    property.removeFormula();
    FocList propertyFormulaList = focObj.getPropertyFormulaList();
    PropertyFormula propertyFormula = (PropertyFormula)propertyFormulaList.newEmptyItem();
    propertyFormula.setFormulaEnvironment(this);
    propertyFormula.setFieldName(property.getFocField().getDBName());
    propertyFormula.setExpression(expression);
    property.setPropertyFormulaContext(newPropertyFormulaContext(property, propertyFormula));
  }

}
