package com.foc.web.unitTesting;

import java.util.ArrayList;

import com.foc.access.FocLogger;

public class FocUnitTest implements ITestCase {

  private String                           name  = null;
  private FocUnitTestingSuite              suite = null;
  private ArrayList<FocUnitTestingCommand> arrayCommands = null;
  private FocUnitXMLAttributes             callerArguments = null;
  
  public FocUnitTest() {
    
  }
  
  public void dispose(int mode){
    name = null;
    suite = null;
    if(arrayCommands != null){
	    arrayCommands.clear();
	    arrayCommands = null;
    }
    callerArguments = null;
  }
  
  public FocUnitTest(String name, FocUnitTestingSuite suite, ArrayList<FocUnitTestingCommand> arrayCommands) {
    setName(name);
    setSuite(suite);
    this.arrayCommands = arrayCommands;
  }
  
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public FocUnitTestingSuite getSuite() {
    return suite;
  }
  
  public void setSuite(FocUnitTestingSuite suite) {
    this.suite = suite;
  }
  
  private ArrayList<FocUnitTestingCommand> getArrayCommands() {
    if(arrayCommands == null) {
      arrayCommands = new ArrayList<FocUnitTestingCommand>();
    }
    return arrayCommands;
  }
 
  public void addCommand(FocUnitTestingCommand command) {
    getArrayCommands().add(command);
  }
  
  @Override
  public void init() {
    //FocLogger.getInstance().addInfo("Initializing: Test " + this.getName() + " in suite " + this.getSuite().getName() + ".");
  }

  @Override
  public void runTest() {
  	String testStartMessage = "TEST: " + getSuite().getName()+" / "+getName();
  	//String testStartMessage = "TEST: ";
  	if(callerArguments != null){
  		String[] excluds = {FXMLUnit.ATT_CALL_SUIT, FXMLUnit.ATT_CALL_TEST, FXMLUnit.ATT_NAME};
  		testStartMessage += " " + callerArguments.getString(excluds);
  	}
    FocLogger.getInstance().openNode(testStartMessage);
      
    for (int i=0;i<getArrayCommands().size();i++) {
      FocUnitTestingCommand command = getArrayCommands().get(i);
      command.execute();
    }
    
    FocLogger.getInstance().closeNode();
  }

  public FocUnitXMLAttributes getCallerArguments() {
    return callerArguments;
  }

  public void setCallerArguments(FocUnitXMLAttributes callerArguments) {
    this.callerArguments = callerArguments;
  }
     
}
