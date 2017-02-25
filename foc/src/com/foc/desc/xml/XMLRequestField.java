package com.foc.desc.xml;

import org.xml.sax.Attributes;

public class XMLRequestField implements FXMLDesc {
	
	private String sourceFieldName = null;
	private String targetFieldName = null;
	
	public XMLRequestField(Attributes att){
  	sourceFieldName = XMLFocDescParser.getString(att, ATT_JOIN_FLD_SRC);
  	targetFieldName = XMLFocDescParser.getString(att, ATT_JOIN_FLD_TAR);
	}

	public String getSourceFieldName() {
		return sourceFieldName;
	}

	public String getTargetFieldName() {
		return targetFieldName;
	}

}
