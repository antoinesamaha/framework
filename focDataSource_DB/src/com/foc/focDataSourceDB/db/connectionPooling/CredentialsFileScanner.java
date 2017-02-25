package com.foc.focDataSourceDB.db.connectionPooling;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import com.foc.ConfigInfo;
import com.foc.Globals;
import com.foc.focDataSourceDB.db.DBManagerServer;
import com.foc.loader.FocFileLoader;
import com.vaadin.server.ClassResource;

public class CredentialsFileScanner {

	private String    xmlDirectory = null;
	
	public CredentialsFileScanner(String xmlDirectory) {
		this.xmlDirectory = xmlDirectory;
	}
	
	public void dispose(){
		xmlDirectory = null;
	}

	private String getRootXmlDirectoryPath(){
		return xmlDirectory;
	}
	
	public void scanDirectory(DBManagerServer server){
		Globals.logString("Scanning Directory: "+xmlDirectory+" for .properties");
		FocFileLoader fileLoader = new FocFileLoader();
		ArrayList<String> xmlFileNames = fileLoader.findFiles(getRootXmlDirectoryPath(), ".properties");
		fileLoader.dispose();
		
		if(xmlFileNames != null){
			for(int i=0; i<xmlFileNames.size(); i++){
				String xmlFileName = xmlFileNames.get(i);
				if(xmlFileName != null){
					Globals.logString("  Found: "+xmlFileName);
					int indexOfDOT = xmlFileName.lastIndexOf(".");
					if(indexOfDOT > 0){
						String dbSourceKey = xmlFileName.substring(0, xmlFileName.indexOf("."));
						
						String fullXMLFileNameWithPath = getRootXmlDirectoryPath();
						if(fullXMLFileNameWithPath.endsWith("/")){
							fullXMLFileNameWithPath += xmlFileName; 
						}else{
							fullXMLFileNameWithPath += "/"+xmlFileName;
						}

						Properties props = new Properties();
			      ClassResource resource = null;
			      InputStream inputStream = null;
		        resource = new ClassResource(fullXMLFileNameWithPath);
		        inputStream = resource.getStream().getStream();

		        if(inputStream != null){
		  	      try{
								props.load(inputStream);
								inputStream.close();
							}catch (IOException e){
								Globals.logException(e);
							}
		        }else{
		        	Globals.logString("Could not load properties file: "+fullXMLFileNameWithPath);
		        }
		  	      
		        ConnectionCredentials cred = new ConnectionCredentials();
		        cred.setDbSourceKey(dbSourceKey);
		        cred.setDrivers(props.getProperty(ConfigInfo.JDBC_DRIVERS));
		        cred.setUrl(props.getProperty(ConfigInfo.JDBC_URL));
		        cred.setUsername(props.getProperty(ConfigInfo.JDBC_USERNAME));
		        cred.setPassword(props.getProperty(ConfigInfo.JDBC_PASSWORD));
		        cred.setXpassword(props.getProperty(ConfigInfo.JDBC_XPASSWORD));
		        
		        server.auxPools_Put(dbSourceKey, cred);
					}
				}
			}
		}
	}
}
