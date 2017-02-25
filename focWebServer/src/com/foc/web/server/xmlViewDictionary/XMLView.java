package com.foc.web.server.xmlViewDictionary;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import com.fab.gui.xmlView.XMLViewDefinition;
import com.foc.ConfigInfo;
import com.foc.Globals;
import com.foc.IFocEnvironment;
import com.foc.admin.FocUser;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.FocWebEnvironment;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.vaadin.server.ClassResource;

public class XMLView {

  public static final int PERMISSION_READ_WRITE = 0;
  public static final int PERMISSION_SELECT     = 1;
  public static final int PERMISSION_NOTHING    = 2;
  
  private XMLViewKey 				xmlViewKey          = null;
  private XMLViewDefinition xmlviewDefinition   = null;
	private String     				xmlFileName         = null;
	private String     				xmlFileName_ForHelp = null;
  private String     				javaClassName       = null;
  
  public XMLView(XMLViewKey xmlViewKey, String xmlFileName, String javaClassName){
    setXmlViewKey(xmlViewKey);
    setXmlFileName(xmlFileName);
    setJavaClassName(javaClassName);
  }

  public XMLView(XMLViewDefinition viewDefinition){
  	setXmlviewDefinition(viewDefinition);
  	
		XMLViewKey key = new XMLViewKey(viewDefinition.getStorageName(),
																		viewDefinition.getType(),
																		viewDefinition.getContext(),
																		viewDefinition.getView());
  	
		setXmlViewKey(key);
  }
  	
  public boolean isSystemView(){
    return xmlviewDefinition == null;
  }
  
  public String getXmlFileName() {
    return xmlFileName;
  }

  public void setXmlFileName(String xmlFileName) {
    this.xmlFileName = xmlFileName;
  }
  
	public String getXmlFileName_ForHelp() {
		return xmlFileName_ForHelp;
	}

	public void setXmlFileName_ForHelp(String xmlFileName_ForHelp) {
//  	FocHelpBook.getInstance().addPage(getXmlViewKey(), xmlFileName_ForHelp);
		this.xmlFileName_ForHelp = xmlFileName_ForHelp;
	}	

  @SuppressWarnings("rawtypes")
  public ICentralPanel newJavaClassInstance(){
    ICentralPanel centralPanel = null;
    Class cls = null;
    try {
      cls = Class.forName(getJavaClassName());
      centralPanel = (ICentralPanel) cls.newInstance();
    } catch (Exception e) {
      Globals.logException(e);
    }
    
    return centralPanel;
  }
  
  public String getJavaClassName() {
  	if(xmlviewDefinition != null){
  		javaClassName = xmlviewDefinition.getJavaClassName();	
  		if(javaClassName.startsWith("b01.foc.")){
  			javaClassName = javaClassName.replace("b01.foc.","com.foc.");
  		}
  	}
  	
    if(javaClassName == null || javaClassName.isEmpty()){
      javaClassName = FocXMLLayout.class.getName();
    }
    return javaClassName;
  }

  public void setJavaClassName(String javaClassName) {
    this.javaClassName = javaClassName;
  }

  public XMLViewKey getXmlViewKey() {
    return xmlViewKey;
  }

  public void setXmlViewKey(XMLViewKey xmlViewKey) {
    this.xmlViewKey = xmlViewKey;
  }
  
  public InputStream getXMLStream_ForHelp(){
  	return getXMLStream(true);
  }
  
  public InputStream getXMLStream_ForView(){
  	return getXMLStream(false);
  }
  
