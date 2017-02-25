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
