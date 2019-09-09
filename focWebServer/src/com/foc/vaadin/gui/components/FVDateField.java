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

import java.util.Date;
import java.util.GregorianCalendar;

import org.xml.sax.Attributes;

import com.foc.ConfigInfo;
import com.foc.Globals;
import com.foc.desc.field.FDateField;
import com.foc.property.FDate;
import com.foc.property.FProperty;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentDelegate;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.web.unitTesting.recording.UnitTestingRecorder_DateField;
import com.vaadin.data.util.converter.Converter.ConversionException;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Field;
import com.vaadin.ui.PopupDateField;

@SuppressWarnings("serial")
public class FVDateField extends PopupDateField implements FocXMLGuiComponent {
	private String name = null;
  
	private IFocData focData   = null;
	private Attributes attributes = null;
	private FocXMLGuiComponentDelegate delegate = null;
	private UnitTestingRecorder_DateField recorder = null;
	
  public FVDateField(FProperty property, Attributes attributes) {
    delegate = new FocXMLGuiComponentDelegate(this);
    recorder = new UnitTestingRecorder_DateField(this);
    
    setFocData(property);
    setAttributes(attributes);
    
    this.setSizeUndefined();
    addStyleName("component-margin");
    
    FDateField dateField = null;
    if(property != null && property.getFocField() instanceof FDateField) {
    	dateField = (FDateField) property.getFocField();
    }
    adjustDateFormat(dateField);
  }

  @Override
  public void dispose(){
  	if(recorder != null) {
  		recorder.dispose();
  		recorder = null;
  	}
    focData    = null;
    attributes = null;
    if(delegate != null){
    	delegate.dispose();
    	delegate = null;
    }
  }

  @Override
  protected Date handleUnparsableDateString(java.lang.String dateString) throws ConversionException {
		// Try custom parsing
  	String fields[] = null;
  	if(dateString.contains("/")) {
  		fields = dateString.split("/");
  	}else if(dateString.contains("-")) {
  		fields = dateString.split("-");
  	}else if(dateString.contains(" ")) {
  		fields = dateString.split(" ");
  	}
		
		if(fields != null && fields.length >= 3){
			try{
				int day = Integer.parseInt(fields[0]);
				int month = Integer.parseInt(fields[1]) - 1;
				int year = Integer.parseInt(fields[2]);
				GregorianCalendar c = new GregorianCalendar(year, month, day);
				return c.getTime();
			}catch (NumberFormatException e){
				throw new ConversionException("Not a number");
			}
		}

		// Bad date
		throw new ConversionException("Your date needs two slashes");
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
  public Attributes getAttributes() {
    return attributes;
  }
	
	@Override
  public void setAttributes(Attributes attributes) {
    this.attributes = attributes;
    FocXMLGuiComponentStatic.applyAttributes(this, attributes);
    if(getWidth() == -1){
    	if(Globals.isValo()){
    		setWidth("140px");
    	}else{
    		setWidth("110px");	
    	}
    }
  }
	
	@Override
  public String getXMLType() {
    return focData != null ? FXML.TAG_FIELD : FXML.TAG_DATE_FIELD;
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
    	try{
      	setValue((Date) ((FProperty)focData).getValue());
    	}catch(Exception e){
    	 	Globals.logException(e);
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
    
    FProperty  dateProp  = null;
    FDateField dateField = null;
    		
    if(focData instanceof FProperty){
    	dateProp  = (FProperty) focData;
    	dateField = dateProp != null ? (FDateField) dateProp.getFocField() : null;
    }else if(focData instanceof FDateField){
    	dateField = (FDateField) focData;
    }
    
    if(dateField != null){
    	if(dateField.isMonthRelevantOnly()){
    		setResolution(Resolution.YEAR);
    		setResolution(Resolution.MONTH);
    	}else{
	    	if(dateField.isDateRelevant()){
	    		setResolution(Resolution.YEAR);
	    		setResolution(Resolution.MONTH);
	    		setResolution(Resolution.DAY);
	    	}
	    	
	    	if(dateField.isTimeRelevant()){
	    		setResolution(Resolution.HOUR);
	    		setResolution(Resolution.MINUTE);
//	    		setResolution(Resolution.SECOND);
	    	}
    	}
    }
    
    adjustDateFormat(dateField);
  }

  private void adjustDateFormat(FDateField dateField) {
  	String fmt = newDateFormat_Internal(dateField);
		setDateFormat(fmt);
  }
  
  private String newDateFormat_Internal(FDateField dateField) {
  	String fmt = "";
  	if(dateField != null){
  		fmt = dateField.newDateFormat();
	  }else{
	  	fmt = getAttributes() != null ? getAttributes().getValue(FXML.ATT_FORMAT) : null;
		  if(fmt == null) {
		  	if(ConfigInfo.isArabic()) {
		  		fmt = FDateField.RTL+"dd"+FDateField.SEPARATOR+"MMM"+FDateField.SEPARATOR+"yyyy"+" "+"HH:mm";
		  	} else {
		  		fmt = "dd MMM yyyy HH:mm";
		  	}
		  }
	  }

  	return fmt;
  }
  
  @Override
  public String getValueString() {
  	Date date = (Date) getValue();
  	return FDate.convertDateToDisplayString(date);
  }

  @Override
  public void setValueString(String value) {//"17:00"
  	Date date = FDate.convertDisplayStringToDate(value);
  	if(date != null){
  		setValue(date);
  	}
  }

	@Override
	public void refreshEditable() {
		if(getDelegate() != null){
			setEnabled(getDelegate().isEditable());
		}
	}
}