  private InputStream getXMLStream(boolean help){
  	InputStream inputStream = null;
  	if(getXmlviewDefinition() != null){
  		if(!help){
	  		XMLViewDefinition xmlViewDef = getXmlviewDefinition();
	  		String xml = xmlViewDef.getXML();
	      try {
	        inputStream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
	      } catch (UnsupportedEncodingException e) {
	        Globals.logException(e);
	      }
	      if(inputStream == null){
	      	Globals.showNotification("Could not load XML from table", "View key : "+getXmlViewKey().getStringKey(), FocWebEnvironment.TYPE_ERROR_MESSAGE);
	      }
  		}
  	}else{
  		ClassResource resource = null;
  		try{
  			String fileName = help ? getXmlFileName_ForHelp() : getXmlFileName();
  			if(fileName != null && !fileName.isEmpty()){
  				resource = new ClassResource(fileName);
  				inputStream = resource.getStream().getStream();
  			}
  		}catch(Exception e){
        Globals.logString("Could not load file : "+getXmlFileName());
        Globals.logException(e);        
        
        if(ConfigInfo.isForDevelopment()){
          Globals.logString("Developer? Will Attempt creating the file : "+getXmlFileName());
          String fullFileName = getFullFileName();
          File file = new File(fullFileName);
          if(!file.exists()){
            try{
              file.createNewFile();

              resource = new ClassResource(getXmlFileName());
              inputStream = resource.getStream().getStream();
            }catch (IOException eForFileCreation){
              Globals.logException(eForFileCreation);
            }
          }
        }
  		}
	    
      if(inputStream == null){
      	Globals.showNotification("Could not load XML from file", "View key : "+getXmlViewKey().getStringKey()+"\nFile : "+getXmlFileName(), IFocEnvironment.TYPE_ERROR_MESSAGE);
        Globals.logString("!!!! ERROR : Could Not Load file : "+getXmlFileName());
      }	    
  	}
    
    return inputStream;
  }

  public String getXMLString(){
	  String xmlContent = "";
	  InputStream xmlFileStream = getXMLStream_ForView();
	  if(xmlFileStream != null){
	    Scanner xmlFileScfanner = new Scanner(xmlFileStream).useDelimiter("\n");
	    while(xmlFileScfanner.hasNext()){
	      xmlContent += xmlFileScfanner.next() + "\n";
	    }
	  }
	  return xmlContent;
  }
  
  public XMLViewDefinition getXmlviewDefinition() {
		return xmlviewDefinition;
	}

	public void setXmlviewDefinition(XMLViewDefinition xmlviewDefinition) {
		this.xmlviewDefinition = xmlviewDefinition;
	}
	
	public String getFullFileName(){
		String fullFileName = null;
		if(isSystemView()){
      if(ConfigInfo.isForDevelopment()){
        try {
          fullFileName = ConfigInfo.getCodeProjectPath();
          if(!fullFileName.endsWith("/")) fullFileName += "/";
          if(xmlFileName.contains("everpro")){
            fullFileName += "everproWebServer/src";
          }else{
            fullFileName += "focWebServer/src";
          }
          if(!xmlFileName.startsWith("/")) fullFileName += "/";
          fullFileName += xmlFileName;
        }catch(Exception e){
        	Globals.logException(e);
        }
      }
		}
		return fullFileName;
	}
	
	public void saveXML(String xml){
    if (isSystemView()) {
      if(ConfigInfo.isForDevelopment()){
        try {
          String fullFileName = getFullFileName();
            
          PrintWriter out = new PrintWriter(fullFileName);
          out.println(xml.toString());
          out.flush();
          out.close();
          
          Globals.logString(xml.toString()+" File written to "+fullFileName);
          
        } catch (IOException e) {
          Globals.logException(e);
        }
      }else{
        //Cannot save XML file unless you are a developer at 01Barmaja 
      }
    }else{
      getXmlviewDefinition().setXML(xml);
      getXmlviewDefinition().validate(false);
    }
	}
	
  public int getViewPermission(FocUser focUser){
    int permission = PERMISSION_SELECT;
    if(focUser.getName().equals("01BARMAJA")){
      permission = PERMISSION_READ_WRITE;
    }
    return permission;
  }
  
  public boolean isHelpFileExist(){
  	return getXmlFileName_ForHelp() != null && !getXmlFileName_ForHelp().isEmpty(); 
  }
}