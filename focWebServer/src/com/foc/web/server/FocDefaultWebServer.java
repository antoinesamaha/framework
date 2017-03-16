package com.foc.web.server;

import java.util.Iterator;

import com.foc.FocMainClass;
import com.foc.Globals;
import com.foc.desc.FocModule;
import com.foc.vaadin.FocWebModule;

@SuppressWarnings("serial")
public abstract class FocDefaultWebServer extends FocWebServer {

	public abstract void modules();
	
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
}
