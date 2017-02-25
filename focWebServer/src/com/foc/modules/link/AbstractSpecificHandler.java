package com.foc.modules.link;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.foc.Globals;

public abstract class AbstractSpecificHandler extends DefaultHandler implements IFocLinkConst {

	private String errorMessage = null;
	
	public abstract StringBuffer getResponse();
	public abstract void commit();
	
	public void dispose(){
		errorMessage = null;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
	}
  
  @Override 
	public void endElement(String uri, String localName, String qName) throws SAXException {
	}

  protected int getReferenceFromAttributes(String qName, Attributes attributes){
		int ref = 0;
		
		String refStr = null;
		if(attributes == null){
			addErrorMessage(qName+" must have attributes object");
		}
		
		if(!error()){
			refStr = attributes.getValue(ATT_REFERENCE);
			if(refStr == null){
				addErrorMessage(qName+" must have the attribute "+ATT_REFERENCE);
			}
		}
		
		if(!error()){
			try{
				ref = Integer.valueOf(refStr);
			}catch(Exception e){
				addErrorMessage(ATT_REFERENCE+"="+refStr+" in tag "+qName+" must be a positive integer");
				Globals.logException(e);
			}
			if(ref <= 0){
				addErrorMessage(ATT_REFERENCE+"="+refStr+" in tag "+qName+" must be a positive integer");
			}
		}
		
		return ref;
  }

  public boolean error(){
  	return errorMessage != null;
  }
  
	public String getErrorMessage() {
		return errorMessage;
	}

	public void addErrorMessage(String errorMessage) {
		if(this.errorMessage == null){
			this.errorMessage = errorMessage;
		}else{
			this.errorMessage += ". "+errorMessage;
		}
	}
  

}
