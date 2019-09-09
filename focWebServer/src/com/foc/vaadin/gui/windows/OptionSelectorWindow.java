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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import com.fab.gui.xmlView.XMLViewDefinition;
import com.foc.Globals;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.util.Utils;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.vaadin.gui.components.FVTextField;
import com.foc.vaadin.gui.layouts.FVHorizontalLayout;
import com.foc.vaadin.gui.layouts.FVVerticalLayout;
import com.foc.vaadin.gui.layouts.validationLayout.FVViewSelector;
import com.foc.vaadin.gui.layouts.validationLayout.FVViewSelector_MenuBar;
import com.foc.web.server.xmlViewDictionary.XMLView;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class OptionSelectorWindow extends Window
{
	private FVViewSelector_MenuBar viewSelector      = null;
  private FVHorizontalLayout     horizontalLayout  = null;
  private FVVerticalLayout       verticalLayout    = null;
  private FVTextField            viewNameTextField = null;
  private FVButton               apply             = null;
  private FVButton               cancel            = null;
  
  public OptionSelectorWindow(FVViewSelector viewSelector){
  	
  }
  
  public OptionSelectorWindow(FVViewSelector_MenuBar viewSelector){
    this.viewSelector = viewSelector;
    setWidth("300px");
    setHeight("250px");
    setModal(true);
    
    setCaption("New View Name");
    
    horizontalLayout = new FVHorizontalLayout(null);
    verticalLayout   = new FVVerticalLayout(null);
    horizontalLayout.setSpacing(true);
    verticalLayout.setSpacing(true);
    horizontalLayout.setMargin(true);
    verticalLayout.setMargin(true);

    viewNameTextField = new FVTextField("New View Name");
    viewNameTextField.setWidth("200px");
    
    cancel            = new FVButton("Cancel");
    apply             = new FVButton("Apply");

    horizontalLayout.addComponent(apply);
    horizontalLayout.addComponent(cancel);
    
    verticalLayout.addComponent(viewNameTextField);
    verticalLayout.addComponent(horizontalLayout);
    
    setContent(verticalLayout);
  }
  
  private void cancelButtonListener(){
    cancel.addClickListener(new ClickListener() {
      @Override
      public void buttonClick(ClickEvent event) {
        close();
      }
    });
  }
  
  public void duplicate(){
    apply.addClickListener(new ClickListener() {
      @Override
      public void buttonClick(ClickEvent event) {
        String viewName = viewNameTextField.getValue().toString();
        XMLView oldXmlView = viewSelector.getCentralPanel().getXMLView();
        XMLViewKey oldXmlViewKey = oldXmlView.getXmlViewKey();
        XMLViewKey xmlViewKey = new XMLViewKey(oldXmlViewKey);
        xmlViewKey.setUserView(viewName);
        viewSelector.addView(viewName, true);
        XMLView newXMLView = XMLViewDictionary.getInstance().get_CreateIfNeeded(xmlViewKey);
        InputStream oldXmlViewInputStream = oldXmlView.getXMLStream_ForView();
        
        String xmlContent = null;
        try{
        	xmlContent = Utils.inputStreamToString(oldXmlViewInputStream);
        }catch(Exception e){
        	Globals.logException(e);
        }
//        String xmlContent = ""; 
//        Scanner xmlFileScfanner = new Scanner(oldXmlViewInputStream).useDelimiter("\n");
//        while(xmlFileScfanner.hasNext()){
//          xmlContent += xmlFileScfanner.next() + "\n";
//        }
        
        if(xmlContent != null){
	        XMLViewDefinition xmlViewDefinition = newXMLView.getXmlviewDefinition();
	        xmlViewDefinition.setXML(xmlContent);
	        xmlViewDefinition.setJavaClassName(oldXmlView.getJavaClassName());
	        xmlViewDefinition.validate(false);
	        close();
	//        viewSelector.setView(viewName);
	        viewSelector.selectView(viewName);
        }else{
        	close();
        }
      }
    });
    cancelButtonListener();
  }
  
  public void newViewWindow(){
    apply.addClickListener(new ClickListener() {
      @Override
      public void buttonClick(ClickEvent event) {
        String viewName = viewNameTextField.getValue().toString();
        XMLView oldXmlView = viewSelector.getCentralPanel().getXMLView();
        XMLViewKey xmlViewKey = oldXmlView.getXmlViewKey();
        xmlViewKey.setUserView(viewName);
        viewSelector.addView(viewName);
        XMLView newXMLView = XMLViewDictionary.getInstance().get_CreateIfNeeded(xmlViewKey);
        XMLViewDefinition xmlViewDefinition = newXMLView.getXmlviewDefinition();
        xmlViewDefinition.setXML("<VerticalLayout width=\"100%\" height=\"500px\">\n</VerticalLayout>");
        xmlViewDefinition.setJavaClassName(oldXmlView.getJavaClassName());
        xmlViewDefinition.validate(false);
        close();
//        viewSelector.setView(viewName);
        viewSelector.selectView(viewName);
      }
    });
    cancelButtonListener();
  }
  
  public void deleteView(String viewPropertyId){
  	viewNameTextField.setCaption("Are your sure you want to delete this view");
    viewNameTextField.setValue(viewPropertyId);
    viewNameTextField.setEnabled(false);
    apply.addClickListener(new ClickListener() {
      @Override
      public void buttonClick(ClickEvent event) {
        XMLView oldXmlView = viewSelector.getCentralPanel().getXMLView();
        XMLViewKey xmlViewKey = oldXmlView.getXmlViewKey();
        if(!XMLViewDictionary.getInstance().delete(xmlViewKey)){
        	viewSelector.removeView(xmlViewKey.getUserView());
        	viewSelector.resetCheckedMenuItems();
        	viewSelector.selectStandardView();
        }
        close();
      }
    });
    cancelButtonListener();
  }
  
  private String inputStreamToString(InputStream inputStream){
    StringBuilder builder = null;
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
      builder = new StringBuilder();
      String line = null;
      while((line = reader.readLine())!=null) {
        builder.append(line);
        builder.append("\n");
      }
    }catch (Exception e) {
      e.printStackTrace();
    } finally {
        try {
          inputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    return builder.toString();
  }
}
