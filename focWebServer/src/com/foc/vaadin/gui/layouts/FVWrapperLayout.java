package com.foc.vaadin.gui.layouts;

import org.xml.sax.Attributes;

import com.foc.ConfigInfo;
import com.foc.Globals;
import com.foc.property.FProperty;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentDelegate;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

@SuppressWarnings("serial")
public class FVWrapperLayout extends HorizontalLayout implements FocXMLGuiComponent {
  private Label                      caption  = null;
  private FocXMLGuiComponent         field    = null;
  
  public FVWrapperLayout(FocXMLGuiComponent field, Attributes attributes) {
    this.caption = new Label("");
    this.caption.addStyleName("foc-f12");
    this.field = field;
    setAttributes(attributes);
  }
  
  public FVWrapperLayout(Component component, String label, String width) {
    this.caption = new Label(label);
    caption.setWidth(width);
    
    addComponent(this.caption);
    setComponentAlignment(caption, Alignment.BOTTOM_RIGHT);
    addComponent(component);
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
	    if(field != null) field.setAttributes(attributes);
	
	    caption.setValue(attributes.getValue(FXML.ATT_CAPTION));
	    //setSpacing(true);
	    String captionWidth = attributes.getValue(FXML.ATT_CAPTION_WIDTH);
	    if(captionWidth != null){
	    	caption.setWidth(captionWidth); 
	    }else{
	    	caption.setWidth("75px");
	    }
	    
	    String captionPosition = attributes.getValue(FXML.ATT_CAPTION_POSITION);
	    if (captionPosition.equals(FXML.VAL_CAPTION_POS__RIGHT)) {
	    	removeAllComponents();
	      addComponent((Component) field);
	      setComponentAlignment((Component) field, Alignment.MIDDLE_LEFT);
	      addComponent(this.caption);
	      setComponentAlignment(this.caption, Alignment.MIDDLE_RIGHT);
	      setSpacing(true);
	      FocXMLGuiComponentStatic.applyStyle(this.caption, "f16,bold,text-left");
	    } else if (captionPosition.equals(FXML.VAL_CAPTION_POS__LEFT)) {
	    	removeAllComponents();
	      addComponent(this.caption);
	      setComponentAlignment(this.caption, Alignment.MIDDLE_RIGHT);
	      addComponent((Component) field);
	      setComponentAlignment((Component) field, Alignment.MIDDLE_LEFT);
	    }
	    
	    FocXMLGuiComponentStatic.applyStyle(caption, attributes.getValue(FXML.ATT_CAPTION_STYLE));
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
  
  public static FocXMLGuiComponent wrapIfNecessary(FocXMLGuiComponent atomicComponent){
    FocXMLGuiComponent retComp = atomicComponent;
  	if(atomicComponent != null){
	  	Attributes attributes = atomicComponent.getAttributes();
	  	if (attributes != null){
	  		String encapsulate = attributes.getValue(FXML.ATT_ENCAPSULATE);
	  		if(encapsulate != null && encapsulate.trim().toLowerCase().equals("true")){ 
	  			retComp = new FVEncapsulatorLayout(atomicComponent, attributes);
		  	}else if (attributes.getValue(FXML.ATT_CAPTION_POSITION) != null) {
		      if (attributes.getValue(FXML.ATT_CAPTION_POSITION).equals("left") || attributes.getValue(FXML.ATT_CAPTION_POSITION).equals("right")) {
		        retComp = new FVWrapperLayout(atomicComponent, attributes);
		      }
		    }
	  	}
  	}
  	return retComp;
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
  public void dispose(){
  	if(field != null){
  		field.dispose();
  		field = null;
  	}
    caption  = null;
    removeAllComponents();
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
