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
package com.foc.vaadin.gui.mswordGenerator;

import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.foc.Globals;
import com.foc.shared.dataStore.IFocData;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;

public class FocMSWordIncludeXML extends FocMSWordVerticalLayout{
 
	public FocMSWordIncludeXML(FocMSWordLayout pdfLayout, FocXMLAttributes attribute) {
		super(pdfLayout, attribute);
	}
	
	@Override
	public void write(IMSWordContainer container) {
		IFocData data = getFocData();
		
		String storageName = getXmlAttribute().getValue(FXML.ATT_VIEW_KEY_STORAGE_NAME);
		String dataPath    = getXmlAttribute().getValue(FXML.ATT_DATA_PATH);
		String context     = getXmlAttribute().getValue(FXML.ATT_VIEW_KEY_CONTEXT) != null ? getXmlAttribute().getValue(FXML.ATT_VIEW_KEY_CONTEXT) : XMLViewKey.CONTEXT_DEFAULT;
		String formType    = getXmlAttribute().getValue(FXML.ATT_VIEW_KEY_TYPE) != null ? getXmlAttribute().getValue(FXML.ATT_VIEW_KEY_TYPE) : FXML.VAL_VIEW_KEY_TYPE__FORM;
		String userView    = getXmlAttribute().getValue(FXML.ATT_VIEW_KEY_VIEW) != null ? getXmlAttribute().getValue(FXML.ATT_VIEW_KEY_VIEW) : XMLViewKey.VIEW_DEFAULT;
		
		int type = XMLViewKey.TYPE_TABLE;
		if(formType.equals(FXML.VAL_VIEW_KEY_TYPE__FORM)){
			type = XMLViewKey.TYPE_FORM; 
		}else if(formType.equals(FXML.VAL_VIEW_KEY_TYPE__PIVOT)){
			type = XMLViewKey.TYPE_PIVOT; 
		}else if(formType.equals(FXML.VAL_VIEW_KEY_TYPE__TREE)){
			type = XMLViewKey.TYPE_TREE; 
		}
		
		XMLViewKey xmlViewKey      = new XMLViewKey(storageName, type, context, userView);
		ICentralPanel centralPanel = XMLViewDictionary.getInstance().newCentralPanel(null, xmlViewKey, data);
		if(centralPanel != null){
			InputStream inputStream = centralPanel.getXMLView().getXMLStream_ForView();
			
			setCentralPanel(centralPanel);
			parseXmlFile(inputStream);
			
		}
	}
	private ICentralPanel centralPanel = null;
	
	private void setCentralPanel(ICentralPanel centralPanel){
		this.centralPanel = centralPanel;
	}
	
	private ICentralPanel getCentralPanel(){
		return centralPanel;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if(MSWordXmlFactory.getInstance().get(qName) != null){
			FocXMLAttributes xmlAttrib = new FocXMLAttributes((FocXMLLayout) getCentralPanel(), attributes);
			super.startElement(uri, localName, qName, xmlAttrib);
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if(MSWordXmlFactory.getInstance().get(qName) != null){
			super.endElement(uri, localName, qName);
		}
	}
	
	private void parseXmlFile(InputStream inputStream){
		try{
			SAXParserFactory factory   = SAXParserFactory.newInstance();
			SAXParser        saxParser = factory.newSAXParser();
			
			if(inputStream != null){
				saxParser.parse(inputStream, this);
			}
		}catch(Exception ex){
			Globals.logException(ex);
		}
	}

}
























