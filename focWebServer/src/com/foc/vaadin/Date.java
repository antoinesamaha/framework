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
