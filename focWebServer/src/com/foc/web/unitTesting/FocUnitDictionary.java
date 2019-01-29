package com.foc.web.unitTesting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.foc.ConfigInfo;
import com.foc.Globals;
import com.foc.access.FocLogLineDesc;
import com.foc.access.FocLogLineTree;
import com.foc.access.FocLogger;
import com.foc.htmldoc.HTMLDocumentation;
import com.foc.loader.FocFileLoader;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.util.Utils;
import com.foc.vaadin.FocWebApplication;
import com.foc.vaadin.FocWebEnvironment;
import com.foc.vaadin.ICentralPanel;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.FocWebServer;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;

public class FocUnitDictionary {
  private Map<String, FocUnitTestingSuite> testSuiteMap  = null;
  private LinkedList<FocUnitTestingSuite>  suiteSequence = null;
  private Map<String, Object>              xmlVariables  = null;
  private HTMLDocumentation                documentation = null;

  private FocUnitExpectedNotification expectedNotification = null;  

  private boolean                 nextTestExist = false;
  private boolean                 pause     = false;
  private Stack<FocUnitTestLevel> testStack = null;
  
//	private String                  nextSuiteName = null;
//	private String                  nextTestName  = null;
  private boolean                 exitTesting   = false;
  
  private int currentSuite = -1;
  private int currentTest  = -1;
  
  public static int MODE_CLEAR   = 0;
  public static int MODE_DISPOSE = 1;
  
