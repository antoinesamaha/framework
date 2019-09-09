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
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field;

@SuppressWarnings("serial")
public class FVComboBox extends ComboBox implements FocXMLGuiComponent {
  
  private Attributes attributes = null;
  private FocXMLGuiComponentDelegate delegate = null;
  
  public FVComboBox() {
    addStyleName("component-margin");
  }
  
	@Override
	public Field getFormField() {
		return this;
	}
	
	@Override
  public Attributes getAttributes() {
    return null;
  }
	
	@Override
  public void setAttributes(Attributes attributes) {
    this.attributes = attributes;
    FocXMLGuiComponentStatic.applyAttributes(this, attributes);
  }
  
	@Override
  public FProperty getFocData() {
    return null;
  }
	
	@Override
  public String getXMLType() {
    return FXML.TAG_FIELD;
  }

  @Override
  public boolean copyGuiToMemory() {
  	return false;
  }

  @Override
  public void copyMemoryToGui() {
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
    attributes = null;
    delegate   = null;
  }

  @Override
  public void setFocData(IFocData focData) {
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
	@Deprecated
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	@Override
	public void refreshEditable() {
		if(getDelegate() != null){
			setEnabled(getDelegate().isEditable());
		}
	}
}
