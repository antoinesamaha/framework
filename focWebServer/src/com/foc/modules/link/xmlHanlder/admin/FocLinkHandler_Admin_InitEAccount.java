package com.foc.modules.link.xmlHanlder.admin;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.foc.modules.link.AbstractSpecificHandler;
import com.foc.modules.link.IFocLinkConst;
import com.foc.web.server.FocWebServer;
import com.foc.webservice.FocWebService;

public class FocLinkHandler_Admin_InitEAccount extends AbstractSpecificHandler implements IFocLinkConst {

	public static final String  TAG_EACCOUNT     = "EAccount_CreateUser";
	public static final String TAG_COMPANY_NAME = "company";
	public static final String TAG_PASSWORD     = "password";
	public static final String TAG_USER_NAME    = "userName";
	public static final String TAG_CURRENCY     = "currency";
	
	public static final String TAG_APPLICATION_TYPE = "applicationType";
	public static final String TAG_PLAN             = "plan";
	public static final String TAG_RENEWED_UNTIL    = "renewedUntil";
	public static final String TAG_USER_ROLE        = "userRole";
	
	public FocLinkHandler_Admin_InitEAccount(){
		
	}
	
	public void dispose(){
		super.dispose();
	}
	
  @Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		
  	if(!error() && attributes != null){
			if(qName.equals(TAG_EACCOUNT)){
				String accountName  = attributes.getValue(TAG_USER_NAME);
				String password     = attributes.getValue(TAG_PASSWORD);
				String company      = attributes.getValue(TAG_COMPANY_NAME);
				String currency     = attributes.getValue(TAG_CURRENCY);
				String appType      = attributes.getValue(TAG_APPLICATION_TYPE);
				String plan         = attributes.getValue(TAG_PLAN);
				String userRole     = attributes.getValue(TAG_USER_ROLE);
				String renewedUntil = attributes.getValue(TAG_RENEWED_UNTIL);

				FocWebService focWebService = FocWebServer.getInstance().newFocWebService();
				focWebService.initAcount(company, accountName, password, currency, appType, plan, userRole, renewedUntil);
			}
		}
	}
  	
  @Override
  public void characters(char ch[], int start, int length){
	}
  		 
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
	}
	
	@Override
	public StringBuffer getResponse() {
		return null;
	}

	@Override
	public void commit() {
	}
}
