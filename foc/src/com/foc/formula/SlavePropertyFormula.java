package com.foc.formula;

import com.foc.desc.FocConstructor;
import com.foc.desc.FocFieldEnum;
import com.foc.desc.FocObject;
import com.foc.property.FProperty;

public class SlavePropertyFormula extends PropertyFormula {

  public SlavePropertyFormula(FocConstructor constr) {
    super(constr);
  }
  
  public FProperty getForeignObjectPropertyIfNotEmpty(){
    FProperty foundProp = null;
    SlavePropertyFormulaDesc desc = (SlavePropertyFormulaDesc)getThisFocDesc();
    for( int i = 0; i < desc.getForeignObjectFieldCount() && foundProp == null; i++ ){
      int fieldID = SlavePropertyFormulaDesc.FLD_FIRST_OBJECT+i;
      FocObject foreignObject = getPropertyObject(fieldID);
      if(foreignObject != null){
        foundProp = getFocProperty(fieldID);
      }
    }
    return foundProp;
  }
  
  public FocObject getForeignObject(){
    FocObject foreinObject = null;
    FProperty prop = getForeignObjectPropertyIfNotEmpty();
    if( prop != null ){
      foreinObject = getPropertyObject(prop.getFocField().getID());
    }
    return foreinObject; 
  }
  
  public void updateFatherFormulaProperties(){
    FocObject foreignObject = getForeignObject();
    if( foreignObject != null ){
      FocFieldEnum iter = new FocFieldEnum(foreignObject.getThisFocDesc(), foreignObject, FocFieldEnum.CAT_ALL, FocFieldEnum.LEVEL_PLAIN);
      while(iter != null && iter.hasNext()){
        iter.next();
        FProperty prop = (FProperty) iter.getProperty();
        if( prop.getFocField().getDBName().equals(getFieldName()) ){
          if(prop.isWithFormula()){
            String expression = getExpression(); 
            
            if(expression.equals("")){
              prop.removeFormula();
              setDeleted(true);
            }else{
              formulaEnvironment.applyFormulaToProperty(prop, expression);
            }
            break;  
          }
        }
      }
    }
  }
  
}
