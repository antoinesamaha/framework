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
package com.foc.vaadin.gui.windows;

import org.xml.sax.helpers.AttributesImpl;

import com.foc.vaadin.gui.layouts.FVGridLayout;
import com.foc.vaadin.gui.layouts.FVLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class CreateLayoutWindow extends Window {
  private VerticalLayout mainLayout;
  private HorizontalLayout buttonsLayout;
  private TextField width, height;
  private TextField rows = null;
  private TextField cols = null;
  private CheckBox border;
  private Button create, cancel;
  
  public CreateLayoutWindow(final FVLayout layout) {
    setModal(true);
    setWidth("250px");
    setHeight("250px");
    
    mainLayout = new VerticalLayout();
    buttonsLayout = new HorizontalLayout();
    
    width = new TextField("Width:");
    height = new TextField("Height:");
    border = new CheckBox("Border:");
    
    if (layout instanceof FVGridLayout) {
      rows = new TextField("Rows:");
      cols = new TextField("Columns:");
    }
    
    create = new Button("Create");
    cancel = new Button("Cancel");
    
    cancel.addClickListener(new ClickListener() {
      
      @Override
      public void buttonClick(ClickEvent event) {
        close();
      }
    });
    
    create.addClickListener(new ClickListener() {
      
      @Override
      public void buttonClick(ClickEvent event) {
        layout.setHeight(height.getValue().toString());
        layout.setWidth(width.getValue().toString());
        
        AttributesImpl attributesImpl = new AttributesImpl(layout.getAttributes());
        attributesImpl.addAttribute("", "name", "name", "CDATA", "null");
        attributesImpl.addAttribute("", "width", "width", "CDATA", width.getValue()+"px");
        attributesImpl.addAttribute("", "height", "height", "CDATA", height.getValue()+"px");
        
        if (rows != null) {
          ((FVGridLayout) layout).setRows(Integer.parseInt(rows.getValue().toString()));
          ((FVGridLayout) layout).setColumns(Integer.parseInt(cols.getValue().toString()));
          
          attributesImpl.addAttribute("", "cols", "cols", "CDATA", cols.getValue()+"");
          attributesImpl.addAttribute("", "rows", "rows", "CDATA", rows.getValue()+"");
        }
        
        layout.setAttributes(attributesImpl);
        
        if(border.booleanValue()) {
          layout.addStyleName("border");
        }
        
        close();
      }
    });
    
    buttonsLayout.setMargin(true);
    buttonsLayout.setSpacing(true);
    buttonsLayout.addComponent(create);
    buttonsLayout.addComponent(cancel);
    mainLayout.addComponent(width);
    mainLayout.addComponent(height);
    if (rows != null) {
      mainLayout.addComponent(rows);
      mainLayout.addComponent(cols);
      mainLayout.setComponentAlignment(rows, Alignment.MIDDLE_CENTER);
      mainLayout.setComponentAlignment(cols, Alignment.MIDDLE_CENTER);
    }
    mainLayout.addComponent(border);
    mainLayout.addComponent(buttonsLayout);
    mainLayout.setComponentAlignment(width, Alignment.MIDDLE_CENTER);
    mainLayout.setComponentAlignment(height, Alignment.MIDDLE_CENTER);
    mainLayout.setComponentAlignment(border, Alignment.MIDDLE_CENTER);
    mainLayout.setComponentAlignment(buttonsLayout, Alignment.MIDDLE_CENTER);
    setContent(mainLayout);
  }
}