  public FocUnitDictionary() {
  	suiteSequence = new LinkedList<FocUnitTestingSuite>();
  	
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
    
    if(testStack != null){
    	Iterator<FocUnitTestLevel> iter = testStack.iterator();
    	while(iter != null && iter.hasNext()){
    		FocUnitTestLevel level = iter.next();
    		if(level != null){
    			level.dispose();
    		}
    	}
    		
    	testStack.clear();
    	testStack = null;
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
  
  public Object getXMLVariable(String key){
    return getXMLVariables() != null ? getXMLVariables().get(key) : null;
  }
    
  private Map<String, FocUnitTestingSuite> getTestingSuiteMap() {
    if(testSuiteMap == null) {
      testSuiteMap = new HashMap<String, FocUnitTestingSuite>();
    }
    
    return testSuiteMap;
  }
  
  public void put(FocUnitTestingSuite suite) {
  	put(suite, false);
  }

  public void putAndSequence(FocUnitTestingSuite suite) {
  	put(suite, true);
  }
  
  private void put(FocUnitTestingSuite suite, boolean addToSequence) {
    getTestingSuiteMap().put(suite.getName(), suite);
    if(addToSequence) suiteSequence.add(suite);
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
  
  public void expectedNotification_Occured(String notificationMessage, String description, int notificationType) throws Exception {
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
  
  public void initializeCurrentSuiteAndTest(String suiteName, String testName) {
  	currentTest  = -1;
  	currentSuite = -1;
  	
  	if(!Utils.isStringEmpty(suiteName)) {
	  	for(int s=0; s<suiteSequence.size() && currentSuite < 0; s++) {
	  		FocUnitTestingSuite suite = suiteSequence.get(s);
	  		if(suite.getName().equals(suiteName)) {
	  			currentSuite = s;
	  			if(Utils.isStringEmpty(testName)) {
	  				currentTest = 0;
	  			} else {
		  			for(int t=0; t<suite.testSequence_Size() && currentTest < 0; t++) {
		  				FocUnitTest test = suite.testSequence_Get(t);
		  				if(test.getName().equals(testName)) {
		  					currentTest = t;
		  				}
		  			}
	  			}
	  		}
	  	}
  	}
  }
  
  public void runSequence() throws Exception {
  	setExitTesting(false);
  	if(currentSuite == -1) {
  		currentSuite = 0;
  		currentTest = 0;
  	}
  	
  	if(suiteSequence != null) {
  		for(; currentSuite<suiteSequence.size(); currentSuite++) {
  			FocUnitTestingSuite suite = suiteSequence.get(currentSuite);
  			for(; currentTest<suite.testSequence_Size(); currentTest++) {
  				FocUnitTest test = suite.testSequence_Get(currentTest);
  				if(test != null) {
  					test.runTest();
  					if(isExitTesting()) break;
  				}
  			}
  			if(isExitTesting()) break;
  			currentTest = 0;
  		}
  		if(currentSuite >= suiteSequence.size()) {
  		  currentSuite = -1;
  		  currentTest = -1;
  		  INavigationWindow window = FocWebApplication.getInstanceForThread().getNavigationWindow();
  		  FocUnitDictionary.getInstance().popupLogger(window);
  		}
  	}
  }
  
  public void incrementTestIndexes() {
  	if(currentSuite >= 0) {
  		FocUnitTestingSuite suite = suiteSequence.get(currentSuite);
  		if(suite != null && currentTest >= 0) {
  			currentTest++;
  			if(currentTest >= suite.testSequence_Size()) {
  				currentTest = 0;
  				currentSuite++;
  			}
  		}
  	}
  }
  
  public void runUnitTest(String suiteName) throws Exception {
  	//		INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
  	
  	//VaadinRequest vaadinRequest = VaadinService.getCurrent().getCurrentRequest();
  	//String path = vaadinRequest.getPathInfo();
  	
    FocUnitTestingSuite suite = getTestingSuite(suiteName);
    if(suite == null){
    	FocUnitDictionary.getInstance().getLogger().addError("Test suite not found: "+suiteName);
    }else{
			try {
				Globals.getApp().setIsUnitTest(true);
    		setNextTestExist(false);
			  FocUnitDictionary.getInstance().getTestingSuite(suite.getName()).runSuite();
			} catch (Exception e) {
			  Globals.logException(e);
			  FocUnitDictionary.getInstance().getLogger().addError(e.getMessage());
			}
    }
  }
  
  public void continueTestByName(String suiteName, String testName) throws Exception {
    FocUnitTestingSuite suite = getTestingSuite(suiteName);
		
		try {
			Globals.getApp().setIsUnitTest(true);
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
  
  public void stackPush(FocUnitTest test, int commandIndex){
  	FocUnitTestLevel level = new FocUnitTestLevel(test, commandIndex);
  	if(testStack == null){
  		testStack = new Stack<FocUnitTestLevel>();
  	}
  	testStack.push(level);
  }
  
  public void stackSetCommand(int commandIndex){
  	FocUnitTestLevel level = testStack != null ? testStack.peek() : null;
  	if(level != null){
  		level.setCommandIndex(commandIndex);
  	}
  }
  
  public void stackPop(){
  	if(testStack != null){
  		testStack.pop();
  	}
  }
  
  public void pause(){
  	pause = true;
  }
  
  public void resume(){
  	pause = false;
  }
  
  public boolean isPause(){
  	return pause;
  }

	public boolean isNextTestExist() {
		return nextTestExist;
	}

	public void setNextTestExist(boolean nextTestExist) {
		this.nextTestExist = nextTestExist;
	}

	public boolean isExitTesting() {
		return exitTesting;
	}

	public void setExitTesting(boolean exitTesting) {
		this.exitTesting = exitTesting;
	}
		
	public boolean hasNextTest() {
		return getCurrentSuite() >= 0;
	}

	public int getCurrentSuite() {
		return currentSuite;
	}

	public void setCurrentSuite(int currentSuite) {
		this.currentSuite = currentSuite;
	}

	public int getCurrentTest() {
		return currentTest;
	}

	public void setCurrentTest(int currentTest) {
		this.currentTest = currentTest;
	}
	
	public void unitDoc_BodyClose() {
		if(documentation != null) {
			documentation.close();
		}
	}
	
	public void unitDoc_CreateIfNeeded() {
	  try{
	  	if(ConfigInfo.isUnitAllowed() && documentation == null) {
	  		documentation = new HTMLDocumentation(ConfigInfo.getLogDir(), "UnitTestingDefinition");
	  	}
		}catch (Exception e){
			Globals.logException(e);
		}
	}
	
	public void unitDoc_AddSuite(String suiteName, String description) {
		unitDoc_CreateIfNeeded();
		if(documentation != null) {
			documentation.addSection(suiteName, description);
		}
	}
	
	public void unitDoc_AddTest(String testName, String description, boolean implemented) {
		unitDoc_CreateIfNeeded();
		if(documentation != null) {
			String title = testName;
			if(!implemented) {
				title += "   !NOT IMPLEMENTED!";
			}
			documentation.addCollapsible(title, description);
		}
	}
}
