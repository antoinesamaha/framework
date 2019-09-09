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

import com.foc.Globals;
import com.foc.desc.field.FMultipleChoiceItem;
import com.foc.property.FMultipleChoice;
import com.foc.property.FProperty;
import com.foc.shared.dataStore.IFocData;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.PopupView;

@SuppressWarnings("serial")
public class FVMultipleChoiceOptionGroupPopupViewContent implements PopupView.Content{

	private FVMultipleChoiceOptionGroup multipleChoiceOptionGroup = null;
	private PopupView  popupView  = null;	
	private FProperty  property   = null;
	private Attributes attributes = null;
	
	public FVMultipleChoiceOptionGroupPopupViewContent(FProperty property, Attributes attributes) {
		this.property   = property;
		this.attributes = attributes;
	}
	
	public void dispose(){
		if(multipleChoiceOptionGroup != null){
			multipleChoiceOptionGroup.dispose();
			multipleChoiceOptionGroup = null;
		}
		property   = null;
		attributes = null;
		popupView  = null;
	}
	
	public IFocData getFocData(){
		return property;
	}

	public void setFocData(IFocData focData){
		try{
			if(focData instanceof FProperty){
				property = (FProperty) focData;
			}
		}catch(ClassCastException e){
			Globals.logExceptionWithoutPopup(e);
		}
	}
	
	@Override
	public String getMinimizedValueAsHTML() {
		String value = " - ? - ";
		if(getFocData() != null){
			value = (String) getFocData().iFocData_getValue();
		}
		String html = FVObjectPopupViewContent.getPopupDorpdownHtmlImg(value);
		return html;
	}

	@Override
	public Component getPopupComponent() {
		return getPopupComponent(true);
	}
		
	public Component getPopupComponent(boolean createIfNeeded) {
		if(multipleChoiceOptionGroup == null && createIfNeeded){
			multipleChoiceOptionGroup = new FVMultipleChoiceOptionGroup(property, attributes);
			if(getFocData() != null){
				FMultipleChoiceItem multipleChoiceItem = (FMultipleChoiceItem) ((FMultipleChoice) getFocData()).getValue();
				if(multipleChoiceItem != null){
					multipleChoiceOptionGroup.setValue(multipleChoiceItem);
				}
			}		
			multipleChoiceOptionGroup.addValueChangeListener(new ValueChangeListener() {
	      @Override
	      public void valueChange(ValueChangeEvent event) {
	      	if(event != null && event.getProperty() != null && event.getProperty().getValue() != null && event.getProperty().getValue() instanceof FMultipleChoiceItem && property != null){
						if(getPopupView() != null){
							getPopupView().setPopupVisible(false);      		
						}
	      		FMultipleChoiceItem multipleChoiceItem = (FMultipleChoiceItem) event.getProperty().getValue();
	      		if(multipleChoiceItem != null){
	      			property.setObject(multipleChoiceItem.getTitle());
	      		}
	      	}
	      }
			});
		}
		return multipleChoiceOptionGroup;
	}

	public PopupView getPopupView() {
		return popupView;
	}

	public void setPopupView(PopupView popupView) {
		this.popupView = popupView;
	}

}
