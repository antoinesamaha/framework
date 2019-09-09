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
import com.foc.web.unitTesting.recording.UnitTestingRecorder_TextArea;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextArea;

@SuppressWarnings({ "serial" })
public class FVTextArea extends TextArea implements FocXMLGuiComponent {

	private IFocData focData = null;
	private Attributes attributes = null;
	private FocXMLGuiComponentDelegate delegate = null;
	private UnitTestingRecorder_TextArea recorder = null;

	public FVTextArea(FProperty property, Attributes attributes) {
		delegate = new FocXMLGuiComponentDelegate(this);
		recorder = new UnitTestingRecorder_TextArea(this);
		setAttributes(attributes);
		setFocData(property);
		addStyleName("component-margin");
		if (delegate.isShowDescription()) {
			setDescription(focData.toString());
			copyMemoryToGui();
		}

		TextChangeListener textChange = new TextChangeListener() {
			@Override
			public void textChange(TextChangeEvent event) {
				if (delegate.isShowDescription()) {					
					setDescription(event.getText());
				}
			}
		};
		addTextChangeListener(textChange);

	}

	@Override
	public void dispose() {
  	if(recorder != null) {
  		recorder.dispose();
  		recorder = null;
  	}
		focData = null;
		attributes = null;
		if (delegate != null) {
			delegate.dispose();
			delegate = null;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Field getFormField() {
		return this;
	}

	@Override
	public IFocData getFocData() {
		return focData;
	}

	@Override
	public void setAttributes(Attributes attributes) {
		this.attributes = attributes;
		if(attributes != null) {
			FocXMLGuiComponentStatic.applyAttributes(this, attributes);
			if (attributes.getValue("rows") != null) {
				setRows(Integer.parseInt(attributes.getValue("rows")));
			}
	
			if (attributes.getValue("cols") != null) {
				setColumns(Integer.parseInt(attributes.getValue("cols")));
			}
			
	    if(attributes != null && attributes.getValue(FXML.ATT_PROMPT) != null){
	    	setInputPrompt(attributes.getValue(FXML.ATT_PROMPT));
	    }
		}
	}

	@Override
	public Attributes getAttributes() {
		return attributes;
	}

	@Override
	public String getXMLType() {
		return FXML.TAG_FIELD;
	}

	@Override
	public boolean copyGuiToMemory() {
		if (focData instanceof FProperty) {
			((FProperty) focData).setValue(getValue());
		}
		return false;
	}

	@Override
	public void copyMemoryToGui() {
		if (focData instanceof FProperty) {
			setValue((String) ((FProperty) focData).getValue());
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
	public void setFocData(IFocData focData) {
		this.focData = focData;
	}

	@Override
	public String getValueString() {
		return (String) getValue();
	}

	@Override
	public void setValueString(String value) {
		setValue(value);
	}

	@Override
	public void refreshEditable() {
		setEnabled(getDelegate() != null ? getDelegate().isEditable() : true);
	}

	public void refreshDescription() {
		if (getDelegate() != null && delegate.isShowDescription()) {
			setDescription(focData.toString());
		}
	}

}
