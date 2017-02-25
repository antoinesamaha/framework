package com.foc.vaadin.gui.components;

import org.xml.sax.Attributes;

import com.foc.property.FProperty;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentDelegate;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.vaadin.ui.Field;
import com.vaadin.ui.NativeButton;

@SuppressWarnings({ "serial", "unchecked" })
public class FVNativeButton extends NativeButton implements FocXMLGuiComponent {
  
  private Attributes                 attributes = null;
  private FocXMLGuiComponentDelegate delegate   = null;
  
  public FVNativeButton(Attributes attributes) {
  	delegate = new FocXMLGuiComponentDelegate(this);
    setAttributes(attributes);
    addStyleName("component-margin");
  }
  
  public FVNativeButton(String content) {
    super(content);
    delegate = new FocXMLGuiComponentDelegate(this);
    addStyleName("component-margin");
  }

  public FVNativeButton(String string, ClickListener clickListener) {
  	super(string, clickListener);
  	delegate = new FocXMLGuiComponentDelegate(this);
  	addStyleName("component-margin");
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

    String style = attributes.getValue(FXML.ATT_STYLE);
    if (style != null) {
    	String[] styleArray = style.split(",");
    	for(int i=0; i<styleArray.length; i++){
      	addStyleName(styleArray[i]);
//        	addStyleName(styleArray[i]);
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
}
