package com.foc.vaadin.gui;

import java.io.Serializable;
import java.util.ArrayList;

import com.vaadin.data.util.BeanItemContainer;

public class XMLAttributesContainer extends BeanItemContainer<XMLAttributesModel> implements Serializable{
  
  public XMLAttributesContainer() throws InstantiationException, IllegalAccessException {
    super(XMLAttributesModel.class);
  }
  
  public void addAll(ArrayList<XMLAttributesModel> models) {
    for (XMLAttributesModel model : models)
      addItem(model);
  }
}
