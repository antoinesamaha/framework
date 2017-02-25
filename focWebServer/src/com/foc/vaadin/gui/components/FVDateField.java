package com.foc.vaadin.gui.components;

import java.util.Date;

import org.xml.sax.Attributes;

import com.foc.Globals;
import com.foc.desc.field.FDateField;
import com.foc.property.FDate;
import com.foc.property.FProperty;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentDelegate;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Field;

@SuppressWarnings("serial")
public class FVDateField extends DateField implements FocXMLGuiComponent {
	private String name = null;
  
	private IFocData focData   = null;
	private Attributes attributes = null;
	private FocXMLGuiComponentDelegate delegate = null;
	
  public FVDateField(FProperty property, Attributes attributes) {
    delegate = new FocXMLGuiComponentDelegate(this);
    setFocData(property);
    setAttributes(attributes);
    
    this.setSizeUndefined();
    addStyleName("component-margin");
  }

  @Override
  public void dispose(){
    focData    = null;
    attributes = null;
    if(delegate != null){
    	delegate.dispose();
    	delegate = null;
    }
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
	    		setResolution(Resolution.SECOND);
	    	}
    	}
    	
  		String fmt = "";
  		if(dateField.isMonthRelevantOnly()){
  			fmt = "MMM yyyy";  			
  		}else{
	  		if(dateField.isDateRelevant()){
	  			fmt = "dd MMM yyyy";
	  		}
	  		if(dateField.isTimeRelevant()){
	  			if(!fmt.isEmpty()){
	  				fmt += " ";
	  			}
	  			fmt += "kk:mm";
	  		}
  		}
  		setDateFormat(fmt);
  		
    }else{
    	String format = getAttributes() != null ? getAttributes().getValue(FXML.ATT_FORMAT) : null;
    	if(format == null) format = "dd MMM yyyy kk:mm";
  		setDateFormat(format);
  	}
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
