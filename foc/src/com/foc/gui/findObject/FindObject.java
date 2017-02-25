package com.foc.gui.findObject;

import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;
import com.foc.list.FocList;

public class FindObject extends FocObject {

  public FindObject(FocConstructor constr) {
    super(constr);
    newFocProperties();
    
    if(!isStartsWith() && !isContains()){
      setStartsWith(false);
      setContains(true);
    }
  }

  public FindObject() {
  	this(new FocConstructor(FindObjectDesc.getInstance(), null));
  }
  
  public String getFindExpression(){
    return getPropertyString(FindObjectDesc.FLD_FIND);
  }

  public void setFindExpression(String expression){
    setPropertyString(FindObjectDesc.FLD_FIND, expression);
  }

  public boolean isStartsWith(){
    return getPropertyBoolean(FindObjectDesc.FLD_STARTS_WITH);
  }
  
  public boolean isContains(){
    return getPropertyBoolean(FindObjectDesc.FLD_CONTAINS);
  }
  
  public void setStartsWith(boolean val){
    setPropertyBoolean(FindObjectDesc.FLD_STARTS_WITH, val);
  }
  
  public void setContains(boolean val){
    setPropertyBoolean(FindObjectDesc.FLD_CONTAINS, val);
  }
  
  public static FindObject getFindObject(){
    FocList findObjectList = FindObjectDesc.getList(FocList.LOAD_IF_NEEDED);
    return (FindObject)findObjectList.getOrInsertAnItem();
  }
  
}
