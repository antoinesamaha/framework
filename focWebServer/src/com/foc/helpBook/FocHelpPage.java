package com.foc.helpBook;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import com.foc.Globals;
import com.foc.IFocEnvironment;
import com.vaadin.server.ClassResource;

public class FocHelpPage {

	private String pageCode = null;
	private String fileName = null;

	public FocHelpPage(String pageCode, String fileName) {
		this.fileName = fileName;
		this.pageCode = pageCode;
	}
	
	public void setFileName(String fileName){
		this.fileName = fileName;
	}
	
	public String getFileName(){
		return fileName;
	}
	
	public String getPageCode() {
    return pageCode;
  }
  
  public void setPageCode(String pageCode) {
  	this.pageCode = pageCode;
  }
  
	public String getHelpMessage() {
		String message = null;
		if(message == null){
			try{
				InputStream xmlFileStream = getXMLStream();
				if(xmlFileStream != null){
			    ByteArrayOutputStream baos = new ByteArrayOutputStream();
			    byte[] buffer = new byte[1024];
			    int length = 0;
			    while ((length = xmlFileStream.read(buffer)) != -1) {
		        baos.write(buffer, 0, length);
			    }
			    message = new String(baos.toByteArray());

			    xmlFileStream.close();
			    xmlFileStream = null;
				}
			}catch (Exception e){
				Globals.logException(e);
			}
		}
		return message;
	}
	
  private InputStream getXMLStream(){
  	InputStream inputStream = null;
		ClassResource resource = null;
		try{
			if(fileName != null && !fileName.isEmpty()){
				resource = new ClassResource(fileName);
				inputStream = resource.getStream().getStream();
			}
		}catch(Exception e){
      Globals.logString("Could not load file : "+fileName);
      Globals.logException(e);        
		}
    
    if(inputStream == null){
    	Globals.showNotification("Could not load help page "+fileName, " for page code"+pageCode, IFocEnvironment.TYPE_ERROR_MESSAGE);
    }	    
    
    return inputStream;
  }
}
