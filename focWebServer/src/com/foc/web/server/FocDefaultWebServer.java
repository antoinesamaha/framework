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
package com.foc.web.server;

import java.util.Iterator;

import com.foc.FocMainClass;
import com.foc.Globals;
import com.foc.desc.FocModule;
import com.foc.vaadin.FocWebModule;

@SuppressWarnings("serial")
public abstract class FocDefaultWebServer extends FocWebServer {
	
	protected FocMainClass newMainClass(){
  	String[] args = { "/IS_SERVER:1", "/nol:1"};
  	
  	FocDefaultMainClass main = new FocDefaultMainClass(this, args);
		main.init2(args);
		main.init3(args);
  	
  	return main;
	}
	
  public void declareModules(){
    super.declareModules();
    
    Iterator<FocModule> modules = Globals.getApp().modules_Iterator();
    while(modules != null && modules.hasNext()){
    	FocModule module = modules.next();
    	if(module instanceof FocWebModule){
    		FocWebServer.getInstance().modules_Add((FocWebModule) module);
    	}
    }
  }
  
  public void modules() {
  };
}
