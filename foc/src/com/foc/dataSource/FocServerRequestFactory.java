package com.foc.dataSource;

import java.util.HashMap;

public class FocServerRequestFactory {

	private HashMap <String, IFocXmlService> requestMap = null;
	
	public FocServerRequestFactory(){
		requestMap = new HashMap <String, IFocXmlService>(); 
	}
	
	public void dispose(){
		if(requestMap != null){
			requestMap.clear();
			requestMap = null;
		}
	}
	
	public void put(IFocXmlService request){
		
	}

	// ----------------------------------------------------------------
	// Static
	// ----------------------------------------------------------------	
	private static FocServerRequestFactory instance = null;
	public  static FocServerRequestFactory getInstance(){
		if(instance == null){
			instance = new FocServerRequestFactory();
		}
		return instance;
	}
}
