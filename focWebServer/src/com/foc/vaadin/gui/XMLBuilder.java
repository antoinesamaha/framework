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
package com.foc.vaadin.gui;

import java.util.Iterator;

import org.xml.sax.Attributes;

import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.layouts.FVLayout;
import com.foc.vaadin.gui.layouts.validationLayout.FValidationSettings;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;
import com.vaadin.ui.Component;

public class XMLBuilder {
  private int           spaces    = 0;
  private StringBuilder xmlLayout = null;
  
  public XMLBuilder(FVLayout layout) {
    xmlLayout = new StringBuilder("");
    buildXMLRecursive(layout);
  }
  
  public String getXMLString(){
    return xmlLayout.toString();
  }
  
  @Override
  public String toString(){
    return getXMLString();
  }
  
  public void incrementSpaces(){
  	spaces = spaces+4;
  }

  public void decrementSpaces(){
  	spaces = spaces-4;
  	if(spaces < 0) spaces = 0;
  }
  
  public void appendSpaces() {
    String spacesStr = "";
    for (int i = 0; i < spaces; i++){
      spacesStr += " ";
    }
    append(spacesStr);
  }
  
  public void append(String string){
    xmlLayout.append(string);
  }

  public void appendLine(String string){
    appendSpaces();
    append(string);
    append("\n");
  }

  private void buildXMLRecursive(FVLayout layout) {
    if(layout != null){
      Attributes attributes = layout != null ? layout.getAttributes() : null; 
      appendLine("<"+layout.getXMLType()+" "+constructAttributes(attributes)+" >");
      incrementSpaces();
      
      layout.fillXMLNodeContent(this);
  
      Component layoutComponent = (Component) layout;
      if(layoutComponent.getParent() != null && layoutComponent.getParent() instanceof ICentralPanel){
        FValidationSettings validationSettings = ((ICentralPanel)layoutComponent.getParent()).getValidationSettings(true);
        if(validationSettings != null){
          validationSettings.fillXML(this);
        }
      }
      
      if(!layout.isXMLLeaf()){
        Iterator<Component> itr = ((FVLayout) layout).getComponentIterator();
        
        while (itr.hasNext()) {
          Component temp = itr.next();
          
          if(temp instanceof FVLayout) {
            FVLayout childLayout = (FVLayout) temp;
  
            buildXMLRecursive(childLayout);
          } else if (temp instanceof FocXMLGuiComponent) {
            attributes = ((FocXMLGuiComponent) temp).getAttributes();
            appendLine("<"+((FocXMLGuiComponent)temp).getXMLType()+" "+constructAttributes(attributes)+" />");          
          }      
        }
      }

      decrementSpaces();
      appendLine("</"+((FVLayout)layout).getXMLType()+">");
    }    
  }
  
  private String constructAttributes(Attributes attributes) {
    String ret = "";
    String q = "\"";
    
    if (attributes != null) {
      for (int i = 0; i < attributes.getLength(); i++) {
        String name = attributes.getQName(i);
        
        String value = "";
        
        if (attributes instanceof FocXMLAttributes) {
          value = ((FocXMLAttributes) attributes).getValueWithoutResolve(attributes.getIndex(name));
        } else {
          value = attributes.getValue(name);
        }
        
        
//        Globals.logString(name+" : "+value);
        
        ret += (name+"="+q+value+q+" ");
      }
    }
    
    return ret;
  }
}
