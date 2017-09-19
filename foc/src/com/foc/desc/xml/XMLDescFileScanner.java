package com.foc.desc.xml;

import java.util.ArrayList;

import com.foc.Globals;
import com.foc.desc.FocModule;
import com.foc.desc.FocObject;
import com.foc.loader.FocFileLoader;
import com.foc.util.ASCII;

public class XMLDescFileScanner {

	private String    xmlDirectory = null;
	private String    javaPackage  = null;
	private FocModule module       = null;
	
	public XMLDescFileScanner(FocModule module, String javaPackage) {
		this.javaPackage  = javaPackage;
		this.xmlDirectory = "/"+javaPackage.replace(".", "/")+"/";
		this.module       = module;
	}
	
	public XMLDescFileScanner(FocModule module, String xmlDirectory, String javaPackage) {
		this.javaPackage  = javaPackage;
		this.xmlDirectory = xmlDirectory;
		this.module       = module;
	}
	
	public void dispose(){
		javaPackage  = null;
		xmlDirectory = null;
		module       = null;
	}

	private FocModule getFocModule(){
		return module;
	}
	
	private String getRootXmlDirectoryPath(){
		return xmlDirectory;
	}
	
	private String getJavaPackage() {
		return javaPackage;
	}
	
	public Class<XMLFocDesc> getFocDesc_Class(String xmlFileName, boolean oldFashion){
		String javaClassName = xmlFileName;
		if(oldFashion){
			javaClassName = ASCII.convertJavaNaming_ToVariableGetterSetterNaming(xmlFileName);
		}
		javaClassName = javaClassName.replace(".xml", "Desc"); 
		javaClassName = getJavaPackage() + "." + javaClassName;
		Class<XMLFocDesc> focDescClass = null;
		try{
			focDescClass = (Class<XMLFocDesc>) Class.forName(javaClassName);
		}catch (NoClassDefFoundError e1){
			focDescClass = null;
		}catch (Exception e1){
			focDescClass = null;
		}
		if(focDescClass == null && !oldFashion){
			focDescClass = getFocDesc_Class(xmlFileName, true);
		}
		if(focDescClass == null){
			focDescClass = XMLFocDesc.class;
		}
		return focDescClass;
	}

	public Class<FocObject> getFocObject_Class(String xmlFileName, boolean oldFashion, Class<FocObject> defaultClass){
		String focObjClassName = xmlFileName.replace(".xml", "");
		if(oldFashion){
			focObjClassName = ASCII.convertJavaNaming_ToVariableGetterSetterNaming(focObjClassName);
		}
		focObjClassName = getJavaPackage() + "." + focObjClassName;
		Class<FocObject> focObjClass = null;
		try{
			focObjClass = (Class<FocObject>) Class.forName(focObjClassName);
		}catch (NoClassDefFoundError e1){
			focObjClass = null;
		}catch (ClassNotFoundException e1){
			focObjClass = null;
		}catch (Exception e2){
			focObjClass = null;
		}
		if(focObjClass == null && !oldFashion){
			focObjClass = getFocObject_Class(xmlFileName, true, defaultClass);
		}
		if(focObjClass == null){
			focObjClass = defaultClass;
		}		
		return focObjClass;		
	}
	
	public void scanDirectory(){
		FocFileLoader fileLoader = new FocFileLoader();
		ArrayList<String> xmlFileNames = fileLoader.findFiles(getRootXmlDirectoryPath(), ".xml");
		fileLoader.dispose();
		
		if(xmlFileNames != null){
			for(int i=0; i<xmlFileNames.size(); i++){
				String xmlFileName = xmlFileNames.get(i);
				if(xmlFileName != null){
					int indexOfDOT = xmlFileName.lastIndexOf(".");
					if(indexOfDOT > 0){
						String storageName = xmlFileName.substring(0, xmlFileName.indexOf("."));

						Class defaultFocObjectClass = XMLFocObject.class;
						if(xmlFileName.endsWith("Filter.xml")){
							defaultFocObjectClass = XMLFocObjectFilterBindedToList.class;
						}
						
						Class<XMLFocDesc> focDescClass = getFocDesc_Class(xmlFileName, false);
						Class focObjClass  = getFocObject_Class(xmlFileName, false, defaultFocObjectClass);
						
						String fullXMLFileNameWithPath = getRootXmlDirectoryPath();
						if(fullXMLFileNameWithPath.endsWith("/")){
							fullXMLFileNameWithPath += xmlFileName; 
						}else{
							fullXMLFileNameWithPath += "/"+xmlFileName;
						}
						
						FocDescDeclaration_XMLBased declaration = new FocDescDeclaration_XMLBased(module, storageName, storageName, fullXMLFileNameWithPath, focDescClass, focObjClass);
						Globals.getApp().declaredObjectList_DeclareObject(declaration);
					}
				}
			}
		}
	}
}
