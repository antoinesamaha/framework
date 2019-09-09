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


public class TestPojo {
  private int    id           = 0;
  private double doubleMember = 0.0;
  private String stringMember = null;
  private String[] stringArrayMember = null;
  
  public TestPojo(){
    id = -1;
    doubleMember = -1.0;
    stringMember = "EMPTY";
    
    int arraySize = 5;
    
    stringArrayMember = new String[arraySize];
    for(int i=0; i<arraySize; i++){
      stringArrayMember[i] = "Test " + i;
    }
  }

  public int getID(){
    return id;
  }
  
  public void setID(int id){
    this.id = id;
  }
  
  public double getDoubleMember(){
    return doubleMember;
  }
  
  public void setDoubleMember(double doubleMember){
    this.doubleMember = doubleMember;
  }
  
  public String getStringMember(){
    return stringMember;
  }
  
  public void setStringMember(String memberString){
    stringMember = memberString;
  }
  
  public String[] getStringArrayMember(){
    return stringArrayMember;
  }
  
  
}
