package com.foc.web.unitTesting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.foc.Globals;
import com.foc.access.FocLogLineDesc;
import com.foc.access.FocLogLineTree;
import com.foc.access.FocLogger;
import com.foc.loader.FocFileLoader;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.util.Utils;
import com.foc.vaadin.FocWebEnvironment;
import com.foc.vaadin.ICentralPanel;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.FocWebServer;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;

public class FocUnitDictionary {
  private Map<String, FocUnitTestingSuite> testSuiteMap = null; 
  private Map<String, Object> xmlVariables              = null;
  
  private FocUnitExpectedNotification expectedNotification = null;  
  
  public static int MODE_CLEAR   = 0;
  public static int MODE_DISPOSE = 1;
  
  public FocUnitDictionary() {
    FocUnitTestingSuite suite = null;

    suite = new FocUnitTestingSuite(this, "ADMIN", "/xmlUnitTesting/admin/Admin.xml");
    put(suite);

    suite = new FocUnitTestingSuite(this, "GROUPS", "/xmlUnitTesting/admin/Groups.xml");
    //suite.setShowInMenu(false);
    put(suite);

    suite = new FocUnitTestingSuite(this, "WORKFLOW", "/xmlUnitTesting/admin/Workflow.xml");
    put(suite);

    suite = new FocUnitTestingSuite(this, "ADR_BOOK_PARTY", "/xmlUnitTesting/business/AddressBook.xml");
    put(suite);

    suite = new FocUnitTestingSuite(this, "CALENDAR", "/xmlUnitTesting/business/Calendar.xml");
    put(suite);
  }

  public FocUnitDictionary(String rootDirectory) {
  	declareFolderFiles(rootDirectory);
  }
  
  public void declareFolderFiles(String rootDirectory) {
  	if(!Utils.isStringEmpty(rootDirectory)){
  		if(!rootDirectory.endsWith("/")) {
  			rootDirectory += "/";
  		}
  		if(!rootDirectory.startsWith("/")) {
  			rootDirectory = "/"+rootDirectory;
  		}
  		
  		FocFileLoader loader = new FocFileLoader();
	  	ArrayList<String> fileArray = loader.findFiles(rootDirectory, "xml");
	  	loader.dispose();
	  	
	  	if(fileArray != null){
		  	for(int i=0; i<fileArray.size(); i++){
		  		String fileNameWithExtension = fileArray.get(i);
		  		int lastDot   = fileNameWithExtension.lastIndexOf(".xml");
		  		if(lastDot > 0){
		  			String smallName = fileNameWithExtension.substring(0, lastDot);
		  			
		  			FocUnitTestingSuite suite = new FocUnitTestingSuite(this, smallName, rootDirectory+fileNameWithExtension);
		  	    put(suite);
		  		}
		  	}
	  	}
  	}
  }
  
  public void dispose() {
  	dispose(MODE_DISPOSE);
  }
  
  public void clear(){
  	dispose(MODE_CLEAR);
  }
  	
  private void dispose(int mode) {
    FocLogger.getInstance().dispose();
    Collection<FocUnitTestingSuite> collection = testSuiteMap.values();
    Iterator<FocUnitTestingSuite> itr = collection.iterator();
    
    while(itr.hasNext()){
      itr.next().dispose(mode);
    }
    collection = null;
    itr = null;
    
    if(mode == MODE_DISPOSE){
      testSuiteMap.clear();
      testSuiteMap = null;      
    }
 
    if(xmlVariables != null){
      xmlVariables.clear();
      xmlVariables = null;
    }
  }
  
  public Map<String, Object> getXMLVariables(){
    if(xmlVariables == null) {
      xmlVariables = new HashMap<String, Object>();
    }
    return xmlVariables;
  }
  
  public void putXMLVariable(String key, Object object){
    getXMLVariables().put(key, object);
  }
    
  private Map<String, FocUnitTestingSuite> getTestingSuiteMap() {
    if(testSuiteMap == null) {
      testSuiteMap = new HashMap<String, FocUnitTestingSuite>();
    }
    
    return testSuiteMap;
  }
  
  public void put(FocUnitTestingSuite suite) {
    getTestingSuiteMap().put(suite.getName(), suite);
  }
  
  public FocUnitTestingSuite getTestingSuite(String name) {
    return getTestingSuiteMap().get(name);
  }
  
  public int getDictionarySize() throws SAXException, IOException, ParserConfigurationException {
    return getInstance().getTestingSuiteMap().size();
  }
  
  public Collection<FocUnitTestingSuite> getTestingSuiteMapValues() {
    return getTestingSuiteMap().values();
  }
  
  public FocLogger getLogger() {
    return Globals.getApp().getFocLogger(true);
  }

  public FocUnitExpectedNotification expectedNotification_Get(){
  	return expectedNotification;
  }
  
  public void expectedNotification_Set(String notificationMessage, String description, String notificationType){
		int notificationTypeNumber = notificationType != null ? Integer.valueOf(notificationType) : FocWebEnvironment.TYPE_NOT_DEFINED;
		expectedNotification = new FocUnitExpectedNotification(notificationMessage, description, notificationTypeNumber);
  }
  
  public void expectedNotification_Occured(String notificationMessage, String description, int notificationType){
  	FocUnitExpectedNotification notif = expectedNotification_Get();
  	if(notif == null){
    	getLogger().addFailure("Notification not expected! "+notificationMessage+" : "+description);
  	}else{
  		notif.assertNotification(notificationMessage, description, notificationType);
  		expectedNotification = null;
  	}
  }
  
  public static FocUnitDictionary getInstance() {
    FocUnitDictionary unitDictionary = null;
    if(FocWebServer.getInstance() != null){
      unitDictionary = FocWebServer.getInstance().getUnitDictionary();
    }
    return unitDictionary;
  }
  
  public void runUnitTest(String suiteName){
    FocUnitTestingSuite suite = getTestingSuite(suiteName);
  	
//		INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
		  
		//VaadinRequest vaadinRequest = VaadinService.getCurrent().getCurrentRequest();
		//String path = vaadinRequest.getPathInfo();
		
		try {
			Globals.getApp().setIsUnitTest(true);
		  FocUnitDictionary.getInstance().getTestingSuite(suite.getName()).runSuite();
		} catch (Exception e) {
		  Globals.logException(e);
		  FocUnitDictionary.getInstance().getLogger().addError(e.getMessage());
		}
  }
  
  public void continueTestByName(String suiteName, String testName){
    FocUnitTestingSuite suite = getTestingSuite(suiteName);
		
		try {
			suite.runTestByName(testName);
		} catch (Exception e) {
		  Globals.logException(e);
		  FocUnitDictionary.getInstance().getLogger().addError(e.getMessage());
		}
  	
  }
  
  public void popupLogger(INavigationWindow mainWindow){
    FocLogLineTree tree = FocUnitDictionary.getInstance().getLogger().getTree();
    tree.recompute();

    XMLViewKey xmlViewKey = new XMLViewKey(FocLogLineDesc.getInstance().getStorageName(), XMLViewKey.TYPE_TREE);
    ICentralPanel centralPanel = XMLViewDictionary.getInstance().newCentralPanel((INavigationWindow) mainWindow, xmlViewKey, tree);
    mainWindow.changeCentralPanelContent(centralPanel, true);
  }
}
