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

import org.xml.sax.Attributes;

import com.foc.vaadin.gui.xmlForm.FXML;
import com.vaadin.ui.Component;

public class FVGuiComponent {
  public static void applyCommonAttributes(Component component, Attributes attributes){
 	 String width = attributes.getValue(FXML.ATT_WIDTH);
    if(width != null) component.setWidth(width);
    
    String height = attributes.getValue(FXML.ATT_HEIGHT);
    if(height != null) component.setHeight(height);
    
    String text    = attributes.getValue(FXML.ATT_CAPTION);
    String captPos = attributes.getValue(FXML.ATT_CAPTION_POSITION);
    
    if (captPos != null) {
      if(text != null && !captPos.equals("left") && !captPos.equals("right")) component.setCaption(text);
    } else {
      if(text != null) component.setCaption(text);
    }
  }
}
