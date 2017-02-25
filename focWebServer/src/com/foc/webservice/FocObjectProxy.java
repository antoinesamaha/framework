package com.foc.webservice;

import java.util.HashMap;

public class FocObjectProxy {
  private String[] fieldsArray = null;
  private String[] valuesArray = null;
  private HashMap<String, String> valueMap = null;
  
  public FocObjectProxy(){
    
  }
  
  public void fill(){
    valueMap = new HashMap<String, String>();
    
    valueMap.put("key 1", "Value 1");
    valueMap.put("key 2", "Value 2");
    valueMap.put("key 3", "Value 3");

    fieldsArray = new String[5];
    valuesArray = new String[5];
    for(int i=0 ; i<5; i++){
      fieldsArray[i] = "Field" + i;
      valuesArray[i] = "Value" + i;
    }
    
  }
  
  public String get(String key){
    return valueMap.get(key);
  }
  
  public String[] getFieldsArray(){
    return fieldsArray;
  }
  
  public String[] getValuesArray(){
    return valuesArray;
  }
  
}
