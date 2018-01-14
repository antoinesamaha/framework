//IMPLEMENTED

/*
 * Created on Sep 9, 2005
 */
package com.foc.list.filter;

import com.foc.desc.field.FFieldPath;

/**
 * @author 01Barmaja
 */
public class LongCondition extends NumCondition {
  
  public LongCondition(FFieldPath numFieldPath, String fieldPrefix){
    super(numFieldPath, fieldPrefix);
  }

  public LongCondition(int fieldID){
  	super(fieldID);
  }

  public void setToValue(FocListFilter filter, int operation, long firstValue, long lastValue){
  	setToValue(filter, operation, (double) firstValue, (double) lastValue);
  }

}
