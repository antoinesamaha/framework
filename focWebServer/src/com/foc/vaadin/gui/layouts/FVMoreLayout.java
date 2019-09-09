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
package com.foc.vaadin.gui.layouts;

import org.xml.sax.Attributes;

import com.foc.ConfigInfo;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.themes.BaseTheme;

@SuppressWarnings("serial")
public class FVMoreLayout extends FVVerticalLayout {
  private boolean extended      = false;
  private Button  button        = null;
  private Attributes attributes = null;
  	
  public FVMoreLayout(Attributes attributes) {
  	super(attributes);
  	this.attributes = attributes;
  	button = new Button("More...");
  	button.setStyleName(BaseTheme.BUTTON_LINK);
  	button.addStyleName("moreBackground");
  	getMoreLayoutButton().addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				setExtended(!isExtended());
			}
		});
  	addComponent(button);
  }
  
  @Override
  public void dispose(){
  	button     = null;
    attributes = null;
  	super.dispose();
  }
  
  private Button getMoreLayoutButton(){
  	if(button == null){
	  	button = new Button("More...");
	  	button.setStyleName(BaseTheme.BUTTON_LINK);
	  	button.addStyleName("moreBackground");
  	}
  	return button;
  }

  @Override
  public void addComponent(Component c) {
  	super.addComponent(c);
  	setExtended(isExtended());
  }
  
	public Button getButton() {
		return button;
	}

	public boolean isExtended() {
		return extended;
	}
	
	public void setExtended(boolean extended) {
    this.extended = extended;
    boolean arabic = ConfigInfo.isArabic();
    String caption = arabic ? "المزيد" : "More";
    if(attributes != null && attributes.getValue(FXML.ATT_MORE_CAPTION) != null){
      caption = attributes.getValue(FXML.ATT_MORE_CAPTION);
    }
    
    if(arabic){
    	getMoreLayoutButton().setCaption(extended ? "اخف "+caption+" >>>" : " <<< اظهر "+caption);
    }else{
    	getMoreLayoutButton().setCaption(extended ? " <<< Hide "+caption : "Show "+caption+" >>>");
    }
    for(int i=0; i<getComponentCount(); i++){
      Component comp = getComponent(i);
      if(comp != getButton()){
        comp.setVisible(isExtended());
      }
    }
  }

  @Override
  public void setAttributes(Attributes attributes) {
  	super.setAttributes(attributes);
    if(attributes != null && attributes.getValue(FXML.ATT_EXPAND) != null){
    	String expand = attributes.getValue(FXML.ATT_EXPAND);
    	if(expand.equalsIgnoreCase("true")){
    		setExtended(true);
    	}
    }
  }
	
//	public void setExtended(boolean extended) {
//		this.extended = extended;
//		button.setCaption(extended ? " <<< Collapse" : "More >>>");
//		for(int i=0; i<getComponentCount(); i++){
//			Component comp = getComponent(i);
//			if(comp != getButton()){
//				comp.setVisible(isExtended());
//			}
//		}
//	}
	
  @Override
  public String getXMLType() {
    return FXML.TAG_MORE_LAYOUT;
  }
}
