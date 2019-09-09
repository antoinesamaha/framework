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
package com.foc.vaadin.gui.components.multipleChoiceOptionGroupHorizontal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.xml.sax.Attributes;

import com.foc.Globals;
import com.foc.desc.field.FMultipleChoiceField;
import com.foc.desc.field.FMultipleChoiceItem;
import com.foc.desc.field.FMultipleChoiceItemInterface;
import com.foc.property.FProperty;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentDelegate;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.layouts.FVHorizontalLayout;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.OptionGroup;

@SuppressWarnings("serial")
public class FVMultipleChoiceOptionGroupHorizontal extends HorizontalLayout implements FocXMLGuiComponent {
	private FMultipleChoiceField mfld = null;
  private IFocData focData = null;
  private Attributes attributes = null;
  private FocXMLGuiComponentDelegate delegate = null;
  
  private boolean switchingOffTheRest = false;
  private ArrayList<OptionGroupHorizontalCheckBox> checkBoxArray = null;
    
  public FVMultipleChoiceOptionGroupHorizontal(FProperty property, Attributes attributes) {
//    super(property != null && property.getFocField() != null ? "" : "", new BeanItemContainer(FMultipleChoiceItem.class, ((FMultipleChoiceField)property.getFocField()).getChoicesCollection()));
  	delegate = new FocXMLGuiComponentDelegate(this);
  	if(property != null && property.getFocField() != null){
  		
  		checkBoxArray = new ArrayList<OptionGroupHorizontalCheckBox>();
  		
  		Collection<FMultipleChoiceItemInterface> coll = ((FMultipleChoiceField)property.getFocField()).getChoicesCollection();
  		Iterator iter = coll.iterator();
  		while(iter != null && iter.hasNext()){
  			FMultipleChoiceItemInterface item = (FMultipleChoiceItemInterface) iter.next();
  			
  			OptionGroupHorizontalCheckBox cb = new OptionGroupHorizontalCheckBox(item);
  			addComponent(cb);
  			setComponentAlignment(cb, Alignment.MIDDLE_CENTER);
  			
  			checkBoxArray.add(cb);
  		}
   	}
    
    setFocData(property);
    setAttributes(attributes);
    
    addStyleName("component-margin");
  }
  
  @Override
  public void dispose(){
    mfld = null;
    focData = null;
    attributes = null;
    if(delegate != null){
    	delegate.dispose();
    	delegate = null;
    }
    
		if(checkBoxArray != null){
			for(int i=0; i<checkBoxArray.size(); i++){
				OptionGroupHorizontalCheckBox cb = checkBoxArray.get(i);
				cb.dispose();
			}
			checkBoxArray.clear();
			checkBoxArray = null;
		}
  }

	@Override
	public Field getFormField() {
		return null;
	}
	
	@Override
  public IFocData getFocData() {
    return focData;
  }
	
	@Override
  public void setAttributes(Attributes attributes) {
    this.attributes = attributes;
    FocXMLGuiComponentStatic.applyAttributes(this, attributes);
//    if(attributes != null){
//    	String direction = attributes.getValue(FXML.ATT_DIRECTION);
//    	if(direction != null && direction.toLowerCase().equals(FXML.VAL_DIRECTION_HORIZONTAL)){
//    		addStyleName("horizontal");
//    	}
//    }
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
	  if(focData instanceof FProperty){
	  	OptionGroupHorizontalCheckBox cb = getSelectedCheckBox();
	  	if(cb != null){
	  		((FProperty)focData).setValue(cb.getMultipleChoiceItem());
	  	}
	  }
	  return false;
  }

  @Override
  public void copyMemoryToGui() {
    if(focData instanceof FProperty){
    	FMultipleChoiceItemInterface mcItem = (FMultipleChoiceItemInterface) ((FProperty)focData).getValue();
    	OptionGroupHorizontalCheckBox cb = getCheckBoxForItem(mcItem);
    	if(cb != null){
    		switchOffTheRest(cb);
    	}
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
  	String valueStr = null;
  	try{
  		FMultipleChoiceItemInterface value = null; 
  		OptionGroupHorizontalCheckBox cb = getSelectedCheckBox();
  		if(cb != null){
  			value = cb.getMultipleChoiceItem();
  		}
	  	if(value != null){
	  		valueStr = value.getTitle();
	  	}
  	}catch(Exception e){
  		Globals.logException(e);
  	}
    return valueStr;
  }

  @Override
  public void setValueString(String value) {
  	//ICI
//  	FVMultipleChoiceComboBox.setComboStringValue(this, value);
  }
  
	@Override
	public void refreshEditable() {
		setEnabled(getDelegate() != null ? getDelegate().isEditable() : true);
	}
	
	private OptionGroupHorizontalCheckBox getSelectedCheckBox(){
		OptionGroupHorizontalCheckBox cb = null;
		if(checkBoxArray != null){
			for(int i=0; i<checkBoxArray.size() && cb == null; i++){
				OptionGroupHorizontalCheckBox box = checkBoxArray.get(i);
				if(box != null && box.getValue()){
					cb = box;
				}
			}
		}
		return cb;
	}
	
	private OptionGroupHorizontalCheckBox getCheckBoxForItem(FMultipleChoiceItemInterface mcItem){
		OptionGroupHorizontalCheckBox cb = null;
		if(checkBoxArray != null){
			for(int i=0; i<checkBoxArray.size() && cb == null; i++){
				OptionGroupHorizontalCheckBox box = checkBoxArray.get(i);
				if(box != null && box.getMultipleChoiceItem() == mcItem){
					cb = box;
				}
			}
		}
		return cb;
	}
	
	private void switchOffTheRest(CheckBox cb){
		if(checkBoxArray != null){
			switchingOffTheRest = true;
			for(int i=0; i<checkBoxArray.size(); i++){
				OptionGroupHorizontalCheckBox box = checkBoxArray.get(i);
				if(box != cb){
					box.setValue(false);
				}else{
					box.setValue(true);
				}
			}
			switchingOffTheRest = false;
		}
	}
	
	private class OptionGroupHorizontalCheckBox extends CheckBox implements ValueChangeListener {

		private FMultipleChoiceItemInterface mcItem = null;
		
		public OptionGroupHorizontalCheckBox(FMultipleChoiceItemInterface mcItem){
			this.mcItem = mcItem;
			if(mcItem != null){
				String title = mcItem.getTitle();
				setCaption(title);
			}
			addValueChangeListener(this);
		}
		
		public void dispose(){
			removeValueChangeListener(this);
			mcItem = null;
		}

		@Override
		public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
			if(!switchingOffTheRest){
//				super.valueChange(event);
				switchOffTheRest(OptionGroupHorizontalCheckBox.this);
			}
			copyGuiToMemory();
		}

		public FMultipleChoiceItemInterface getMultipleChoiceItem() {
			return mcItem;
		}
	}
}
