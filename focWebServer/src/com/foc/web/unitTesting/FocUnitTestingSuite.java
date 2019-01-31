package com.foc.web.unitTesting;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import com.foc.Globals;
import com.foc.access.FocLogger;
import com.foc.annotations.unit.FUSuite;
import com.foc.annotations.unit.FUTest;
import com.vaadin.server.ClassResource;

public class FocUnitTestingSuite {

  private String name = null;
  private String fileName = null;
  private boolean showInMenu = false;

  private boolean parsingDone = false;
  private String startingTest = null;
  
  private Map<String, FocUnitTest> testMap = null;
  private LinkedList<FocUnitTest> testSequence = null;
  
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
		dictionary.putAndSequence(this);
		
		String description = "";
		FUSuite suiteAnnotation = getClass().getAnnotation(FUSuite.class);
		if(suiteAnnotation != null) {
			description = suiteAnnotation.description();
		}
		dictionary.unitDoc_AddSuite(name, description);
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
  	
  	Method[] methodArray = getDeclaredMethodsInOrder(cls);
  	
//  	Method[] methodArray = cls.getMethods();
  	for(int i=0; i<methodArray.length; i++) {
  		Method m = methodArray[i];
  		try{
  			boolean isATestMethod = m.getName().startsWith("test_");
  			boolean isImplemented = true;
  			String description = "";
				FUTest ann = m.getAnnotation(FUTest.class);
				if(ann != null) {
					isATestMethod = true;
					isImplemented = ann.implemented();
					description = ann.description();
				}
  			
  			if(isATestMethod) {
  				getDictionary().unitDoc_AddTest(m.getName(), description, isImplemented);
  				if(isImplemented) {
			      Class[] clss = new Class[0];
			      Object[] args = new Object[0];
	  				m.invoke(this, clss);
  				}
  			}
			}catch (Exception e){
				Globals.logException(e);
			}
  	}
  }
  
  //REVERTME final
  public void runSuite() throws Exception {
  	if(!FocUnitDictionary.getInstance().isExitTesting()) {
//	    FocUnitDictionary.getInstance().clear();
      FocLogger.getInstance().openNode("Starting suite " + this.getName());
	    execute();
      FocLogger.getInstance().closeNode();
  	}
  }
  
  public void execute() throws Exception {
    String startingTest = getStartingTest();
    if (startingTest != null && getTestMap(false).get(startingTest) != null) {
      runTestByName(startingTest);
    } else {
      runAllTests();
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
    if(testSequence == null) testSequence = new LinkedList<FocUnitTest>();
    testSequence.add(test);
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

  public int testSequence_Size() {
  	return testSequence != null ? testSequence.size() : 0;
  }
  
  public FocUnitTest testSequence_Get(int at) {
  	return testSequence != null ? testSequence.get(at) : null;
  }
  
  
  
  /** Grok the bytecode to get the declared order */
  public static Method[] getDeclaredMethodsInOrder(Class clazz) {
      Method[] methods = null;
      try {
          String resource = clazz.getName().replace('.', '/')+".class";

          methods = clazz.getDeclaredMethods();

          InputStream is = clazz.getClassLoader()
              .getResourceAsStream(resource);

          if (is == null) {
              return methods;
          }

          java.util.Arrays.sort(methods,new ByLength());
          ArrayList<byte[]> blocks = new ArrayList<byte[]>();
          int length = 0;
          for (;;) {
              byte[] block = new byte[16*1024];
              int n = is.read(block);
              if (n > 0) {
                  if (n < block.length) {
                      block = java.util.Arrays.copyOf(block,n);
                  }
                  length += block.length;
                  blocks.add(block);
              } else {
                  break;
              }
          }

          byte[] data = new byte[length];
          int offset = 0;
          for (byte[] block : blocks) {
              System.arraycopy(block,0,data,offset,block.length);
              offset += block.length;
          }

          String sdata = new String(data,java.nio.charset.Charset.forName("UTF-8"));
          int lnt = sdata.indexOf("LineNumberTable");
          if (lnt != -1) sdata = sdata.substring(lnt+"LineNumberTable".length()+3);
          int cde = sdata.lastIndexOf("SourceFile");
          if (cde != -1) sdata = sdata.substring(0,cde);
          
          MethodOffset mo[] = new MethodOffset[methods.length];

          
          for (int i=0; i<methods.length; ++i) {
              int pos = -1;
              for (;;) {
                  pos=sdata.indexOf(methods[i].getName(),pos);
                  if (pos == -1) break;
                  boolean subset = false;
                  for (int j=0; j<i; ++j) {
                      if (mo[j].offset >= 0 &&
                          mo[j].offset <= pos &&
                          pos < mo[j].offset + mo[j].method.getName().length()) {
                          subset = true;
                          break;
                      }
                  }
                  if (subset) {
                      pos += methods[i].getName().length();
                  } else {
                      break;
                  }
              }
              mo[i] = new MethodOffset(methods[i],pos);
          }
          java.util.Arrays.sort(mo);
          for (int i=0; i<mo.length; ++i) {
              methods[i]=mo[i].method;
          }
      } catch (Exception ex) {
          ex.printStackTrace();
      }

      return methods;
  }
  
  private static class MethodOffset implements Comparable<MethodOffset> {
    MethodOffset(Method _method, int _offset) {
        method=_method;
        offset=_offset;
    }

    @Override
    public int compareTo(MethodOffset target) {
        return offset-target.offset;
    }

    Method method;
    int offset;
  }

  static class ByLength implements Comparator<Method> {

    @Override
    public int compare(Method a, Method b) {
        return b.getName().length()-a.getName().length();
    }
  }
}