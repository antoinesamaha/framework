package com.foc.vaadin.gui.layouts;

import org.xml.sax.Attributes;

import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentDelegate;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;

@SuppressWarnings("serial")
public class FVEncapsulatorLayout extends HorizontalLayout implements FocXMLGuiComponent {
  private FocXMLGuiComponent field = null;
  
  public FVEncapsulatorLayout(FocXMLGuiComponent field, Attributes attributes) {
    this.field = field;
    if(field != null && field.getFormField() != null) field.getFormField().setCaption(null);
    if(attributes instanceof FocXMLAttributes) ((FocXMLAttributes)attributes).removeAttribute(FXML.ATT_CAPTION);
  	setMargin(false);
  	setSpacing(false);
    setAttributes(attributes);
  }
  
  public FVEncapsulatorLayout(Component component, String label, String width) {
  	component.setCaption(null);
  	setMargin(false);
  	setSpacing(false);
    addComponent(component);
  }
  
  @Override
  public void dispose(){
  	dispose_Field();
  	removeAllComponents();
  }

  public void dispose_Field(){
  	if(field != null){
  		field.dispose();
  		field = null;
  	}
  }
  
  public void replaceField(FocXMLGuiComponent newField){
  	removeAllComponents();
  	dispose_Field();
  	field = (FocXMLGuiComponent) newField;
  	addComponent((Component) newField);
  }
  
  @Override
  public void setWidth(String width){
  	super.setWidth(width);
  }
  
  @Override
  public void setWidth(float width, Unit unit){
  	super.setWidth(width, unit);
  }
  
  @Override
  public String getXMLType() {
    return field != null ? field.getXMLType() : null;
  }

  @Override
  public void setAttributes(Attributes attributes) {
  	if(attributes != null){
  		FocXMLGuiComponentStatic.applyAttributes(this, attributes);
	    if(field != null) field.setAttributes(attributes);
    	removeAllComponents();
      addComponent((Component) field);
  	}
  }

  @Override
  public Field getFormField() {
    return field != null ? field.getFormField() : null;
  }

  @Override
  public Attributes getAttributes() {
    return field != null ? field.getAttributes() : null;
  }

  @Override
  public boolean copyGuiToMemory() {
    if(field != null){
      field.copyGuiToMemory();
    }
    return false;
  }

  @Override
  public void copyMemoryToGui() {
    if(field != null){
      field.copyMemoryToGui();
    }
  }

  @Override
  public IFocData getFocData() {
    return field != null ? field.getFocData() : null;
  }
    
  @Override
  public void setDelegate(FocXMLGuiComponentDelegate delegate) {
    if(field != null) field.setDelegate(delegate);
  }

  @Override
  public FocXMLGuiComponentDelegate getDelegate() {
    return field != null ? field.getDelegate() : null;
  }
  
  @Override
  public void setFocData(IFocData focData) {
    if(field != null){
      field.setFocData(focData);
    }
  }

  @Override
  public String getValueString() {
    String temp = null;
    
    FocXMLGuiComponent component = (FocXMLGuiComponent) getFormField();
    if (component != null){
      temp = component.getValueString();
    }
    return temp;
  }

  @Override
  public void setValueString(String value) {
    FocXMLGuiComponent component = (FocXMLGuiComponent) getFormField();
    if (component != null){
      component.setValueString(value);
    }
  }
  
	@Override
	public void refreshEditable() {
    FocXMLGuiComponent component = (FocXMLGuiComponent) getFormField();
    if (component != null){
      component.refreshEditable();
    }
	}
}
