package com.foc.web.unitTesting;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import com.foc.Globals;
import com.foc.access.FocLogger;
import com.vaadin.server.ClassResource;

public class FocUnitTestingSuite {

  private String name = null;
  private String fileName = null;
  private boolean showInMenu = false;

  private boolean parsingDone = false;
  private String startingTest = null;
  private Map<String, FocUnitTest> testMap = null;
  private FocUnitDictionary dictionary = null;
  
  public FocUnitTestingSuite() {
  }

  public FocUnitTestingSuite(FocUnitDictionary dictionary, String name, String fileName) {
    setName(name);
    setFileName(fileName);
    setDictionary(dictionary);
  }
  
  /**
   * This is used for the new Test that do not use XML and support parsing methods "test_" for test declataion
   * 
   * @param dictionary
   * @param name
   */
  public FocUnitTestingSuite(FocUnitDictionary dictionary, String name) {
    setName(name);
    setFileName(null);
    setDictionary(dictionary);

		setParsingDone(true);
		dictionary.put(this);
		
    declareAllTestMethods();
  }

  public void dispose(int mode) {
    
    if(mode == FocUnitDictionary.MODE_DISPOSE){
      name = null;
      fileName = null;
      dictionary = null;
      showInMenu = false;
    }

    parsingDone = false;
    
    if(testMap != null){
      Collection<FocUnitTest> collection = testMap.values();
      Iterator<FocUnitTest> itr = collection.iterator();
      
      while(itr.hasNext()){
        itr.next().dispose(mode);
      }
      
      collection.clear();
      collection = null;
      itr = null;
      
      testMap.clear();
      testMap = null;
    }
  }
  
  public void declareAllTestMethods() {
  	Class cls = getClass();
  	Method[] methodArray = cls.getMethods();
  	for(int i=0; i<methodArray.length; i++) {
  		Method m = methodArray[i];
  		try{
  			if(m.getName().startsWith("test_")) {
		      Class[] clss = new Class[0];
		      Object[] args = new Object[0];
  				m.invoke(this, clss);
  			}
			}catch (Exception e){
				Globals.logException(e);
			}
  	}
  }
  
  public void runSuite() throws Exception {
    FocUnitDictionary.getInstance().clear();
    String startingTest = getStartingTest();
    if (startingTest != null && getTestMap(false).get(startingTest) != null) {
      FocLogger.getInstance().openNode("Starting: Test " + startingTest + " in suite " + this.getName() + " by name.");
      runTestByName(startingTest);
      FocLogger.getInstance().closeNode("Ending: Test " + startingTest + " in suite " + this.getName() + " by name.");
    } else {
      FocLogger.getInstance().openNode("Starting: All tests in suite " + this.getName() + ".");
      runAllTests();
      FocLogger.getInstance().closeNode("Ending: All tests in suite " + this.getName() + ".");
    }
  }

  private void runAllTests() throws Exception {
    if (getTestMap(false) != null) {
      Collection<FocUnitTest> collection = getTestMap(false).values();

      if (collection != null) {
        Iterator<FocUnitTest> itr = collection.iterator();

        while (itr != null && itr.hasNext()) {
          FocUnitTest test = (FocUnitTest) itr.next();
          FocLogger.getInstance().openNode("Starting: Test " + test.getName() + " in suite " + test.getSuite().getName() + ".");
          test.init();
          test.runTest();
          FocLogger.getInstance().closeNode("Ending: Test " + test.getName() + " in suite " + test.getSuite().getName() + ".");
        }
      } else {
        FocLogger.getInstance().addError("Test suite " + this.getName() + " contains no tests.");
      }
    } else {
      FocLogger.getInstance().addError("Test suite " + this.getName() + " contains no tests.");
    }
  }

	public FocUnitTest getTest(String name) {
		return getTestMap(false).get(name);
	}
	
  public void runTestByName(String testName, FocUnitXMLAttributes callerArguments) throws Exception {
    if (testName != null) {
      Map<String, FocUnitTest> tempTestMap = getTestMap(false);
      if (tempTestMap != null) {
        FocUnitTest tempTest = tempTestMap.get(testName);
        if (tempTest != null) {
          tempTest.init();
          tempTest.setCallerArguments(callerArguments);
          tempTest.runTest();
        } else {
          FocLogger.getInstance().addFailure("Test " + testName + " not found in suite " + this.getName() + ".");
          Globals.logString("Available Tests in suite are:");
          Globals.logString("-----------------------------");
          Iterator<String> iter = tempTestMap.keySet().iterator();
          while(iter != null && iter.hasNext()){
          	String val = iter.next();
          	Globals.logString("Test: "+val);
          }
          Globals.logString("-----------------------------");
        }
      } else {
        FocLogger.getInstance().addError("Test map returned null in suite " + this.getName() + ".");
      }
    } else {
      FocLogger.getInstance().addError("Test name cannot be empty.");
    }
  }

  public void runTestByName(String testName) throws Exception {
    runTestByName(testName, null);
  }

