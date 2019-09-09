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
package com.foc.vaadin.xmleditor;

import com.foc.Globals;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.components.FVTextArea;
import com.foc.vaadin.gui.layouts.FVHorizontalLayout;
import com.foc.vaadin.gui.layouts.FVVerticalLayout;
import com.foc.web.server.xmlViewDictionary.XMLView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class XMLEditor extends Window {
  private XMLView       xmlView      = null;
  
  private FVVerticalLayout layout;
  private FVHorizontalLayout buttonLayout;
  private FVTextArea editor;
  private Button save;
  private Button cancel;
  
  public XMLEditor(ICentralPanel panel, String title, String xml) {
  	this(panel != null ? panel.getXMLView() : null, title, xml);
  }
  
  public XMLEditor(XMLView xmlView, String title, String xml) {
    setXMLView(xmlView);
    layout = new FVVerticalLayout();
    buttonLayout = new FVHorizontalLayout(null);
//    buttonLayout.setMargin(true, false, false, false);
    buttonLayout.setSpacing(true);
    
    setCaption(title);

    editor = new FVTextArea(null, null);
//    editor.setRows(40);
//    editor.setColumns(80);
    editor.setWidth("100%");
    editor.setHeight("100%");
    Globals.logString("XML before editor.setValue()="+xml);
    editor.setValue(xml);
    editor.addStyleName("focXMLEditor");
    
    save = new Button("save");
    cancel = new Button("cancel");
    
    addListenersToButtons();
 
    layout.setWidth("100%");
    layout.setHeight("100%");
    layout.setSpacing(false);
    layout.addComponent(editor);
    layout.setComponentAlignment(editor, Alignment.TOP_CENTER);
    layout.setExpandRatio(editor, 1);
    
    buttonLayout.addComponent(save);
    buttonLayout.setComponentAlignment(save, Alignment.MIDDLE_CENTER);
    buttonLayout.addComponent(cancel);
    buttonLayout.setComponentAlignment(cancel, Alignment.MIDDLE_CENTER);
    
    layout.addComponent(buttonLayout);
    layout.setComponentAlignment(buttonLayout, Alignment.TOP_CENTER);
    
    setWidth("100%");
    setHeight("100%");
    setModal(true);
    
    setContent(layout);
  }

  private void addListenersToButtons() {
    save.addClickListener(new Button.ClickListener() {
      
      @Override
      public void buttonClick(ClickEvent event) {
         String newXml = editor.getValue().toString();
         
         XMLView view = getXMLView();
         view.saveXML(newXml);
         XMLEditor.this.close();
      }
    });
    
    cancel.addClickListener(new Button.ClickListener() {
      
      @Override
      public void buttonClick(ClickEvent event) {
        XMLEditor.this.close();
      }
    });
  }

	public XMLView getXMLView() {
		return xmlView;
	}

	public void setXMLView(XMLView xmlView) {
		this.xmlView = xmlView;
	}
}
