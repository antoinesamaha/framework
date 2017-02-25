package com.foc.vaadin.gui.xmlForm;

import java.util.ArrayList;

import org.xml.sax.Attributes;

import com.foc.dataDictionary.FocDataDictionary;
import com.foc.web.util.FXMLAbstractAttributes;

public class FocXMLAttributes extends FXMLAbstractAttributes {

	private IXMLAttributeResolver xmlLayout = null;
	
	public FocXMLAttributes(FocXMLLayout xmlLayout, Attributes attributes){
		super(attributes);
		if(attributes instanceof FocXMLAttributes){
		  for(int i=0; i<attributes.getLength(); i++){
		    addAttribute(attributes.getQName(i), ((FocXMLAttributes)attributes).getValueWithoutResolve(i));		    
		  }
		}
		  
		this.xmlLayout = xmlLayout;
	}
	
	public FocXMLAttributes(){
	}
	
	public void dispose(){
		xmlLayout = null;
	}

	@Override
	protected String resolveValue(String value){
		if(xmlLayout != null){
			FocDataDictionary localDataDictionary = xmlLayout.getFocDataDictionary(false);
			if(localDataDictionary != null){
				value = localDataDictionary.resolveExpression(xmlLayout.getFocData(), value, false);
			}
			value = FocDataDictionary.getInstance().resolveExpression(xmlLayout.getFocData(), value, true);
		}
		return value;
	}
	
  private ArrayList<String> getFieldsUsed(String value){
    ArrayList<String> array = null;
    if(xmlLayout != null){
      array = FocDataDictionary.getInstance().getFieldsUsed(value);
    }
    return array;
  }
  
	public void addAttribute(String tag, String value){
		int idx = getIndex(tag);
		if(idx >= 0){
			setAttribute(idx, "", tag, tag, "CDATA", value);
		}else{
			addAttribute("", tag, tag, "CDATA", value);
		}
	}
	
	public void removeAttribute(String tag){
		int idx = getIndex(tag);
		if(idx >= 0){
			removeAttribute(idx);
		}
	}
	
	@Override
	public String getValue(int arg0) {
		String value = super.getValue(arg0);
		value = resolveValue(value);
		return value;
	}
	
	public String getValueWithoutResolve(int arg0) {
    String value = super.getValue(arg0);
    return value;
  }

	@Override
	public String getValue(String arg0) {
		String value = super.getValue(arg0);
		value = resolveValue(value);
		return value;
	}

	@Override
	public String getValue(String arg0, String arg1) {
		String value = super.getValue(arg0, arg1);
		value = resolveValue(value);
		return value;
	}
	
	public ArrayList<String> getArrayOfFieldsUsed(int idx){
	  String value = getValueWithoutResolve(idx);
	  return getFieldsUsed(value);
	}
	
	public void replaceStyleValue(String oldStyle, String newStyle){
		String value = getValue(FXML.ATT_STYLE);
		if(value != null && value.contains(newStyle)){
			
		}else{
			if(value != null && value.contains(oldStyle)){
				value = value.replace(oldStyle, newStyle);
				addAttribute(FXML.ATT_STYLE, value);
			}else{
				if(value == null || value.isEmpty()){
					addAttribute(FXML.ATT_STYLE, newStyle);
				}else{
					addAttribute(FXML.ATT_STYLE, value+","+newStyle);
				}
			}
		}
	}
}
