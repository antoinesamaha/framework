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
