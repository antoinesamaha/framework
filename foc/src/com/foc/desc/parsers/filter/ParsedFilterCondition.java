package com.foc.desc.parsers.filter;

import org.xml.sax.Attributes;

import com.foc.annotations.model.FocFilterCondition;
import com.foc.desc.parsers.xml.FXMLDesc;
import com.foc.desc.parsers.xml.XMLFocDescParser;

public class ParsedFilterCondition {
	private String fieldPath       = null;
	private String prefix          = null;
	private String caption         = null;
	private String captionProperty = null;

	public ParsedFilterCondition(FocFilterCondition filterCondAnnotation){
		this.fieldPath = filterCondAnnotation.fieldPath();
		this.prefix = filterCondAnnotation.prefix();
		this.caption = filterCondAnnotation.caption();
		this.captionProperty = filterCondAnnotation.captionProperty();
	}
	
	public ParsedFilterCondition(Attributes att){
		this.fieldPath = XMLFocDescParser.getString(att, FXMLDesc.ATT_FILTER_ON_FIELD);
		this.prefix = XMLFocDescParser.getString(att, FXMLDesc.ATT_FILTER_CONDITION_PREFIX);
		this.caption = XMLFocDescParser.getString(att, FXMLDesc.ATT_FILTER_CONDITION_CAPTION);
		this.captionProperty = XMLFocDescParser.getString(att, FXMLDesc.ATT_FILTER_CONDITION_CAPTION_PROPERTY);
	}

	public String getFieldPath() {
		return fieldPath;
	}

	public String getPrefix() {
		return prefix;
	}

	public String getCaption() {
		return caption;
	}

	public String getCaptionProperty() {
		return captionProperty;
	}

	public void setCaptionProperty(String captionProperty) {
		this.captionProperty = captionProperty;
	}
}
