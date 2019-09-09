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

import com.foc.vaadin.gui.components.FVCheckBox;
import com.foc.vaadin.gui.components.FVLabel;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class LineLabelCreator extends Window {
  private VerticalLayout layout;
  private HorizontalLayout buttonsLayout;
  private TextField lineHeight;
  private TextField lineWidth;

  private Button create, cancel;
  
  public LineLabelCreator(final FVLabel label) {
    setModal(true);
    setWidth("250px");
    setHeight("200px");
    
    layout = new VerticalLayout();
    buttonsLayout = new HorizontalLayout();
    
    lineHeight = new TextField("Line Height:");
    lineWidth = new TextField("Line Width:");
    
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
        AttributesImpl attributes = new AttributesImpl();
        attributes.addAttribute("", "name", "name", "CDATA", "null");
        attributes.addAttribute("", "value", "value", "CDATA", " ");
        attributes.addAttribute("", "bgColor", "bgColor", "CDATA", "#CCCCCC");
        
        if(!lineHeight.getValue().toString().isEmpty()) {
          attributes.addAttribute("", "height", "height", "CDATA", lineHeight.getValue()+"px");
        }
        
        if(!lineWidth.getValue().toString().isEmpty()) {
          attributes.addAttribute("", "width", "width", "CDATA", lineWidth.getValue()+"px");
        }
        
        label.setAttributes(attributes);
        label.initLabel();
        
        label.requestRepaint();

        close();
      }
    });
    
    buttonsLayout.setMargin(true);
    buttonsLayout.setSpacing(true);
    buttonsLayout.addComponent(create);
    buttonsLayout.addComponent(cancel);
    layout.addComponent(lineHeight);
    layout.addComponent(lineWidth);
    layout.addComponent(buttonsLayout);
    layout.setComponentAlignment(lineHeight, Alignment.MIDDLE_CENTER);
    layout.setComponentAlignment(lineWidth, Alignment.MIDDLE_CENTER);
    layout.setComponentAlignment(buttonsLayout, Alignment.MIDDLE_CENTER);
    setContent(layout);
  }
  
}
