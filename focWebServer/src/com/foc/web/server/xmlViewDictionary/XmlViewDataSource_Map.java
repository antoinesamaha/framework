/*******************************************************************************
 * Copyright 2016 Antoine Nicolas SAMAHA
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
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
