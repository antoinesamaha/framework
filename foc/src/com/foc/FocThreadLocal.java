package com.foc;

import java.io.Serializable;

import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class FocThreadLocal implements Serializable{
	private static ThreadLocal<Object> threadWebServer      = new ThreadLocal<Object>();
	private static ThreadLocal<Object> threadWebApplication = new ThreadLocal<Object>();
	
	public static void setWebServer(Object webServer){
		if(webServer == null){
			Globals.logString("THREAD SET SERVER ThreadID="+Thread.currentThread().getId()+" TO NULL");
		}else{
			Globals.logString("THREAD SET SERVER ThreadID="+Thread.currentThread().getId()+" ");
		}
		if(UI.getCurrent() != null){
			UI.getCurrent().setData(webServer);
		}
		threadWebServer.set(webServer);
	}

	public static Object getWebServer(){
		Object obj = null; 
		if(UI.getCurrent() != null){
			obj = UI.getCurrent().getData();
		}
		if(obj == null){
			obj = threadWebServer.get();
		}
//		if(obj == null){
//			Globals.logString("THREAD GETTING NULL SERVER !!! ThreadID="+Thread.currentThread().getId());
//		}
		return obj;
	}

	public static void unsetThreadLocal(){
		Globals.logString("THREAD SET SERVER ThreadID="+Thread.currentThread().getId()+" TO NULL (Remove)");
		threadWebServer.remove();
		threadWebApplication.remove();		
	}
	
	public static void setWebApplication(Object webApplication){
		UI.setCurrent((UI) webApplication);
		threadWebApplication.set(webApplication);
	}

	public static Object getWebApplication(){
		Object obj = UI.getCurrent(); 
		if(obj == null){
			Globals.logString("DEBUG_SESSION_NOT_VALID THIS IS NULL FocThreadLocal->UI.getCurrent()");
			obj = threadWebApplication.get();
			if(obj == null) Globals.logString("DEBUG_SESSION_NOT_VALID THIS IS NULL TOOO FocThreadLocal->threadWebApplication.get()");
		}
		return obj;
	}
}