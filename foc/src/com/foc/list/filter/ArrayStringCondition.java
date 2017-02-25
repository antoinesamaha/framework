//IMPLEMENTED

/*
 * Created on Sep 9, 2005
 */
package com.foc.list.filter;

import com.foc.desc.*;
import com.foc.desc.field.FFieldPath;
import com.foc.property.FPropertyArray;

/**
 * @author 01Barmaja
 */
public class ArrayStringCondition extends StringCondition{

  public ArrayStringCondition(FFieldPath stringFieldPath, String fieldPrefix){
    super(stringFieldPath, fieldPrefix);
  }
  
  // oooooooooooooooooooooooooooooooooo
  // oooooooooooooooooooooooooooooooooo
  // IMPLEMENTED
  // oooooooooooooooooooooooooooooooooo
  // oooooooooooooooooooooooooooooooooo
  
  public boolean includeObject(FocListFilter filter, FocObject object){
    boolean include = true;
    int operation = getOperation(filter);
    if(operation != StringCondition.OPERATION_NONE){
      String condText = getText(filter);
      
      FPropertyArray propArray = (FPropertyArray) getFieldPath().getPropertyFromObject(object);
      String text = propArray != null ? propArray.getString() : "";
      
      switch(operation){
        case StringCondition.OPERATION_EQUALS:
          include = text.toUpperCase().compareTo(condText.toUpperCase()) == 0;
          break;
        case StringCondition.OPERATION_CONTAINS:
          include = text.toUpperCase().contains(condText.toUpperCase());
          break;
        case StringCondition.OPERATION_STARTS_WITH:
          include = text.toUpperCase().startsWith(condText.toUpperCase());
          break;
        case StringCondition.OPERATION_EMPTY:
          include = text.trim().compareTo("") == 0;
          break;
      }
    }
    return include;
  }  
}
