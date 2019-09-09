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
package com.foc.web.server.xmlViewDictionary.xmlViewKeyGenerator;

import java.util.ArrayList;
import java.util.StringTokenizer;

import com.fab.codeWriter.CodeWriterConstants;
import com.foc.loader.FocFileLoader;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.util.Utils;
import com.foc.vaadin.FocWebModule;
import com.foc.web.server.xmlViewDictionary.XMLView;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;

public class XmlViewFileScanner {

	//STORAGENAME^CONTEXT^USERVIEW^TYPE.xml
	
	private static final String FILE_NAME_SEPERATOR     = CodeWriterConstants.XML_VIEW_FILE_NAME_SEPERATOR;
	private static final String FILE_LANGUAGE_SEPARATOR = "-";
	
	private FocWebModule focWebModule         = null;
	private String       rootXmlDirectoryPath = null;
	private String       javaPackage          = null;
	
	public XmlViewFileScanner(FocWebModule focWebModule, String javaPackage) {
		this.javaPackage          = javaPackage;
		this.rootXmlDirectoryPath = "/"+javaPackage.replace(".", "/")+"/";
		this.focWebModule         = focWebModule;
	}
	
	public XmlViewFileScanner(FocWebModule focWebModule,String rootXmlDirectoryPath, String javaPackage) {
		this.javaPackage          = javaPackage;
		this.rootXmlDirectoryPath = rootXmlDirectoryPath;
		this.focWebModule         = focWebModule;
	}
	
	public void dispose(){
		javaPackage          = null;
		rootXmlDirectoryPath = null;
		focWebModule         = null;
	}
	
	public boolean isSearchForClassBySimilarName(){
		return false;
	}
	
	private Class getJavaClass_WithSimilarName(String xmlFileName, boolean removeLanguage){
		String javaClassName = toJavaClassName(xmlFileName, removeLanguage);
		javaClassName        = getJavaPackage() + "." + javaClassName;
		Class<?> layoutClass = null; 
		try{
			layoutClass = Class.forName(javaClassName);
		}catch (ClassNotFoundException e1){
			layoutClass = null;
		}
		return layoutClass;
	}
	
	public void scanPush(){
		FocFileLoader fileLoader = new FocFileLoader();
		ArrayList<String> xmlFileNames = fileLoader.findFiles(getRootXmlDirectoryPath(), ".xml");
		fileLoader.dispose();
		
//		File[] xmlFiles = xmlDirectoryFiles != null ? xmlDirectoryFiles.listFiles(new XMLFileFilter()) : null;
		if(xmlFileNames != null){
			for(int i=0; i<xmlFileNames.size(); i++){
				String xmlFileName = xmlFileNames.get(i);
				if(xmlFileName != null){
					int indexOfDOT = xmlFileName.lastIndexOf(".");
					if(indexOfDOT > 0){
						String nameWithoutExtension = xmlFileName.substring(0, xmlFileName.indexOf("."));
						StringTokenizer xmlFileNameStringTokenizer = new StringTokenizer(nameWithoutExtension, FILE_NAME_SEPERATOR);
						
//						ClassFinder classFinder = new ClassFinder(javaClassName);
//						Class focXmlLayoutClass = classFinder.locateImplementation();
						
						XMLViewKey xmlViewKey = new XMLViewKey();
						
						ArrayList<String> tokens = new ArrayList<String>();
						while(xmlFileNameStringTokenizer.hasMoreTokens()){
							String token = xmlFileNameStringTokenizer.nextToken();
							tokens.add(token);
						}
						
						if(tokens.size() >= 2 && tokens.size() <= 5){
							xmlViewKey.setStorageName(tokens.get(0));
							String lastToken = tokens.get(tokens.size() - 1);
							if(lastToken != null){
								int indexOfDash = lastToken.indexOf("-");
								if(indexOfDash > 0 && indexOfDash<lastToken.length()) {
									xmlViewKey.setLanguage(lastToken.substring(indexOfDash+1));
									lastToken = lastToken.substring(0, indexOfDash);
								}
								xmlViewKey.setType(lastToken.toLowerCase());
							}
							
							if(tokens.size() == 5){
								xmlViewKey.setContext(tokens.get(1));
								xmlViewKey.setUserView(tokens.get(2));
								String third = tokens.get(3); 
								if(third.equals(XMLViewKey.IS_MOBILE_FRIENDLY)){
									xmlViewKey.setMobileFriendly(true);
								}
							}else if(tokens.size() == 4){
								xmlViewKey.setContext(tokens.get(1));
								xmlViewKey.setUserView(tokens.get(2));
							}else if(tokens.size() == 3){
								String contextToken = tokens.get(1);
								if(contextToken.equals(XMLViewKey.IS_MOBILE_FRIENDLY)){
									xmlViewKey.setContext(XMLViewKey.CONTEXT_DEFAULT);
									xmlViewKey.setMobileFriendly(true);
								}else{
									xmlViewKey.setContext(contextToken);
								}
								xmlViewKey.setUserView(XMLViewKey.VIEW_DEFAULT);
							}else{
								xmlViewKey.setContext(XMLViewKey.CONTEXT_DEFAULT);
								xmlViewKey.setUserView(XMLViewKey.VIEW_DEFAULT);
							}
						}
						if(XMLViewDictionary.getInstance().get_Strictly(xmlViewKey) == null){
//							if(isSearchForClassBySimilarName()){
//				        try {
//				        	String fullJavaClassPath = javaClassName;
//				        	if(fullJavaClassPath != null && fullJavaClassPath.contains(".java")){
//				        		fullJavaClassPath = getFocWebModule().getClass().getPackage().getName() + "." + fullJavaClassPath.replace(".java", "");
//				        	}
//									focXmlLayoutClass = fullJavaClassPath != null ? Class.forName(fullJavaClassPath) : null;
//								} catch (ClassNotFoundException e) {
//									Globals.logExceptionWithoutPopup(e);
//								}
//							}
			        //
							
							Class layoutClass = getJavaClass_WithSimilarName(xmlFileName, false);
							if(layoutClass == null && !Utils.isStringEmpty(xmlViewKey.getLanguage())) {
								layoutClass = getJavaClass_WithSimilarName(xmlFileName, true);
							}
							String layoutClassName = layoutClass != null ? layoutClass.getName() : null;
							String xmlFullName = getRootXmlDirectoryPath();
							if(!xmlFullName.endsWith("/")) xmlFullName += "/";
							xmlFullName += xmlFileName;
			        XMLView xmlView = new XMLView(xmlViewKey, xmlFullName, layoutClassName);
							XMLViewDictionary.getInstance().put(xmlView);
						}
					}
				}
			}
		}
	}
	
	private String toJavaClassName(String xmlFileName, boolean removeLanguage){
		String javaFileName = null;
		if(xmlFileName != null){
			javaFileName = xmlFileName.replace(".xml", "");
			javaFileName = javaFileName.replace(FILE_NAME_SEPERATOR, "_");

			if(removeLanguage) {
				int indexOfDash = javaFileName.indexOf(FILE_LANGUAGE_SEPARATOR);
				if(indexOfDash > 0) {
					javaFileName = javaFileName.substring(0, indexOfDash);
				}
			}
		}
		return javaFileName;
	}
	
	private FocWebModule getFocWebModule(){
		return focWebModule;
	}
	
	private String getRootXmlDirectoryPath(){
		return rootXmlDirectoryPath;
	}
	
	private String getJavaPackage() {
		return javaPackage;
	}
}
