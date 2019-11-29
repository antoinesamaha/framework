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
package com.foc.web.unitTesting;

import java.util.ArrayList;

import com.foc.Globals;
import com.foc.access.FocLogLine;
import com.foc.access.FocLogger;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;

public class FocUnitTest extends FocUnitTestingCommand implements ITestCase {

  private String                           name  = null;
  private FocUnitTestingSuite              suite = null;
  private ArrayList<FocUnitTestingCommand> arrayCommands = null;
  private FocUnitXMLAttributes             callerArguments = null;
  
  public FocUnitTest() {
    this(null, null);
  }

  public FocUnitTest(String name, FocUnitTestingSuite suite) {
  	this(name, suite, null);
  }

  public FocUnitTest(String name, FocUnitTestingSuite suite, ArrayList<FocUnitTestingCommand> arrayCommands) {
  	super(null, null, new FocXMLAttributes());
  	//Init Command
  	setUnitTest(this);
  	setMethodName(name);
  	
    setName(name);
    setSuite(suite);
    this.arrayCommands = arrayCommands;
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

  protected void exec() throws Exception {
    for (int i=0;i<getArrayCommands().size() && !FocUnitDictionary.getInstance().isPause(); i++) {
      FocUnitTestingCommand command = getArrayCommands().get(i);
      FocUnitDictionary.getInstance().stackSetCommand(i);
      command.execute();
    }
  }
  
  @Override
  public void runTest() {
  	if(!FocUnitDictionary.getInstance().isExitTesting()) {
	  	String testStartMessage = "TEST: " + getSuite().getName()+" / "+getName();
	  	//String testStartMessage = "TEST: ";
	  	if(callerArguments != null){
	  		String[] excluds = {FXMLUnit.ATT_CALL_SUIT, FXMLUnit.ATT_CALL_TEST, FXMLUnit.ATT_NAME};
	  		testStartMessage += " " + callerArguments.getString(excluds);
	  	}
	    FocLogLine logLine = FocLogger.getInstance().openNode(testStartMessage);
	    
	    FocUnitDictionary.getInstance().stackPush(FocUnitTest.this, 0);
	    
	    try{
				exec();
			}catch (Exception e){
				FocUnitDictionary.getInstance().setExitTesting(true);
				Globals.logException(e);
			}finally {
				if(!FocUnitDictionary.getInstance().isPause()){
					FocUnitDictionary.getInstance().stackPop();
					FocLogger.getInstance().closeNodeUntil(logLine);
				}
			}
  	}    
  }

  public FocUnitXMLAttributes getCallerArguments() {
    return callerArguments;
  }

  public void setCallerArguments(FocUnitXMLAttributes callerArguments) {
    this.callerArguments = callerArguments;
  }
     
	public void loginAs_IfMemoryCheckActive(String name, String password) throws Exception {
		if(getSuite().getDictionary().isMemoryCheckActive()) {
			loginAs(name, password);
		}
	}
	
	public void loginAs(String name, String password) throws Exception {
		component_SetValue("NAME", name, false);
		component_SetValue("PASSWORD", password, false);
		
		FocXMLLayout navigationLayout = getCurrentCentralPanel();
		FocXMLGuiComponent component = findComponent(navigationLayout, "LOGIN", false);
		if(component != null) {
			button_Click("LOGIN");
		} else {
			button_Click("login");
		}
	}
	
	public void loginAs_IfMemoryCheckActive(String name) throws Exception {
		if(getSuite().getDictionary().isMemoryCheckActive()) {
			loginAs(name);
		}
	}
	
	public void loginAs(String name) throws Exception {
		loginAs(name, "");
	}

	protected void memoryCheck() throws Exception {
		
	}
	
	public void logoutWithMemoryCheck() throws Exception {
		getDictionary().incrementTestIndexes();
		getMainWindow().logout();
		memoryCheck();
		FocUnitDictionary.getInstance().setExitTesting(true);
	}
	
	public void changeSite(String station, String title) throws Exception {
		FocLogger.getInstance().openNode("Change Site and Writer : "+station+" - "+title);
		{
			popupUserAccount();
			component_SetValue("CURRENT_SITE", station, false);
			component_SetValue("CURRENT_TITLE", title, false);
			button_ClickApply();
		}
		FocLogger.getInstance().closeNode();
	}

}
