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

import com.foc.vaadin.gui.layouts.FVAbsoluteLayout;
import com.foc.vaadin.gui.layouts.FVGridLayout;
import com.foc.vaadin.gui.layouts.FVHorizontalLayout;
import com.foc.vaadin.gui.layouts.FVLayout;
import com.foc.vaadin.gui.layouts.FVVerticalLayout;
import com.foc.vaadin.gui.layouts.FVWrapperLayout;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class AddTabWindow extends Window {
  private static final String[] layoutTypes = new String[] { FXML.TAG_VERTICAL_LAYOUT, 
                                                             FXML.TAG_HORIZONTAL_LAYOUT, 
                                                             FXML.TAG_ABSOLUTE_LAYOUT, 
                                                             FXML.TAG_GRID_LAYOUT};
  
  private VerticalLayout   layout         = null;
  private VerticalLayout   optionsLayout  = null;
  private VerticalLayout   seperateLayout = null;
  private HorizontalLayout buttonsLayout  = null;
  private FVLayout         selectedLayout = null;
  
  private TextField       tabName            = null;
  private FVWrapperLayout tabNameWrapper     = null;
  private ComboBox        innerLayout        = null;
  private FVWrapperLayout innerLayoutWrapper = null;
  
  private TextField       layoutHeight  = null;
  private FVWrapperLayout heightWrapper = null;
  private TextField       layoutWidth   = null;
  private FVWrapperLayout widthWrapper  = null;
  private TextField       layoutRows    = null;
  private FVWrapperLayout rowsWrapper   = null;
  private TextField       layoutCols    = null;
  private FVWrapperLayout colsWrapper   = null;
  
  private Button create = null, cancel = null;
  
  private final String width = "70px";
  
  public AddTabWindow(final TabSheet tabSheet, final VerticalLayout addTabLayout) {
    super("Add New Tab");
    setModal(true);
    setWidth("264px");
    setHeight("272px");
    
    layout         = new VerticalLayout();
    buttonsLayout  = new HorizontalLayout();
    optionsLayout  = new VerticalLayout();
    seperateLayout = new VerticalLayout();
    
    tabName            = new TextField();
    tabNameWrapper     = new FVWrapperLayout(tabName, "Tab Name:", width);
    layoutHeight       = new TextField();
    heightWrapper      = new FVWrapperLayout(layoutHeight, "Height:", width);
    layoutWidth        = new TextField();
    widthWrapper       = new FVWrapperLayout(layoutWidth, "Width:", width);
    innerLayout        = new ComboBox();
    innerLayoutWrapper = new FVWrapperLayout(innerLayout, "Layout Type:", width);
    
    for (int i = 0; i < layoutTypes.length; i++) {
      innerLayout.addItem(layoutTypes[i]);
    }
    
    innerLayout.setImmediate(true);
    innerLayout.setNullSelectionAllowed(false);
    innerLayout.setValue(layoutTypes[0]);
    
    innerLayout.addValueChangeListener(new Property.ValueChangeListener() {
      
      @Override
      public void valueChange(ValueChangeEvent event) {
        String layoutType = event.getProperty().toString();
        
        if (layoutType.equals(FXML.TAG_GRID_LAYOUT)) {
          layoutCols = new TextField();
          colsWrapper = new FVWrapperLayout(layoutCols, "Columns:", width);
          layoutRows = new TextField();
          rowsWrapper = new FVWrapperLayout(layoutRows, "Rows:", width);
          
          optionsLayout.addComponent(colsWrapper);
          optionsLayout.addComponent(rowsWrapper);
          
          setHeight("350px");
          
        } else {
          
          if (layoutCols != null) {
            optionsLayout.removeComponent(colsWrapper);
            layoutCols = null;
            colsWrapper = null;
          }
          
          if (layoutRows != null) {
            optionsLayout.removeComponent(rowsWrapper);
            layoutRows = null;
            rowsWrapper = null;
          }
          
          setHeight("272px");
        }
      }
    });
    
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
        String layoutType = innerLayout.getValue().toString();

        AttributesImpl attributes = new AttributesImpl();
        
        attributes.addAttribute("", "name", "name", "CDATA", "null");
        attributes.addAttribute("", "height", "height", "CDATA", layoutHeight.getValue().toString().isEmpty() ? "-1px" : layoutHeight.getValue().toString()+"px");
        attributes.addAttribute("", "width", "width", "CDATA", layoutWidth.getValue().toString().isEmpty() ? "-1px" : layoutWidth.getValue().toString()+"px");
        
        if (layoutType.equals(FXML.TAG_ABSOLUTE_LAYOUT)) {
          selectedLayout = new FVAbsoluteLayout(attributes);
        } else if (layoutType.equals(FXML.TAG_VERTICAL_LAYOUT)) {
          selectedLayout = new FVVerticalLayout(attributes);
        } else if (layoutType.equals(FXML.TAG_HORIZONTAL_LAYOUT)) {
          selectedLayout = new FVHorizontalLayout(attributes);
        } else {
          
          attributes.addAttribute("", "rows", "rows", "CDATA", layoutRows.getValue().toString());
          attributes.addAttribute("", "cols", "cols", "CDATA", layoutCols.getValue().toString());
          
          selectedLayout = new FVGridLayout(attributes);
        }
        
        tabSheet.removeTab(tabSheet.getTab(addTabLayout));
        tabSheet.addTab((Component) selectedLayout, tabName.getValue().toString());
        tabSheet.addTab(addTabLayout, "+");
        close();
      }
    });
    
    optionsLayout.setSpacing(true);
    optionsLayout.addComponent(widthWrapper);
    optionsLayout.addComponent(heightWrapper);
    
    seperateLayout.setHeight("25px");
    
    buttonsLayout.setSpacing(true);
    buttonsLayout.addComponent(create);
    buttonsLayout.addComponent(cancel);
    
    layout.setSpacing(true);
    layout.addComponent(tabNameWrapper);
    layout.addComponent(innerLayoutWrapper);
    layout.addComponent(optionsLayout);
    layout.addComponent(seperateLayout);
    layout.addComponent(buttonsLayout);
    
    layout.setComponentAlignment(buttonsLayout, Alignment.MIDDLE_CENTER);
    
    setContent(layout);
  }
}
