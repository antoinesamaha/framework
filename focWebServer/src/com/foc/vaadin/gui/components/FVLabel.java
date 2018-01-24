package com.foc.vaadin.gui.components;

import org.xml.sax.Attributes;

import com.foc.Globals;
import com.foc.property.FProperty;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentDelegate;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Field;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.Label;

@SuppressWarnings({ "serial", "unchecked" })
public class FVLabel extends Label implements FocXMLGuiComponent {
  
  private Attributes attributes    = null;
  private FocXMLGuiComponentDelegate delegate = null;
  private String content = null;
  
  public FVLabel(Attributes attributes) {
  	delegate = new FocXMLGuiComponentDelegate(this);
    setAttributes(attributes);
    
    initLabel();
  }
  
  public FVLabel(Attributes attributes, IFocData focData) {
    delegate = new FocXMLGuiComponentDelegate(this);
    setAttributes(attributes);
    setFProperty(focData);
  }
  
  public FVLabel(String content){
    super(content);
    this.content = content;
    delegate = new FocXMLGuiComponentDelegate(this);
  }
  
  public FVLabel(String content, ContentMode contentMode){
    super(content, contentMode);
    delegate = new FocXMLGuiComponentDelegate(this);
  }

  @Override
  public void dispose(){
  	if(attributes != null){
  		try{
  			((FocXMLAttributes)attributes).dispose();
  		}catch(Exception e){
  			Globals.logExceptionWithoutPopup(e);
  		}
      attributes = null;
  	}

    if(delegate != null){
    	delegate.dispose();
    	delegate= null;
    }
  }
  
  @Override
  public void setParent(HasComponents parent) {
  	super.setParent(parent);
  }  
  
  @Override
  public Field getFormField() {
    return null;
  }
  
  @Override
  public Attributes getAttributes() {
    return attributes;
  }
  
  @Override
  public void setAttributes(Attributes attributes) {
    this.attributes = attributes;
    initLabel();
    FocXMLGuiComponentStatic.applyAttributes(this, attributes);
  }
  
  @Override
  public FProperty getFocData() {
    return null;
  }

  @Override
  public String getXMLType() {
    return FXML.TAG_LABEL;
  }
  
  public String getLabelValue_AsHTML(){
  	String val =  attributes != null ? attributes.getValue(FXML.ATT_VALUE) : null;
  	if(val != null){
  		val=val.replace("\n", "<br>");
	  	boolean keepLooking = val.contains("  ");
	  	while(keepLooking){
	  		val = val.replace("  ", "&nbsp;&nbsp;");
	  		val = val.replace("&nbsp; ", "&nbsp;&nbsp;");
	  		keepLooking = val.contains("  ") || val.contains("&nbsp; ");
	  	}
  	}
  	return val;
  }
  
