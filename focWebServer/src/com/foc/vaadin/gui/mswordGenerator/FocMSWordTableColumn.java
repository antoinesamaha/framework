package com.foc.vaadin.gui.mswordGenerator;

import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;

public class FocMSWordTableColumn extends FocMSWordComponent{

	private String dataPath = null;

	public FocMSWordTableColumn(FocMSWordLayout pdfParent, FocXMLAttributes xmlAttribute) {
		super(pdfParent, xmlAttribute);
		dataPath = buildDataPath(xmlAttribute);
	}

	@Override
	public void write(IMSWordContainer container) {
	}
	
	public String getDataPath(){
		return dataPath;
	}
	
	private String buildDataPath(FocXMLAttributes xmlAttribute){
		String name            = xmlAttribute.getValue(FXML.ATT_NAME);
		String dataPath        = xmlAttribute.getValue(FXML.ATT_DATA_PATH);
		String captionProperty = xmlAttribute.getValue(FXML.ATT_CAPTION_PROPERTY);
		
		if(dataPath == null){
			dataPath = name;
		}
		if(captionProperty != null){
			dataPath = dataPath + "." + captionProperty;
		}
		return dataPath;
	}
}
