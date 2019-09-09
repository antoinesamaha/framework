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

import org.xml.sax.Attributes;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.foc.vaadin.FocWebApplication;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.layouts.FVLayout;
import com.foc.vaadin.gui.layouts.FVTabbedLayout;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class EditFieldsWindow extends Window {
  
  private VerticalLayout   mainLayout    = null;
  private HorizontalLayout buttonsLayout = null;
  private TabSheet         tabLayout     = null;
  
  private Button add    = null;
  private Button save   = null;
  private Button cancel = null;
  
  
  //Used for counting tabs in the component editor
  private int count = 1;
  
  public EditFieldsWindow() {
  	
    setModal(true);
    setWidth("400px");
    setCaption("Component Editor");

    mainLayout    = new VerticalLayout();
    buttonsLayout = new HorizontalLayout();
    tabLayout     = new FVTabbedLayout(null);
    tabLayout.setSizeFull();
    
    add    = new Button("Add Field");
    save   = new Button("Save Changes");
    cancel = new Button("Cancel");
    
    initButtons();

    mainLayout.setSpacing(true);
    
    buttonsLayout.setMargin(true);
    buttonsLayout.setSpacing(true);
    buttonsLayout.addComponent(add);
    buttonsLayout.addComponent(save);
    buttonsLayout.addComponent(cancel);
    
    mainLayout.addComponent(tabLayout);
    mainLayout.addComponent(buttonsLayout);
    mainLayout.setComponentAlignment(tabLayout, Alignment.MIDDLE_CENTER);
    mainLayout.setComponentAlignment(buttonsLayout, Alignment.MIDDLE_CENTER);
    
    setContent(mainLayout);
  }
  
  private void initButtons() {
    add.addClickListener(new ClickListener() {
      
      @Override
      public void buttonClick(ClickEvent event) {
//        attributesList.add(new XMLAttributesModel("", ""));
//        container.addBean(attributesList.get(attributesList.size()-1));
      }
    });
    
    save.addClickListener(new ClickListener() {
      
      @Override
      public void buttonClick(ClickEvent event) {
        for(int i=0; i<tabLayout.getComponentCount();i++) {
          EditFieldsLayoutInTab tab = null;
          
          if(tabLayout.getTab(i).getComponent() instanceof EditFieldsLayoutInTab){
            tab = (EditFieldsLayoutInTab) tabLayout.getTab(i).getComponent();
          }
          if(tab!=null){
            tab.saveChanges();
          }  
        }
        close();
      }
    });
    
    cancel.addClickListener(new ClickListener() {
      
      @Override
      public void buttonClick(ClickEvent event) {
        close();
      }
    });
  }
  
  public void addTab(FocXMLLayout xmlLayout, Component componentEdited, FVLayout layout, String name, Attributes attributes, boolean isPalette){
    EditFieldsLayoutInTab tab = new EditFieldsLayoutInTab(xmlLayout, componentEdited, layout, name, attributes, isPalette);
    String tabName = null;
    count++;
    if(componentEdited instanceof FocXMLGuiComponent){
      FocXMLGuiComponent comp = (FocXMLGuiComponent) componentEdited;
      String xmlCaption = comp.getAttributes().getValue(FXML.ATT_CAPTION);
      String xmlName = null;
      if(xmlCaption != null){
        tabName = xmlCaption;
      }
      
      else if ((xmlName=comp.getAttributes().getValue(FXML.ATT_NAME))!=null){
        tabName = xmlName;
      }
      else{
        tabName = comp.getXMLType();
      }
    }
    else{
      tabName = "Component " + count;
    }
    tab.setSizeFull();
//    tab.setWidth("300px");
    tabLayout.addTab(tab, tabName);
  }

  public void close(){
    super.close();
    setInstanceForThread(null);
    count = 1;
  }
  
  //--------------------------------
  // STATIC THREAD LOCAL
  //--------------------------------
  private static ThreadLocal<EditFieldsWindow> threadLocalFieldsWindowSession = new ThreadLocal<EditFieldsWindow>();
  
  private static void setInstanceForThread(EditFieldsWindow editFieldWindow){
    threadLocalFieldsWindowSession.set(editFieldWindow);
  }
  
  public static EditFieldsWindow getInstanceForThread(){
    EditFieldsWindow editFieldWindow = threadLocalFieldsWindowSession.get();
    if(editFieldWindow == null){
      editFieldWindow = new EditFieldsWindow();
      FocWebApplication.getInstanceForThread().addWindow(editFieldWindow);
      setInstanceForThread(editFieldWindow);
    }
    return editFieldWindow;
  }
  //--------------------------------  

}
