package com.foc.vaadin.gui.components;

import org.xml.sax.Attributes;

import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentDelegate;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;

@SuppressWarnings("serial")
public class FVLine extends Label implements FocXMLGuiComponent {
  
  private Attributes attributes = null;
  private FocXMLGuiComponentDelegate delegate = null;
  
  public FVLine(Attributes attributes) {
    super(initLine(attributes), ContentMode.HTML);
    delegate = new FocXMLGuiComponentDelegate(this);
    addStyleName("seperationLine");
    setAttributes(attributes);
  }
  
  public FVLine() {
    super(initLine(null), ContentMode.HTML);
    addStyleName("seperationLine");
  }
  
  @Override
  public void dispose(){
    attributes = null;
    if(delegate != null){
    	delegate.dispose();
    	delegate = null;
    }
  }
  
  private static String initLine(Attributes attributes){
    String html = "<hr  />";
    return html;
  }
    
  public FVLine(String content){
    super(content);
    delegate = new FocXMLGuiComponentDelegate(this);
  }
  
  public FVLine(String content, ContentMode contentMode){
    super(content, contentMode);
    delegate = new FocXMLGuiComponentDelegate(this);
  }

  @Override
  public Field getFormField() {
    return null;
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
  public IFocData getFocData() {
    return null;
  }

  @Override
  public String getXMLType() {
    return FXML.TAG_LINE;
  }

  @Override
  public boolean copyGuiToMemory() {
  	return false;
  }

  @Override
  public void copyMemoryToGui() {
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
	public void refreshEditable() {
	}
}