  public String parseStyleAttributeValue(String style){
  	String valueAsHTML =  getLabelValue_AsHTML();
  	
    if (style != null) {
    	String[] styleArray = style.split(",");
    	for(int i=0; i<styleArray.length; i++){
      	addStyleName("foc-"+styleArray[i]);
//      	addStyleName(styleArray[i]);
    	}
    }

  	style = "";
    String marginPadding = "";

    //Filling the new style coming from attributes
    if(attributes != null){
      if(attributes.getValue("fontSize") != null) {
        style += "font-size:"+attributes.getValue("fontSize")+";";
      }
      
      if(attributes.getValue("bold") != null) {
        style += "font-weight:bold;";
      }
      
      if(attributes.getValue("bgColor") != null) {
        style += "background-color:"+attributes.getValue("bgColor")+";";
      }
      
      if(attributes.getValue("fontColor") != null) {
        style += "color:"+attributes.getValue("fontColor")+";";
      }
      
      if(attributes.getValue("marginTop") != null) {
        marginPadding += "margin-top:"+attributes.getValue("marginTop")+";";
      }
      
      if(attributes.getValue("marginBottom") != null) {
        marginPadding += "margin-bottom:"+attributes.getValue("marginBottom")+";";
      }
      
      if(attributes.getValue("marginLeft") != null) {
        marginPadding += "margin-left:"+attributes.getValue("marginLeft")+";";
      }
      
      if(attributes.getValue("marginRight") != null) {
        marginPadding += "margin-right:"+attributes.getValue("marginRight")+";";
      }
      
      if(attributes.getValue("paddingTop") != null) {
        marginPadding += "padding-top:"+attributes.getValue("paddingTop")+";";
      }
      
      if(attributes.getValue("paddingBottom") != null) {
        marginPadding += "padding-bottom:"+attributes.getValue("paddingBottom")+";";
      }
      
      if(attributes.getValue("paddingLeft") != null) {
        marginPadding += "padding-left:"+attributes.getValue("paddingLeft")+";";
      }
      
      if(attributes.getValue("paddingRight") != null) {
        marginPadding += "padding-right:"+attributes.getValue("paddingRight")+";";
      }
    }
      
    setContentMode(ContentMode.HTML);
    
    if(valueAsHTML == null || valueAsHTML.isEmpty()){
    	valueAsHTML = content;
    }
    
    if(!style.isEmpty() || !marginPadding.isEmpty()){
	    if(marginPadding.isEmpty()) {
	    	valueAsHTML = "<p style='margin: 0px; padding: 0px;"+style+"'>"+valueAsHTML+"</p>";
//	      setValue("<p style='margin: 0px; padding: 0px;"+style+"'>"+valueAsHTML+"</p>");
	    } else {
	    	valueAsHTML = "<p style='"+marginPadding+style+"'>"+valueAsHTML+"</p>";
//	      setValue("<p style='"+marginPadding+style+"'>"+valueAsHTML+"</p>");
	    }
    }
    return valueAsHTML;
  }
  
  public void initLabel() {
  	addStyleName("foc-normal");
  	addStyleName("foc-wrap-line");
  	setContentMode(ContentMode.HTML);
  	
  	copyMemoryToGui();
  }

  @Override
  public boolean copyGuiToMemory() {
  	return false;
  }

  @Override
  public void copyMemoryToGui() {
  	if(attributes != null){//&& canCopyMemoryToGui()
	    String style = attributes.getValue(FXML.ATT_STYLE);
	    String differentValue = parseStyleAttributeValue(style);
	//    if(getFocData() != null && getFocData() instanceof FProperty && ((FProperty)getFocData()).getAccessRight() == FocObject.PROPERTY_RIGHT_NONE){
	//    	setValue(FField.NO_RIGHTS_STRING);
	//    }else if(differentValue != null){
	    differentValue = getFocData() != null ? getFocData().iFocData_getValue()+"" : differentValue;
	    if(differentValue != null){    
	    	setValue(differentValue);
	    }else{
	    	setValue(getValueFromAttribute());
	    }
  	}
  }
  
  @Override
  public void setValue(String newStringValue) {
  	setContent(newStringValue);//This case is used when we set the value programatically without having an attribute.
  	super.setValue(newStringValue);
  }
  
  private String getValueFromAttribute(){
  	String value = attributes != null ? attributes.getValue(FXML.ATT_VALUE) : null;
  	return value;
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
  
  private void setFProperty(IFocData rootFocData){
    String name = getAttributes().getValue(FXML.ATT_NAME);
    String dataPath = getAttributes().getValue(FXML.ATT_DATA_PATH);
    if(dataPath == null) {
      dataPath = name;
    }
    
    String captionProp = attributes.getValue(FXML.ATT_CAPTION_PROPERTY);
    String newDataPath = dataPath+"."+captionProp;
    IFocData data = null;
    FProperty newProperty = null;
    if(captionProp != null && newDataPath != null){
      data = rootFocData.iFocData_getDataByPath(newDataPath);
      if(data instanceof FProperty){
        newProperty = (FProperty) data;
      }
    }
    if(newProperty == null){
      data = rootFocData.iFocData_getDataByPath(dataPath);
      if(data instanceof FProperty){
        newProperty = (FProperty) data;
      }
    }
    content = newProperty+"";
  }

	@Override
	public void refreshEditable() {
	}

	private String getContent() {
		return content;
	}

	private void setContent(String content) {
		this.content = content;
	}
}