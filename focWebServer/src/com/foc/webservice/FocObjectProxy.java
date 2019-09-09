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
