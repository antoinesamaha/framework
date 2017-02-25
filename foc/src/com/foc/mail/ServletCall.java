package com.foc.mail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;

import com.foc.Globals;

public class ServletCall {
	
	private String                  url          = "http://localhost:8080/TrustTradingVaadin/update";
	private HashMap<String, String> parameterMap = null;
	
	public ServletCall(String url){
		this.url = url;
		parameterMap = new HashMap<String, String>();
	}
	
	public void dispose(){
		url = null;
	  if(parameterMap != null){
	  	parameterMap.clear();
	  	parameterMap = null;
	  }
	}
	
	public void putParameter(String key, String value){
		parameterMap.put(key, value);
	}
	
  public void callServer(){
  	try{
  		URL updateURL = new URL(url);
  		URLConnection updateConnection = updateURL.openConnection();
  		
  		if(parameterMap != null && parameterMap.size() > 0){
  		  updateConnection.setDoOutput(true);
  		
  		  boolean first = true;
  	    BufferedWriter out = new BufferedWriter( new OutputStreamWriter( updateConnection.getOutputStream() ) );
  	    Iterator<String> iter = parameterMap.keySet().iterator();
  	    while(iter != null && iter.hasNext()){
  	    	String key = iter.next();
  	    	if(!first) out.write("&");
  	    	out.write(key+"="+parameterMap.get(key));
  	    	first = false;
  	    }
  	    
  	    out.flush();
  	    out.close();
  		
  	    BufferedReader in = new BufferedReader(new InputStreamReader(updateConnection.getInputStream()));
  	    String inputLine;
  	    
  	    while ((inputLine = in.readLine()) != null) System.out.println(inputLine);
  	    in.close();
  		}
  	}catch(Exception e){
  	  Globals.logException(e);
  	}
  }
}
