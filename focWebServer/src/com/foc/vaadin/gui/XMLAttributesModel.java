package com.foc.vaadin.gui;

import java.io.Serializable;

public class XMLAttributesModel implements Serializable {
  
  private String key, value;
  
  public XMLAttributesModel(String key, String value) {
    this.key = key;
    this.value = value;
  }
  
  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}