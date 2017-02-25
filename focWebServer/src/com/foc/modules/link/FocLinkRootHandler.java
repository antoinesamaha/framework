package com.foc.modules.link;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.foc.Globals;

public class FocLinkRootHandler extends DefaultHandler implements IFocLinkConst {

  private AbstractSpecificHandler specificHandler = null;
  
	public FocLinkRootHandler(String xmlInput) throws Exception {
		try{
			if(xmlInput != null){
				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser saxParser = factory.newSAXParser();
				InputStream is = new ByteArrayInputStream(xmlInput.getBytes());
				saxParser.parse(is, this);
			}
		}catch (Exception e){
			Globals.logException(e);
		}
	}
	
	public void dispose(){
		if(specificHandler != null){
			specificHandler.dispose();
			specificHandler = null;
		}
	}
	
//	public void respond(HttpServletResponse response, String username){
//		FocLinkResponseGenerator responseGenerator = new FocLinkResponseGenerator(username, getAttributeMap());
//		try {
//			String xmlResponse = responseGenerator.generateXmlResponse();
//			if(!Utils.isStringEmpty(xmlResponse)){
//				response.getOutputStream().write(xmlResponse.getBytes());
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
  @Override 
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if(specificHandler==null){
			String name = attributes != null ? attributes.getValue(ATT_NAME) : null;
			FocLinkHandlerCreator creator = FocLinkParserFactory.getInstance().get(qName, name);
			if(creator != null){
				specificHandler = creator.newFocLinkHandler();
			}
		}
		if(specificHandler != null){
			specificHandler.startElement(uri, localName, qName, attributes);
		}
	}
  
  @Override 
	public void endElement(String uri, String localName, String qName) throws SAXException {
  	if(specificHandler != null){
  		specificHandler.endElement(uri, localName, qName);
  	}
	}
  
  @Override
  public void characters(char ch[], int start, int length) throws SAXException{
  	if(specificHandler != null){
  		specificHandler.characters(ch, start, length);
  	}
  }
  
  public StringBuffer getResponse(){
  	StringBuffer buffer = null; 
  	if(specificHandler != null){
  		buffer = specificHandler.getResponse();
  	}
  	return buffer;
  }
  
  public void commit(){
  	if(specificHandler != null){
  		specificHandler.commit();
  	}
  }
}