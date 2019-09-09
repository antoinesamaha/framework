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
package com.foc.vaadin.gui.components;

import org.xml.sax.Attributes;

import com.foc.property.FProperty;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentDelegate;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.vaadin.ui.Field;
import com.vaadin.ui.RichTextArea;

@SuppressWarnings({ "serial", "unchecked" })
public class FVRichTextArea extends RichTextArea implements FocXMLGuiComponent {
  
  private String name;
  private Attributes attributes;
  private IFocData focData = null;
  private FocXMLGuiComponentDelegate delegate = null;
  
  public FVRichTextArea(FProperty property, Attributes attributes) {
    delegate = new FocXMLGuiComponentDelegate(this);
    setFocData(property);
    setAttributes(attributes);
    
    if(			attributes == null 
    		|| 	attributes.getValue(FXML.ATT_TEXT_AREA_TOOLBAR) == null 
    		||  !attributes.getValue(FXML.ATT_TEXT_AREA_TOOLBAR).trim().toLowerCase().equals("true")) {
    	addStyleName("no-toolbar");
    }
    addStyleName("component-margin");
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public Field getFormField() {
    return this;
  }
  
  @Override
  public void setAttributes(Attributes attributes) {
    this.attributes = attributes;
  	FocXMLGuiComponentStatic.applyAttributes(this, attributes);
  }
  
  @Override
  public Attributes getAttributes() {
    return attributes;
  }
  
  @Override
  public IFocData getFocData() {
    return focData;
  }
  
  @Override
  public String getXMLType() {
    return FXML.TAG_FIELD;
  }

  @Override
  public boolean copyGuiToMemory() {
    if(focData instanceof FProperty){
      ((FProperty)focData).setValue(getValue());
    }
    return false;
  }

  @Override
  public void copyMemoryToGui() {
    if(focData instanceof FProperty){
      setValue((String) ((FProperty)focData).getValue());
    }
  }
  
  @Override
  public void setDelegate(FocXMLGuiComponentDelegate delegate) {
    this.delegate = delegate;
  }

  @Override
  public FocXMLGuiComponentDelegate getDelegate() {
    return delegate;
  }
  
  @Override
  public void dispose(){
    name       = null;
    attributes = null;
    focData   = null;
		if(delegate != null){
			delegate.dispose();
			delegate = null;
		}
  }

  @Override
  public void setFocData(IFocData focData) {
    this.focData = focData;
  }

  @Override
  public String getValueString() {
    return (String)getValue();
  }

  @Override
  public void setValueString(String value) {
    setValue(value);
  }
  
	@Override
	public void refreshEditable() {
		setEnabled(getDelegate() != null ? getDelegate().isEditable() : true);
	}
}
