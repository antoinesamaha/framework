package com.foc.web.server.xmlViewDictionary.xmlViewKeyGenerator;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

import com.fab.codeWriter.CodeWriterConstants;
import com.foc.Globals;
import com.foc.loader.FocFileLoader;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.FocWebModule;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.server.xmlViewDictionary.XMLView;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;

public class XmlViewFileScanner {

	//STORAGENAME^CONTEXT^USERVIEW^TYPE.xml
	
	private static final String FILE_NAME_SEPERATOR = CodeWriterConstants.XML_VIEW_FILE_NAME_SEPERATOR;
	
	private FocWebModule focWebModule         = null;
	private String       rootXmlDirectoryPath = null;
	private String       javaPackage    = null;
	
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
	
	public Class getJavaClass_WithSimilarName(String xmlFileName){
		String javaClassName = toJavaClassName(xmlFileName);
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
						
						if(tokens.size() >= 2 && tokens.size() <= 4){
							xmlViewKey.setStorageName(tokens.get(0));
							if(tokens.get(tokens.size() - 1) != null){
								xmlViewKey.setType(tokens.get(tokens.size() - 1).toLowerCase());
							}
							
							if(tokens.size() == 4){
								xmlViewKey.setContext(tokens.get(1));
								xmlViewKey.setUserView(tokens.get(2));
							}else if(tokens.size() == 3){
								xmlViewKey.setContext(tokens.get(1));
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
							Class  layoutClass = getJavaClass_WithSimilarName(xmlFileName);
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
	
	private String toJavaClassName(String xmlFileName){
		String javaFileName = null;
		if(xmlFileName != null){
			javaFileName = xmlFileName.replace(".xml", "");
			javaFileName = javaFileName.replace(FILE_NAME_SEPERATOR, "_");
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
	
	/*
	private void createJavaClassIfNotExists(String fileName){
		try{
//			URL url = getClass().getResource(getJavaDirectoryPath());
//			File rootModuleDirectoryFile = new File(url.toURI());
//			String fullJavaDirectoryPath = rootModuleDirectoryFile.getPath();
			String fullJavaDirectoryPath = getJavaPackage();
			
			fileName = toJavaClassName(fileName);
			File directory = new File(fullJavaDirectoryPath);
			if(!directory.exists()){
				directory.mkdirs();
			}
			
			File javaClassFile = new File(fullJavaDirectoryPath, fileName);
			if(!javaClassFile.exists()){
				javaFocXmlLayoutFileWriter(javaClassFile);
				boolean isCreated = javaClassFile.createNewFile();
				Globals.logString(fileName + ".java Object created in " + javaClassFile.getAbsolutePath());
			}
		}catch(Exception e){
			Globals.logException(e);
		}
	}
	
	private void javaFocXmlLayoutFileWriter(File focXmlLayoutFile){
		if(focXmlLayoutFile != null){
			try{
				String javaClassName = focXmlLayoutFile.getName();
				if(javaClassName.contains(".java")){
					javaClassName = javaClassName.replace(".java", "");
				}
				FileWriter fileWriter = new FileWriter(focXmlLayoutFile);
				fileWriter.append("package " + getFocWebModule().getClass().getPackage().getName() + ";\n\n");
				fileWriter.append("import " + FocXMLLayout.class.getName() + ";\n\n");
				fileWriter.append("public class " + javaClassName + " extends FocXMLLayout{");
				fileWriter.append("\n\n}");
				fileWriter.flush();
				fileWriter.close();
			}catch(Exception e){
				Globals.logException(e);
			}
		}
	}
	*/
}
