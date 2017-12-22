package com.foc.vaadin.gui.components;

import java.util.Collection;

import org.xml.sax.Attributes;

import com.foc.property.FProperty;
import com.foc.shared.dataStore.IFocData;
import com.foc.util.Utils;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentDelegate;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.web.unitTesting.FocUnitRecorder;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Button;
import com.vaadin.ui.Field;

@SuppressWarnings({ "serial", "unchecked" })
public class FVButton extends Button implements FocXMLGuiComponent, Field {//Field implementation only to allow helpContext to work on our FVButtons
  
  private Attributes                 attributes = null;
  private FocXMLGuiComponentDelegate delegate   = null;
  
  public FVButton(Attributes attributes) {
  	delegate = new FocXMLGuiComponentDelegate(this);
    setAttributes(attributes);
    addStyleName("component-margin");
    disableDoubleClick();
  }
  
  public FVButton(String content) {
    super(content);
    delegate = new FocXMLGuiComponentDelegate(this);
    addStyleName("component-margin");
    disableDoubleClick();
  }

  public FVButton(String string, ClickListener clickListener) {
  	super(string, clickListener);
  	delegate = new FocXMLGuiComponentDelegate(this);
  	addStyleName("component-margin");
  	disableDoubleClick();
	}

  private void disableDoubleClick() {
  	setDisableOnClick(true);
  	addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				setEnabled(true);
			}
		});
  }
  
  private void setUnitTestingRecordingListener(){
  	if(FocUnitRecorder.isAllowRecording()){
  		addClickListener(new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					if(getDelegate() != null && !Utils.isStringEmpty(getDelegate().getNameInMap())){
						FocUnitRecorder.recordLine("cmd.button_Click(\""+getDelegate().getNameInMap()+"\")");
					}
				}
			});
  	}
  }
  
  @Override
  public void setDescription(String description) {
  	super.setDescription(description);
  }
  
	@Override
  public Attributes getAttributes() {
    return attributes;
  }
	
	@Override
  public FProperty getFocData() {
    return null;
  }

  @Override
  public String getXMLType() {
    return FXML.TAG_BUTTON;
  }

  @Override
  public void setAttributes(Attributes attributes) {
    this.attributes = attributes;
    FocXMLGuiComponentStatic.applyAttributes(this, attributes);
    
    String isHTMLCaption = attributes != null ? attributes.getValue(FXML.ATT_IS_HTML) : null;
    if(isHTMLCaption != null){
    	if(isHTMLCaption.toLowerCase().trim().equals("true") || isHTMLCaption.toLowerCase().trim().equals("1")){
    		setCaptionAsHtml(true);
    	}
    }
  }

  @Override
  public boolean copyGuiToMemory() {
  	return false;
  }

  @Override
  public void copyMemoryToGui() {
  }

	@Override
	public Field getFormField() {
		return null;
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
    if(delegate != null){
    	delegate.dispose();
    	delegate = null;
    }
  }

  @Override
  public void setFocData(IFocData focData) {
  }

  @Override
  public String getValueString() {
    // TODO Auto-generated method stub
    return getCaption();
  }

  @Override
  public void setValueString(String value) {
    // TODO Auto-generated method stub
    
  }

	@Override
	public void refreshEditable() {
	}

	//------------------------------------------------------------
	// This implementation of Field is only to allow the Help Context to be applicable to FVButton  
	//-------------------------------- Field ---------------------
	//-------------------------------- Field ---------------------
	//-------------------------------- Field ---------------------
	@Override
	public boolean isInvalidCommitted() {	return false;	}
	@Override
	public void setInvalidCommitted(boolean isCommitted) {	}
	@Override
	public void commit() throws SourceException, InvalidValueException {	}
	@Override
	public void discard() throws SourceException {	}
	@Override
	public void setBuffered(boolean buffered) {	}
	@Override
	public boolean isBuffered() {		return false;	}
	@Override
	public boolean isModified() {		return false;	}
	@Override
	public void addValidator(Validator validator) {	}
	@Override
	public void removeValidator(Validator validator) {	}
	@Override
	public void removeAllValidators() {	}
	@Override
	public Collection<Validator> getValidators() {		return null;	}
	@Override
	public boolean isValid() {		return false;	}
	@Override
	public void validate() throws InvalidValueException {	}
	@Override
	public boolean isInvalidAllowed() {		return false;	}
	@Override
	public void setInvalidAllowed(boolean invalidValueAllowed) throws UnsupportedOperationException {	}
	@Override
	public Object getValue() {		return null;	}
	@Override
	public void setValue(Object newValue) throws ReadOnlyException {	}
	@Override
	public Class getType() {		return null;	}
	@Override
	public void addValueChangeListener(ValueChangeListener listener) {	}
	@Override
	@Deprecated
	public void addListener(ValueChangeListener listener) {	}
	@Override
	public void removeValueChangeListener(ValueChangeListener listener) {	}
	@Override
	@Deprecated
	public void removeListener(ValueChangeListener listener) {	}
	@Override
	public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {	}
	@Override
	public void setPropertyDataSource(Property newDataSource) {	}
	@Override
	public Property getPropertyDataSource() {		return null;	}
	@Override
	public boolean isRequired() {		return false;	}
	@Override
	public void setRequired(boolean required) {	}
	@Override
	public void setRequiredError(String requiredMessage) {	}
	@Override
	public String getRequiredError() {		return null;	}
	@Override
	public boolean isEmpty() {		return false;	}
	@Override
	public void clear() {	}		
	//-------------------------------- Field ---------------------
	//-------------------------------- Field ---------------------
	//-------------------------------- Field ---------------------

}
