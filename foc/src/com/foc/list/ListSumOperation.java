/*
 * Created on Dec 2, 2005
 */
package com.foc.list;

import com.foc.desc.FocObject;
import com.foc.desc.field.FFieldPath;
import com.foc.property.FProperty;

/**
 * @author 01Barmaja
 */
public class ListSumOperation implements ListOperation{
  protected FFieldPath fieldPath = null; 
  protected FProperty sumProp = null;
  protected double sum = 0;
  
  public ListSumOperation(FFieldPath fieldPath, FProperty sumProp){
    this.fieldPath = fieldPath; 
    this.sumProp = sumProp;
  }
  
  public void dispose(){
    if(fieldPath != null){
      fieldPath.dispose();
      fieldPath = null;
    }
    sumProp = null;
  }
  
  public void reset(){
    sum = 0;  
  }
  
  public void treatObject(FocObject obj){
    FProperty prop = fieldPath.getPropertyFromObject(obj);
    if(prop != null){
      sum += prop.getDouble();
    }
  }
  
  public void sendResult(){
    sumProp.setDouble(sum);
  }
}
