package com.foc.vaadin.gui.components;

import org.xml.sax.Attributes;

import com.foc.desc.field.FField;
import com.foc.desc.field.FIntField;
import com.foc.desc.field.FNumField;
import com.foc.desc.field.FStringField;
import com.foc.property.FProperty;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentDelegate;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.components.validator.FVStringValidator;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.web.unitTesting.recording.UnitTestingRecorder_Button;
import com.foc.web.unitTesting.recording.UnitTestingRecorder_TextField;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextField;

@SuppressWarnings({ "serial"})
public class FVTextField extends TextField implements FocXMLGuiComponent {
  
  private String     name       = null;
  
  private IFocData   focData    = null;
  private Attributes attributes = null;
  private FocXMLGuiComponentDelegate delegate    = null;
  private UnitTestingRecorder_TextField recorder = null;
  
  private boolean callFocus             = true;
  private long    lastTimeFocusListener = 0;
  private boolean descriptionEqualValue = false;
  
  public FVTextField(FProperty property, Attributes attributes) {
    delegate = new FocXMLGuiComponentDelegate(this);
    recorder = new UnitTestingRecorder_TextField(this);
  	setFocData(property);
  	setAttributes(attributes);
  	if(property != null && property.getFocField() instanceof FStringField){
	  	FStringField cFld = (FStringField) property.getFocField();
	  	if(cFld.isCapital()){
	  		FVStringValidator strValidator = new FVStringValidator();
	  		strValidator.setCapital(true);
	  		addValidator(strValidator);
	  	}
  	}
  	if(property != null && (property.getFocField() instanceof FNumField || property.getFocField() instanceof FIntField)){
  	  addStyleName("numfield");
  	}
  	selectAllContentListener();
  	addStyleName("component-margin");
  }
  
  public FVTextField() {
    super();
    delegate = new FocXMLGuiComponentDelegate(this);
    recorder = new UnitTestingRecorder_TextField(this);
    addStyleName("component-margin");
    selectAllContentListener();
  }
  
  public FVTextField(String content) {
    super(content);
    delegate = new FocXMLGuiComponentDelegate(this);
    recorder = new UnitTestingRecorder_TextField(this);
    addStyleName("component-margin");
  }

  @Override
  public void dispose(){
  	if(recorder != null) {
  		recorder.dispose();
  		recorder = null;
  	}
    focData   = null;
    attributes = null;
		if(delegate != null){
			delegate.dispose();
			delegate = null;
		}    
  }
  
  @Override
  public void focus() {
  	if (isCallFocus()) {
  		super.focus();
  	}
  }
  
  @Override
  public void markAsDirty() {
  	//We cannot put the condition
  	//if (isCallFocus())
  	//because then the select all will stop working.
  	//This is why we put this delay thing so that the infinite loop would be broken
  	if(lastTimeFocusListener == 0 || lastTimeFocusListener < System.currentTimeMillis() - 1500){
  		super.markAsDirty();
  		lastTimeFocusListener = System.currentTimeMillis();
  	}
  }

  protected boolean isWithSelectAllListenerOnFocus(){
  	return false;
  }
  
  private void selectAllContentListener(){
  	if(isWithSelectAllListenerOnFocus()){
	  	addFocusListener(new FocusListener() {
				public void focus(FocusEvent event) {
//					Globals.logString("Focus - IN");
					
					//Was seen on web maybe will solv the inifit toggle of focus between component.
					
					//DANGER
//					setCallFocus(false);
					selectAll();

					//DANGER
//					setCallFocus(true);
					
//					Globals.logString("Focus - OUT");
				}
			});
  	}
  }
  
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  
  public void setAttributes(Attributes atr) {
    attributes = atr;
    
    FocXMLGuiComponentStatic.applyAttributes(this, atr);
    
    if(getDescription().equals("[VALUE]")){
    	setDescriptionEqualValue(true);
    	setDescription(getValue());
    }else{
    	setDescriptionEqualValue(false);
    }
    
    if(atr != null && atr.getValue(FXML.ATT_PROMPT) != null){
    	setInputPrompt(atr.getValue(FXML.ATT_PROMPT));
    }
  }
  
  @Override
  public Attributes getAttributes() {
    return attributes;
  }
  
  @Override
  public IFocData getFocData() {
    return focData;
  }

  @Override
  public void setValue(String newValue) throws ReadOnlyException, Converter.ConversionException {
    if(getDelegate() != null && getDelegate().hasNoRight()){// getFocData() != null && getFocData() instanceof FProperty && ((FProperty)getFocData()).getAccessRight() == FocObject.PROPERTY_RIGHT_NONE){
    	super.setValue(FField.NO_RIGHTS_STRING);
    }else{
    	super.setValue(newValue);
    }
    if(isDescriptionEqualValue()){
    	setDescription(getValue());
    }
  }

	@Override
	public Field getFormField() {
		return this;
	}
	
	@Override
  public String getXMLType() {
    return (getFocData() != null) ? FXML.TAG_FIELD : FXML.TAG_TEXT_FIELD;
  }
	
	@Override
  public void setEnabled(boolean enabled) {
    super.setEnabled(enabled);
    addStyleName("visible");
  }
	
	@Override
  public boolean copyGuiToMemory() {
	  if(focData instanceof FProperty){
	  	if(getDelegate() == null || !getDelegate().hasNoRight()){
		  	String val = getValue();
		  	((FProperty)focData).setString(val);
	  	}	  	
	  	
//	  	if(true){
//	  		setDescription(val);
//	  	}
//	    ((FProperty)focData).setValue(getValue());
	  }
	  return false;
  }

  @Override
  public void copyMemoryToGui() {
    if(focData instanceof FProperty){
//      setValue((String) ((FProperty)focData).getValue());
    	setValue(((FProperty)focData).getString());
    	if(isDescriptionEqualValue()){
    		setDescription(getValue());
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
    copyMemoryToGui();
    setAttributes(getAttributes());
  }

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if(visible){
			setAttributes(getAttributes());
		}
	}

  @Override
  public String getValueString() {
    return (String)getValue();
  }

  @Override
  public void setValueString(String value) {
    setValue(value);
  }

	@Override
	@Deprecated
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	public boolean isCallFocus() {
		return callFocus;
	}

	public void setCallFocus(boolean callFocus) {
		this.callFocus = callFocus;
	}

	@Override
	public void refreshEditable() {
		setEnabled(getDelegate() != null ? getDelegate().isEditable() : true);
	}

	public boolean isDescriptionEqualValue() {
		return descriptionEqualValue;
	}

	public void setDescriptionEqualValue(boolean descriptionEqualValue) {
		this.descriptionEqualValue = descriptionEqualValue;
	}
}
