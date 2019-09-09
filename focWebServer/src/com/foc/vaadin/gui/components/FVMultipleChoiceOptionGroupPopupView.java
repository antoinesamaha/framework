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
import com.vaadin.ui.PopupView;

@SuppressWarnings("serial")
public class FVMultipleChoiceOptionGroupPopupView extends PopupView implements FocXMLGuiComponent{

	private FocXMLGuiComponentDelegate delegate = null;
	private Attributes attributes = null;
	
	public FVMultipleChoiceOptionGroupPopupView(FProperty property, Attributes attributes) {
			super(new FVMultipleChoiceOptionGroupPopupViewContent(property, attributes));
			getPopupViewContent().setPopupView(this);
			
			this.attributes = attributes;
			delegate = new FocXMLGuiComponentDelegate(this);
	    
	    setFocData(property);
	    setAttributes(attributes);
	    setStyleName("focPopupView_DropDownList");
	}
	
	public FVMultipleChoiceOptionGroupPopupViewContent getPopupViewContent(){
		return (FVMultipleChoiceOptionGroupPopupViewContent) getContent();
	}
	
	@Override
	public void dispose() {
		if(delegate != null){
			delegate.dispose();
			delegate = null;
		}
		FVMultipleChoiceOptionGroupPopupViewContent choiceOptionGroupPopupViewContent = getPopupViewContent();
		if(choiceOptionGroupPopupViewContent != null){
			choiceOptionGroupPopupViewContent.dispose();
			choiceOptionGroupPopupViewContent = null;
		}
		attributes = null;
	}

	@Override
	public IFocData getFocData() {
		return getPopupViewContent() != null ? getPopupViewContent().getFocData() : null;
	}

	@Override
	public void setFocData(IFocData focData) {
		if(getPopupViewContent() != null){
			getPopupViewContent().setFocData(focData);
		}
	}

	@Override
	public String getXMLType() {
		return FXML.TAG_FIELD;
	}

	@Override
	public Field getFormField(){
		FVMultipleChoiceOptionGroupPopupViewContent content = (FVMultipleChoiceOptionGroupPopupViewContent) getContent();
		FVMultipleChoiceOptionGroup multipleChoiceOptionGroup = (FVMultipleChoiceOptionGroup) (content != null ? content.getPopupComponent() : null);
		return multipleChoiceOptionGroup;
	}
	
	public FVMultipleChoiceOptionGroup getFvMultipleChoiceOptionGroup(boolean createIfNeeded){
		FVMultipleChoiceOptionGroup multipleChoiceOptionGroup = null;
		if(getPopupViewContent() != null && getPopupViewContent().getPopupComponent(createIfNeeded) != null){
			multipleChoiceOptionGroup = (FVMultipleChoiceOptionGroup) getPopupViewContent().getPopupComponent();
		}
		return multipleChoiceOptionGroup;
	}

	@Override
	public boolean copyGuiToMemory() {
		FVMultipleChoiceOptionGroup group = getFvMultipleChoiceOptionGroup(false);
		if(group != null && getFocData() instanceof FProperty){
	    ((FProperty)getFocData()).setValue(group.getValue());
	  }
		return false;
	}

	@Override
	public void copyMemoryToGui() {
		if(getFocData() instanceof FProperty){
			FVMultipleChoiceOptionGroup group = getFvMultipleChoiceOptionGroup(false);
			if(group != null){
				group.setValue(((FProperty)getFocData()).getValue());
			}
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
	}

	@Override
	public String getValueString() {
		String value = "";
		FVMultipleChoiceOptionGroupPopupViewContent content = (FVMultipleChoiceOptionGroupPopupViewContent) getContent();
		FVMultipleChoiceOptionGroup multipleChoiceOptionGroup = (FVMultipleChoiceOptionGroup) (content != null ? content.getPopupComponent() : null);
		if(multipleChoiceOptionGroup != null){
			value = multipleChoiceOptionGroup.getValueString();
		}
		return value;
	}

	@Override
	public void setValueString(String value) {
		FVMultipleChoiceOptionGroupPopupViewContent content = (FVMultipleChoiceOptionGroupPopupViewContent) getContent();
		FVMultipleChoiceOptionGroup multipleChoiceOptionGroup = (FVMultipleChoiceOptionGroup) (content != null ? content.getPopupComponent() : null);
		if(multipleChoiceOptionGroup != null){
			multipleChoiceOptionGroup.setValueString(value);
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
	public void refreshEditable() {
		setEnabled(getDelegate() != null ? getDelegate().isEditable() : true);		
	}
}
