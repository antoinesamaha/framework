package com.foc.vaadin.gui.components;

import org.xml.sax.Attributes;

import com.foc.Globals;
import com.foc.desc.field.FMultipleChoiceField;
import com.foc.desc.field.FMultipleChoiceItem;
import com.foc.property.FProperty;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentDelegate;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.web.unitTesting.recording.UnitTestingRecorder_OptionGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Field;
import com.vaadin.ui.OptionGroup;

@SuppressWarnings("serial")
public class FVMultipleChoiceOptionGroup extends OptionGroup implements FocXMLGuiComponent {
	private FMultipleChoiceField mfld = null;
  private IFocData focData = null;
  private Attributes attributes = null;
  private FocXMLGuiComponentDelegate delegate = null;
  private UnitTestingRecorder_OptionGroup recorder = null;
    
  public FVMultipleChoiceOptionGroup(FProperty property, Attributes attributes) {
//    super(property != null && property.getFocField() != null ? "" : "", new BeanItemContainer(FMultipleChoiceItem.class, ((FMultipleChoiceField)property.getFocField()).getChoicesCollection()));
  	delegate = new FocXMLGuiComponentDelegate(this);
  	recorder = new UnitTestingRecorder_OptionGroup(this);
  	if(property != null && property.getFocField() != null){
  		setContainerDataSource(new BeanItemContainer(FMultipleChoiceItem.class, ((FMultipleChoiceField)property.getFocField()).getChoicesCollection()));
   	}
    
    setFocData(property);
    setAttributes(attributes);
    
    addStyleName("component-margin");
  }
  
  @Override
  public void dispose(){
  	if(recorder != null) {
  		recorder.dispose();
  		recorder = null;
  	}
    mfld = null;
    focData = null;
    attributes = null;
    if(delegate != null){
    	delegate.dispose();
    	delegate = null;
    }
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
  	String valueStr = null;
  	try{
	  	FMultipleChoiceItem value = (FMultipleChoiceItem) getValue();
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
  	FVMultipleChoiceComboBox.setComboStringValue(this, value);
  }
  
	@Override
	public void refreshEditable() {
		setEnabled(getDelegate() != null ? getDelegate().isEditable() : true);
	}  
}
