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

import org.xml.sax.Attributes;

import com.foc.ConfigInfo;
import com.foc.Globals;
import com.foc.desc.field.FBoolField;
import com.foc.property.FBoolean;
import com.foc.property.FProperty;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentDelegate;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;

@SuppressWarnings("serial")
public class FVBooleanOptionGroupHorizontal extends HorizontalLayout implements FocXMLGuiComponent {
	private FBoolField mfld = null;
  private FBoolean focData = null;
  private Attributes attributes = null;
  private FocXMLGuiComponentDelegate delegate = null;
  
  private boolean switchingOffTheRest = false;
  private ArrayList<YesNoHorizontalCheckBox> checkBoxArray = null;
    
  public FVBooleanOptionGroupHorizontal(FProperty property, Attributes attributes) {
//    super(property != null && property.getFocField() != null ? "" : "", new BeanItemContainer(FMultipleChoiceItem.class, ((FMultipleChoiceField)property.getFocField()).getChoicesCollection()));
  	delegate = new FocXMLGuiComponentDelegate(this);
  	if(property != null && property.getFocField() != null){
  		
  		checkBoxArray = new ArrayList<YesNoHorizontalCheckBox>();
  		
			YesNoHorizontalCheckBox cb = new YesNoHorizontalCheckBox(true);
			addComponent(cb);
			setComponentAlignment(cb, Alignment.MIDDLE_CENTER);
			checkBoxArray.add(cb);
  		
			cb = new YesNoHorizontalCheckBox(false);
			addComponent(cb);
			setComponentAlignment(cb, Alignment.MIDDLE_CENTER);
			checkBoxArray.add(cb);
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
				YesNoHorizontalCheckBox cb = checkBoxArray.get(i);
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
    if(attributes != null) {
    	String immediate = attributes.getValue(FXML.ATT_IMMEDIATE);
    	if(			 immediate != null 
    			&&  (immediate.equals("1") || immediate.toLowerCase().equals("true"))) {
    		if(checkBoxArray != null){
    			for(int i=0; i<checkBoxArray.size(); i++){
    				YesNoHorizontalCheckBox box = checkBoxArray.get(i);
    				if(box != null){
    					box.setImmediate(true);
    				}
    			}
    		}
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
  	YesNoHorizontalCheckBox cb = getSelectedCheckBox();
  	if(cb != null){
  		focData.setValue(cb.getItemValue());
  	} else {
  		focData.setBoolean(false);
  		focData.setValueNull_WithListener(true);
  	}
	  return false;
  }

  @Override
  public void copyMemoryToGui() {
  	if(focData.isValueNull()) {
  		switchOffTheRest(null, false);
  	} else {
      boolean mcItem = ((FBoolean)focData).getBoolean();
    	YesNoHorizontalCheckBox cb = getCheckBoxForItem(mcItem);
    	if(cb != null){
    		switchOffTheRest(cb, true);
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
    this.focData = (FBoolean) focData;
  }

  @Override
  public String getValueString() {
  	String valueStr = null;
  	try{
  		YesNoHorizontalCheckBox cb = getSelectedCheckBox();
  		if(cb == null){
  			valueStr = "null";
  		} else {
  			valueStr = cb.getItemValue() ? "true" : "false";
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
	
	private YesNoHorizontalCheckBox getSelectedCheckBox(){
		YesNoHorizontalCheckBox cb = null;
		if(checkBoxArray != null){
			for(int i=0; i<checkBoxArray.size() && cb == null; i++){
				YesNoHorizontalCheckBox box = checkBoxArray.get(i);
				if(box != null && box.getValue()){
					cb = box;
				}
			}
		}
		return cb;
	}
	
	private YesNoHorizontalCheckBox getCheckBoxForItem(boolean mcItem){
		YesNoHorizontalCheckBox cb = null;
		if(checkBoxArray != null){
			for(int i=0; i<checkBoxArray.size() && cb == null; i++){
				YesNoHorizontalCheckBox box = checkBoxArray.get(i);
				if(box != null && box.getItemValue() == mcItem){
					cb = box;
				}
			}
		}
		return cb;
	}
	
	private void switchOffTheRest(CheckBox cb, boolean theValue){
		if(checkBoxArray != null){
			switchingOffTheRest = true;
			for(int i=0; i<checkBoxArray.size(); i++){
				YesNoHorizontalCheckBox box = checkBoxArray.get(i);
				if(box != cb){
					box.setValue(false);
				} else {
					box.setValue(theValue);
				}
			}
			switchingOffTheRest = false;
		}
	}
	
	private class YesNoHorizontalCheckBox extends CheckBox implements ValueChangeListener {

		private boolean mcItem = false;
		
		public YesNoHorizontalCheckBox(boolean mcItem){
			this.mcItem = mcItem;
			String title = mcItem ? "Yes" : "No";
			if (ConfigInfo.isArabic()) {
				title = mcItem ? "نعم" : "كلا";
			}
			setCaption(title);
			
			addValueChangeListener(this);
		}
		
		public void dispose(){
			removeValueChangeListener(this);
		}

		@Override
		public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
			if(!switchingOffTheRest){
				if (getValue()) {
					switchOffTheRest(YesNoHorizontalCheckBox.this, getValue());
				}
			}
			copyGuiToMemory();
		}

		public boolean getItemValue() {
			return mcItem;
		}
	}
}
