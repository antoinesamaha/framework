package com.foc.desc.xml;

import java.io.InputStream;
import java.lang.reflect.Constructor;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.foc.Globals;
import com.foc.IFocDescDeclaration;
import com.foc.desc.FocDesc;
import com.foc.desc.FocModule;
import com.foc.util.Utils;
import com.vaadin.server.ClassResource;

public class FocDescDeclaration_XMLBased implements IFocDescDeclaration {

	private FocModule           module      = null; 
	private Class<XMLFocDesc>   descClass   = null;
	private Class<XMLFocObject> objClass    = null;
	private String              xmlFileName = null;
	private String              name        = null;
	private String              storageName = null;
	private XMLFocDesc          focDesc     = null;
	
	public FocDescDeclaration_XMLBased(FocModule module, String name, String storageName, String xmlFileName, Class<XMLFocDesc> descClass, Class<XMLFocObject> objClass){
		this.name        = name;
		this.module      = module;
		this.xmlFileName = xmlFileName;
		this.descClass   = descClass;
		this.objClass    = objClass;
		this.storageName = storageName;
	}
	
	@Override
	public FocModule getFocModule() {
		return module;
	}

	@Override
	public int getPriority() {
		return IFocDescDeclaration.PRIORITY_FIRST;
	}

	public String getName() {
		return name;
	}

	@Override
	public FocDesc getFocDescription() {
		if(focDesc == null){
			focDesc = parse();
			if(focDesc != null) focDesc.afterXMLParsing();
		}
    return focDesc;
	}
	
	public XMLFocDesc newFocDesc(String forcedStorageName){
		if(!Utils.isStringEmpty(forcedStorageName)){
			storageName = forcedStorageName;
		}
	  try {
	    if (descClass != null) {
	      Class[] clss = new Class[4];
	      Object[] args = new Object[4];
	      {
	      	clss[0] = FocModule.class;
	      	args[0] = getFocModule();
	      	
	      	clss[1] = String.class;
	      	args[1] = storageName;
	      	
	      	clss[2] = String.class;
	      	args[2] = xmlFileName;
	      	
	      	clss[3] = Class.class;
	      	args[3] = objClass;
	      }
	      Constructor<XMLFocDesc> methodGetFocDesc = null;
	      try{
	      	methodGetFocDesc = descClass.getConstructor(clss);
	      }catch(NoSuchMethodException e){
	      	Globals.logException(e);
	      }
	      if(methodGetFocDesc != null){
	      	focDesc = (XMLFocDesc) methodGetFocDesc.newInstance(args);
	      	focDesc.setName(name);
	      	
					Globals.getApp().putIFocDescDeclaration(name, this);
	      }
	    }
	  } catch (Exception e) {
	  	Globals.logString("Exception while getting FocDesc for class : "+descClass.getName());
	    Globals.logException(e);
	  }
	  return focDesc;
	}

	public XMLFocDesc parse(){
		XMLFocDesc xmlFocDesc = null;
		try{
      ClassResource resource = null;
      InputStream inputStream = null;
      resource = new ClassResource(xmlFileName);
      inputStream = resource.getStream().getStream();

      SAXParserFactory factory = SAXParserFactory.newInstance();
      SAXParser saxParser = factory.newSAXParser();
      
      XMLFocDescParser focDescParser = new XMLFocDescParser(this);
      
      saxParser.parse(inputStream, focDescParser);
      xmlFocDesc = focDescParser.getXmlFocDesc();
    } catch (Exception e) {
      Globals.logString("Could not load file : " + xmlFileName);
      Globals.logException(e);
    }
		return xmlFocDesc;
	}
}
