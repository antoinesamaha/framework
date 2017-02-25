package com.foc.vaadin;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.InlineDateField;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class Date extends VerticalLayout implements Property.ValueChangeListener{
  
  public static final Object resolution_PROPERTY_NAME = "name";

  private InlineDateField datetime;
  
  public Date() {
    setSpacing(true);

    datetime = new InlineDateField();

    // Set the value of the PopupDateField to current date
    datetime.setValue(new java.util.Date());

    // Set the correct resolution
    datetime.setResolution(InlineDateField.RESOLUTION_DAY);
    datetime.setImmediate(true);

    addComponent(datetime);
  }
  
  public void valueChange(ValueChangeEvent event) {
  	/*
    datetime.setResolution((Integer) event.getProperty().getValue());
    datetime.requestRepaint();
    */
  } 
}