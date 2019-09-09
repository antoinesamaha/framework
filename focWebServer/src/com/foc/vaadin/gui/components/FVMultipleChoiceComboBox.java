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

import java.util.Collection;
import java.util.Iterator;

import org.xml.sax.Attributes;

import com.foc.desc.field.FMultipleChoiceField;
import com.foc.desc.field.FMultipleChoiceItem;
import com.foc.property.FMultipleChoice;
import com.foc.property.FProperty;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.FVIconFactory;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentDelegate;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.web.unitTesting.recording.UnitTestingRecorder_MultipleChoice;
import com.foc.web.unitTesting.recording.UnitTestingRecorder_TextField;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field;

@SuppressWarnings("serial")
public class FVMultipleChoiceComboBox extends ComboBox implements FocXMLGuiComponent {
  private String name;
  private IFocData focData = null;
  private Attributes attributes = null;
  private FocXMLGuiComponentDelegate delegate = null;
  private UnitTestingRecorder_MultipleChoice recorder = null;

  public FVMultipleChoiceComboBox(FProperty property, Attributes attributes) {
  	delegate = new FocXMLGuiComponentDelegate(this);
  	recorder = new UnitTestingRecorder_MultipleChoice(this);
  	
  	setFilteringMode(FilteringMode.CONTAINS);
    setFocData(property);
    setAttributes(attributes);
    addStyleName("component-margin");

    setNullSelectionAllowed(false);
    
    fillMultipleChoice(property);
  }

  @Override
  public void dispose(){
  	if(recorder != null) {
  		recorder.dispose();
  		recorder = null;
  	}
    name = null;
    focData = null;
    attributes = null;
    if(delegate != null){
    	delegate.dispose();
    	delegate = null;
    }
    Container container = getContainerDataSource();
    if(container != null){
    	if(container instanceof IndexedContainer){
    		IndexedContainer indexedContainer = (IndexedContainer) container;
    		if(indexedContainer != null){
    			indexedContainer.removeAllContainerFilters();
    			indexedContainer.removeAllItems();
    			indexedContainer = null;
        }
    	}else{
    		container.removeAllItems();
    		container = null;
    	}
    }
    
  }

	protected void fillMultipleChoice(FProperty property){
		if(property != null){
	  	FMultipleChoiceField multipleChoiceField = (FMultipleChoiceField)property.getFocField();
	  	setContainerDataSource(new BeanItemContainer(FMultipleChoiceItem.class, multipleChoiceField.getChoicesCollection()));
	  	
	  	String iconDisplayMode = getDisplayIconMode();  
	  	if(iconDisplayMode != null && !iconDisplayMode.isEmpty() && !iconDisplayMode.equalsIgnoreCase("false")){
	  		
	  		
	  		Iterator<FMultipleChoiceItem> choicesItr = multipleChoiceField.getChoiceIterator();
		  	if(choicesItr != null){
		  		
		  		if(iconDisplayMode.equalsIgnoreCase(FXML.VAL_ICON_ONLY)){
		  			setItemCaptionMode(ItemCaptionMode.PROPERTY);
		  			setWidth("50px");
		  		}
		  		
		  		FVIconFactory iconFactory = FVIconFactory.getInstance();
		  		
		  		while(choicesItr.hasNext()){
		  			FMultipleChoiceItem item = choicesItr.next();
		  			String iconPath = item.getIconPath();
		  			if(iconPath != null){
		  				setItemIcon(item, iconFactory.getFVIcon_Small(iconPath));
		  			}
		  		}
		  	}
	  	}
		}
  }
  
  private String getDisplayIconMode(){
  	String showIconMode = null;
  	Attributes attributes = getAttributes();
  	if(attributes != null){
  		String value = attributes.getValue(FXML.ATT_SHOW_ICON);
  		showIconMode = value != null ? value.trim() : null;
  	}
  	return showIconMode;
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
  public IFocData getFocData() {
    return focData;
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
      setValue(((FProperty)focData).getValue());
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
  	String value = null;

  	Object objectValue = getValue();
  	if(objectValue != null && objectValue instanceof FMultipleChoiceItem) {
	  	FMultipleChoiceItem item = (FMultipleChoiceItem) getValue();
	  	if(item != null){
	  		value = item.getTitle();
	  	}
  	} else if(objectValue != null && objectValue instanceof String) {
  		value = (String) objectValue;
  	}

    return value;
  }

  @Override
  public void setValueString(String value) {
  	setComboStringValue(this, value);
  }
  
  public static void setComboStringValue(AbstractSelect comp, String value){
  	if(comp != null && comp instanceof FocXMLGuiComponent){
  		IFocData focData = ((FocXMLGuiComponent)comp).getFocData();
  		if(focData instanceof FMultipleChoice){
		    FMultipleChoice obj = (FMultipleChoice) focData;
		    if(obj != null){
		    	int intValue = 0;
		    	boolean isInteger = false;
		    	try{
		    		intValue = Integer.valueOf(value);
		    		isInteger = true;
		    	}catch(Exception e){
		    	}
		    	
		      Iterator itr = obj.getChoiceIterator();
		      String caption = null;
		      while(itr.hasNext()){
		        FMultipleChoiceItem item = (FMultipleChoiceItem) itr.next();
		        caption = item.getTitle();
		        if(caption.equals(value) || (isInteger && item.getId() == intValue)){
		          comp.setValue(item);
		          break;
		        }
		      }
		    }
  		}else if(comp instanceof FVMultipleChoiceStringField){ //focData instanceof FMultipleChoiceStringBased)
  			FVMultipleChoiceStringField mcStringBased = (FVMultipleChoiceStringField) comp;
  			
  			Collection<String> coll = (Collection<String>) comp.getItemIds();
  			if(coll != null && !coll.contains(value) && mcStringBased.isNewItemsAllowed()){
  				mcStringBased.addNewItem(value);
  			}else{
  				mcStringBased.setValue(value);
  			}
  		}
  	}
  }

	@Override
	public void refreshEditable() {
		setEnabled(getDelegate() != null ? getDelegate().isEditable() : true);
	}
}
