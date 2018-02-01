package com.foc.vaadin.gui.components;

import org.xml.sax.Attributes;

import com.foc.property.FProperty;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentDelegate;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.web.unitTesting.recording.UnitTestingRecorder_CheckBox;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Field;

@SuppressWarnings({ "unchecked", "serial" })
public class FVCheckBox extends CheckBox implements FocXMLGuiComponent {
  
  private IFocData focData = null;
  private Attributes attributes = null;
  private FocXMLGuiComponentDelegate delegate = null;
  private UnitTestingRecorder_CheckBox recorder = null;
  
  public FVCheckBox() {
    super();
    delegate = new FocXMLGuiComponentDelegate(this);
    recorder = new UnitTestingRecorder_CheckBox(this);
    addStyleName("component-margin");
    setImmediate(true);
  }
  
  public FVCheckBox(FProperty property, Attributes attributes) {
    delegate = new FocXMLGuiComponentDelegate(this);
    recorder = new UnitTestingRecorder_CheckBox(this);
    setFocData(property);
  	setAttributes(attributes);
  	addStyleName("component-margin");
  	setImmediate(true);
  }

  public FVCheckBox(String caption, boolean initialState) {
    super(caption, initialState);
    delegate = new FocXMLGuiComponentDelegate(this);
    recorder = new UnitTestingRecorder_CheckBox(this);
    addStyleName("component-margin");
    setImmediate(true);
  }

  public FVCheckBox(String caption) {
    super(caption);
    delegate = new FocXMLGuiComponentDelegate(this);
    recorder = new UnitTestingRecorder_CheckBox(this);
    addStyleName("component-margin");
    setImmediate(true);
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
  public String getXMLType() {
    return FXML.TAG_FIELD;
  }
  
  @Override
  public void setAttributes(Attributes attributes) {
    this.attributes = attributes;
    FocXMLGuiComponentStatic.applyAttributes(this, attributes);
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
      setValue((Boolean) ((FProperty)focData).getValue());
    }
  }

	@Override
	public Field getFormField() {
		return this;
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
  public void setFocData(IFocData focData2) {
    this.focData = focData2;
  }

  @Override
  public String getValueString() {
    boolean value = (Boolean) getValue();
    if(value == true) return "true";
    else return "false";
  }

  @Override
  public void setValueString(String value) {
    if(value.equalsIgnoreCase("true")) setValue(true);
    else if (value.equalsIgnoreCase("false")) setValue(false);
  }

	@Override
	public void refreshEditable() {
		if(getDelegate() != null){
			setEnabled(getDelegate().isEditable());
		}
	}
}