  private void parseXML_IfNeeded() throws Exception {
    try {
      if (!isParsingDone()) {
        setParsingDone(true);
        String fileName = getFileName();
        ClassResource resource = null;
        InputStream inputStream = null;
        try {
          resource = new ClassResource(fileName);
          inputStream = resource.getStream().getStream();
        } catch (Exception e) {
          FocLogger.getInstance().addError("Could not load file : " + fileName);
          FocLogger.getInstance().addError(e.getMessage());
          Globals.logString("Could not load file : " + fileName);
          Globals.logException(e);
        }

        if (inputStream == null) {
          FocLogger.getInstance().addError("Input stream is null. Could not load file: " + fileName + ".");
        }

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        saxParser.parse(inputStream, new FocXMLUnitHandler());
      }
    } catch (Exception e) {
      Globals.logException(e);
      FocLogger.getInstance().addError(e.getMessage());
    }
  }

  private class FocXMLUnitHandler extends DefaultHandler {
    FocUnitTest test = null;

    public void startElement(String uri, String localName, String qName, Attributes attributes) {
      if (qName.equalsIgnoreCase(FXMLUnit.TAG_TEST_SUITE)) {
        FocLogger.getInstance(true).openNode("Parsing: Suite tag encountered.");
        if (attributes.getValue(FXMLUnit.ATT_STARTING_TEST) != null) {
          FocLogger.getInstance().addInfo("Parsing: Attribute " + FXMLUnit.ATT_STARTING_TEST + " encountered.");
          FocUnitTestingSuite.this.setStartingTest(attributes.getValue(FXMLUnit.ATT_STARTING_TEST));
        }
      } else if (qName.equalsIgnoreCase(FXMLUnit.TAG_TEST)) {
        FocLogger.getInstance().openNode("Parsing: Test tag encountered.");
        test = new FocUnitTest();
        test.setSuite(FocUnitTestingSuite.this);
        test.setName(attributes.getValue(FXMLUnit.ATT_NAME));
      } else {
        FocLogger.getInstance().addInfo("Parsing: " + qName + " tag encountered.");

        if (qName.equalsIgnoreCase(FXMLUnit.TAG_CALL)) {
          FocLogger.getInstance().openNode("Parsing: " + FXMLUnit.TAG_CALL + " tag encountered.");
          FocLogger.getInstance().addInfo("New node created.");
        }

        FocUnitMethodFactory methodFactory = FocUnitMethodFactory.getInstance();

        if (methodFactory.getMethodByName(qName) != null) {
          FocUnitTestingCommand command = new FocUnitTestingCommand(test, qName, attributes);
          test.addCommand(command);
          FocLogger.getInstance().addInfo("Parsing: Adding method " + command.getMethodName() + " to the test " + test.getName() + ".");
        } else {
          try{
						FocLogger.getInstance().addError("Parsing: Could not find method " + qName + " by name.");
					}catch (Exception e){
						Globals.logException(e);
					}
        }
      }

    }

    public void endElement(String uri, String localName, String qName) {
      if (qName.equalsIgnoreCase(FXMLUnit.TAG_TEST_SUITE)) {
        FocLogger.getInstance().closeNode("Parsing: Suite closing tag encountered.");
      } else if (qName.equalsIgnoreCase(FXMLUnit.TAG_TEST)) {
        if (test != null) {
          FocLogger.getInstance().addInfo("Parsing: Adding test " + test.getName() + " to suite " + FocUnitTestingSuite.this.getName() + ".");
          FocUnitTestingSuite.this.put(test);
        } else {
          try{
						FocLogger.getInstance().addError("Parsing: The created test is empty.");
					}catch (Exception e){
						Globals.logException(e);
					}
        }
        FocLogger.getInstance().closeNode("Parsing: Test closing tag encountered.");
      } else if (qName.equalsIgnoreCase(FXMLUnit.TAG_CALL)) {
        FocLogger.getInstance().closeNode("Parsing: Closing " + FXMLUnit.TAG_CALL + " tag encountered.");
        FocLogger.getInstance().addInfo("Node has been closed.");
      }
    }
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getStartingTest() {
    try{
			parseXML_IfNeeded();
		}catch (Exception e){
			Globals.logException(e);
		}
    return startingTest;
  }

  public void setStartingTest(String startingTest) {
    this.startingTest = startingTest;
  }

  public Map<String, FocUnitTest> getTestMap(boolean createIfNeeded) {
    try{
			parseXML_IfNeeded();
		}catch (Exception e){
			Globals.logException(e);
		}
    if (testMap == null && createIfNeeded) {
      testMap = new HashMap<String, FocUnitTest>();
    }
    return testMap;
  }

  public void put(FocUnitTest test) {
    getTestMap(true).put(test.getName(), test);
  }

  public boolean isParsingDone() {
    return parsingDone;
  }

  public void setParsingDone(boolean parsingDone) {
    this.parsingDone = parsingDone;
  }
  
  public FocUnitDictionary getDictionary() {
    return dictionary;
  }

  protected void setDictionary(FocUnitDictionary dictionary) {
    this.dictionary = dictionary;
  }

  public boolean isShowInMenu() {
    return showInMenu;
  }

  public void setShowInMenu(boolean showInMenu) {
    this.showInMenu = showInMenu;
  }

}