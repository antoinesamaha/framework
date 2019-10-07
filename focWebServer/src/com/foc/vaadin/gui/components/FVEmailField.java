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

import java.awt.Component;

import org.xml.sax.Attributes;

import com.foc.desc.field.FEMailField;
import com.foc.property.FProperty;
import com.foc.shared.dataStore.IFocData;
import com.foc.util.Utils;
import com.foc.vaadin.gui.FVIconFactory;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentDelegate;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.components.validator.FVStringValidator;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.vaadin.data.Property.ReadOnlyException;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.BaseTheme;

@SuppressWarnings({ "serial"})
public class FVEmailField extends HorizontalLayout implements FocXMLGuiComponent {
  
  private String     name                  = null;
  private IFocData   focData               = null;
  private Attributes attributes            = null;
  private TextField  emailText             = null;
	private Component  component             = null;
	private FVButton   outlookButton         = null;
  private FocXMLGuiComponentDelegate delegate = null;
 
	public FVEmailField(FProperty property, Attributes attributes) {
    delegate = new FocXMLGuiComponentDelegate(this);
  	setFocData(property);
  	
  	if(property != null && property.getFocField() instanceof FEMailField){
	  	FEMailField eFld = (FEMailField) property.getFocField();
	  	if(eFld.isCapital()){
	  		FVStringValidator strValidator = new FVStringValidator();
	  		strValidator.setCapital(true);
	  		getEmailText().addValidator(strValidator);
	  	}
  	}
  	
  	setAttributes(attributes);
  	init();
  }
  
  public FVEmailField() {
    super();
    delegate = new FocXMLGuiComponentDelegate(this);
    init();
  }
  
  @Override
  public void dispose(){
    focData   = null;
    attributes = null;
    component = null;
  }
  //Begin ---------------------------------------------------------------------
  
  private void init(){
  	addComponent(getEmailText());
  	String withSendIcon = getAttributes().getValue(FXML.ATT_WITH_EMAIL_SEND_ICON);
  	if(withSendIcon != null && (withSendIcon.toLowerCase().equals("false") || withSendIcon.equals("0"))){
  		
  	}else{
  		addComponent(getEmailButton());
  		setComponentAlignment(getEmailButton(), Alignment.BOTTOM_LEFT);
  	}
  }
  
  private FVButton getEmailButton(){
  	if(outlookButton == null){
  		outlookButton = new FVButton("");
  		outlookButton.setDescription("Send email");
  		outlookButton.setStyleName(BaseTheme.BUTTON_LINK);
  		outlookButton.setIcon(FVIconFactory.getInstance().getFVIcon_24(FVIconFactory.ICON_EMAIL));  		

  		outlookButton.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
	    		JavaScript.getCurrent().execute("window.location = 'mailto:"+getEmailText().getValue()+"'");
				}
			});
  	}
  	return outlookButton;
  }
    
  public TextField getEmailText(){
  	if(emailText == null){
  		emailText = new TextField();
  	}
  	return emailText;
  }
	
	@Override
	public void setWidth(String width) {
		if(component != null){
			setWidth(width);
		}
	}
	
	@Override
	public void setHeight(String height) {
		if(component != null){
			setHeight(height);
		}
	}
	
  //End-------------------------------------------------------------------
  
	
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  
  public void setAttributes(Attributes atr) {
    attributes = atr;
    
    FocXMLGuiComponentStatic.applyAttributes(this, atr);
    String caption = this.getCaption();
    this.setCaption(null);
    FocXMLGuiComponentStatic.applyAttributes_WidthHeight(emailText, atr);
    if(emailText != null){
    	emailText.setCaption(caption);
    }
    
		if(atr != null) {
      String style = atr.getValue(FXML.ATT_STYLE);
      if(!Utils.isStringEmpty(style)) {
      	FocXMLGuiComponentStatic.applyStyle(emailText, style);
      }
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

  public void setValue(String newValue) throws ReadOnlyException, Converter.ConversionException {
     setValue(newValue);
  }

	@Override
	public Field getFormField() {
		return (Field) emailText;
	}
	
	@Override
  public String getXMLType() {
    return (getFocData() != null) ? FXML.TAG_FIELD : FXML.TAG_EMAIL_FIELD;
  }
	
	@Override
  public void setEnabled(boolean enabled) {
		getEmailText().setEnabled(enabled);
//    super.setEnabled(enabled);
//    addStyleName("visible");
  }
	
	@Override
  public boolean copyGuiToMemory() {
	  if(focData instanceof FProperty){
	    ((FProperty)focData).setValue(getEmailText().getValue());
	  }
	  return false;
  }

  @Override
  public void copyMemoryToGui() {
    if(focData instanceof FProperty){
    	getEmailText().setValue((String) ((FProperty)focData).getValue());
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
    return (String)getEmailText().getValue();
  }

  @Override
  public void setValueString(String value) {
  	getEmailText().setValue(value);
  }

	@Override
	@Deprecated
	public String toString() {
		return super.toString();
	}

	@Override
	public void refreshEditable() {
		if(getDelegate() != null && getEmailText() != null){
			getEmailText().setEnabled(getDelegate().isEditable());
		}
	}
  
}
