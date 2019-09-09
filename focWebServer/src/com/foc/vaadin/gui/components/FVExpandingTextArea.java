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

import org.vaadin.hene.expandingtextarea.ExpandingTextArea;
import org.xml.sax.Attributes;

import com.foc.property.FProperty;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentDelegate;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.vaadin.ui.Field;

@SuppressWarnings("serial")
public class FVExpandingTextArea extends ExpandingTextArea implements FocXMLGuiComponent{

	private IFocData focData = null;
	private Attributes attributes = null;
	private FocXMLGuiComponentDelegate delegate = null;
	
	public FVExpandingTextArea(FProperty property, Attributes attributes) {
		delegate = new FocXMLGuiComponentDelegate(this);
    setAttributes(attributes);
    setFocData(property);
    setRows(2);
    addStyleName("component-margin");    
    setStyleName("v-expandingtextarea");//This is to overwrite the default style of ExpandingTextArea. The default style was important for 7.1.8 but not anymore.
    //This stylename does not exist in the css it is only to remove the default PLEASE DO NOT REPLACE WITH addStyleName
	}
	
	@Override
	public void dispose() {
		focData = null;
		attributes = null;
		if(delegate != null){
			delegate.dispose();
			delegate = null;
		}
	}

	@Override
	public IFocData getFocData() {
		return focData;
	}

	@Override
	public void setFocData(IFocData focData) {
		this.focData = focData;		
	}

	@Override
	public String getXMLType() {
		return FXML.TAG_FIELD;
	}

	@Override
	public Field getFormField() {
		return this;
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
	public Attributes getAttributes() {
		return attributes;
	}

	@Override
	public void setAttributes(Attributes attributes) {
		this.attributes = attributes;
	  FocXMLGuiComponentStatic.applyAttributes(this, attributes);
    if(attributes.getValue("rows") != null) {
      setRows(Integer.parseInt(attributes.getValue("rows")));
    }
    
    if(attributes.getValue("cols") != null) {
      setColumns(Integer.parseInt(attributes.getValue("cols")));
    }		
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
	public void setDelegate(FocXMLGuiComponentDelegate delegate) {
		this.delegate = delegate;		
	}

	@Override
	public FocXMLGuiComponentDelegate getDelegate() {
		return delegate;
	}

	@Override
	public void refreshEditable() {
		setEnabled(getDelegate() != null ? getDelegate().isEditable() : true);		
	}

}
