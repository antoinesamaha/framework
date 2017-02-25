package com.foc.web.server.xmlViewDictionary;

import java.util.HashMap;

import com.foc.access.AccessConsole;

public class XmlViewDataSource_Map extends AccessConsole {
  @SuppressWarnings("rawtypes")
  private HashMap dataMap = null;
  
  public XmlViewDataSource_Map(){
    dataMap = new HashMap<String, Object>();
  }
  
  public void dispose(){
    if(dataMap != null){
      dataMap.clear();
      dataMap = null;
    }
  }
  
  @SuppressWarnings("unchecked")
  public void put(String key, Object obj){
    dataMap.put(key, obj);
  }
  
  public Object get(String key){
    return dataMap.get(key);
  }
  
  @SuppressWarnings("unchecked")
  public void put(Object obj){
    dataMap.put("DEFAULT", obj);
  }
  
  public Object get(){
    return dataMap.get("DEFAULT");
  }
}
