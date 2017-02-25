package com.foc.desc.xml;

import org.xml.sax.Attributes;

public class XMLFilterCondition {
	private String fieldPath = null;
	private String prefix    = null;

	public XMLFilterCondition(Attributes att){
		this.fieldPath = XMLFocDescParser.getString(att, FXMLDesc.ATT_FILTER_ON_FIELD);
		this.prefix = XMLFocDescParser.getString(att, FXMLDesc.ATT_FILTER_CONDITION_PREFIX);
	}

	public String getFieldPath() {
		return fieldPath;
	}

	public String getPrefix() {
		return prefix;
	}
}
