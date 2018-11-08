package com.foc.loader;

import java.io.File;
import java.io.FileFilter;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.foc.Globals;
import com.foc.util.Utils;

public class FocFileLoader {

	public FocFileLoader() {
	}
	
	public void dispose(){
	}
	
	public ArrayList<String> findFiles(String rootDirectory, String extension){
		ArrayList<String> xmlFileNames = null;
		
		if(!rootDirectory.endsWith("/")) {
			rootDirectory += "/";
		}
		if(!rootDirectory.startsWith("/")) {
			rootDirectory = "/"+rootDirectory;
		}
		
		try{
//			Globals.logString("GETTING RESOURCE URL:"+rootDirectory);
			URL url = getClass().getResource(rootDirectory);
			URI uri = url != null ? url.toURI() : null;
			
			if(uri != null){
				if(uri.getScheme().equals("jar")){
					JarURLConnection connection = (JarURLConnection) url.openConnection();
					File file = new File(connection.getJarFileURL().toURI());
					
					if(rootDirectory.startsWith("/")) {
						rootDirectory = rootDirectory.substring(1);
					}
					
				  final JarFile jar = new JarFile(file);
				  final Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
			    while(entries.hasMoreElements()) {
		        String name = entries.nextElement().getName();
		        if (    name.startsWith(rootDirectory)
		        		&& (Utils.isStringEmpty(extension) || name.endsWith(extension))
		        		) { //filter according to the path
		        	//xmlFileNames.add(name.replace(xmlDirectoryPath, ""));
		        	int firstIndex = rootDirectory.length();
	//	        	int firstIndex = name.lastIndexOf('/') + 1;
		        	int lastIndex  = name.length();
		        	if(firstIndex < lastIndex){
			        	name = name.substring(firstIndex, lastIndex);
			        	if(!name.contains("/")){
				        	if(xmlFileNames == null){
				        		xmlFileNames = new ArrayList<String>();
				        	}
				        	xmlFileNames.add(name);
			        	}
		        	}
		        }
			    }
			    jar.close();
				}else{
					File xmlDirectoryFiles = new File(uri);
					File[] xmlFiles = xmlDirectoryFiles != null ? xmlDirectoryFiles.listFiles(new ExtensionFilter(extension)) : null;
					if(xmlFiles != null){
						for(int i=0; i<xmlFiles.length; i++){
							File xmlFile = xmlFiles[i];
		        	if(xmlFileNames == null){
		        		xmlFileNames = new ArrayList<String>();
		        	}
							xmlFileNames.add(xmlFile.getName());
						}
					}
				}
			}
		}catch(Exception e){
			Globals.logString("GETTING RESOURCE URL:"+rootDirectory);
			Globals.logException(e);
		}
		
		return xmlFileNames;
	}
	
	private class ExtensionFilter implements FileFilter {

		private String extension = null;

		public ExtensionFilter(String extension){
			if(!Utils.isStringEmpty(extension)){
				this.extension = extension;
				if(!this.extension.startsWith(".")) this.extension = "."+this.extension; 
			}
		}
		
		@Override
		public boolean accept(File pathname) {
			if(extension != null){
				return pathname != null && pathname.getName().endsWith(extension);
			}else{
				return true;
			}
		}
		
	}
}
