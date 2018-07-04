package com.foc.vaadin;

import java.lang.reflect.Constructor;

import org.apache.tools.ant.taskdefs.Sleep;

import com.foc.Globals;
import com.foc.admin.FocUser;
import com.foc.web.server.FocWebServer;
import com.foc.web.server.session.FocWebSession;

public class FocThreadWithSession extends Thread {
	
	public static final String BATCH_USER = "BATCH LAUNCHER";
	
	private String       classNameFocWebApplication = null;
	private FocWebServer webServer = null;
	
	private Runnable     runnable = null;
	
	public FocThreadWithSession(FocWebApplication initialWebApplication, FocWebServer webServer, Runnable runnable) {
		super();
		
		this.runnable = runnable;
		this.webServer = webServer;
		
		if(initialWebApplication != null) {
			classNameFocWebApplication = initialWebApplication.getClass().getName();
		}
	}
	
	public void run() {
		try{
			sleep(120000);
			while(!webServer.isReady()) {
				sleep(10000);
			}
		}catch (InterruptedException e){
			Globals.logException(e);
		}
		
		initSession();
		
		if(runnable != null) runnable.run();
	}

	public void initSession() {
		if(webServer != null) {
			
			FocWebApplication webApplication = null; 
			FocWebSession     webSession     = null;
			
			if (webApplication == null) {
				try {
					Class cls = Class.forName(classNameFocWebApplication);
					Class[] param = new Class[0];
					Constructor constr = cls.getConstructor(param);
					Object[] argsNew = new Object[0];

					webApplication = (FocWebApplication) constr.newInstance(argsNew);
					FocWebApplication.setInstanceForThread(webApplication);
					webApplication.setData(webServer);
				} catch (Exception e) {
					Globals.logException(e);
				}

				webSession = new FocWebSession(null);//null beccause there is not httpsession
				
				webApplication.setFocWebSession(webSession);
				webServer.addApplication(webApplication);
				
				FocUser batchUser = FocUser.findUser(BATCH_USER);
				if(batchUser != null) {
					webSession.setFocUser(batchUser);
				} else {
					Globals.logString(" ERROR : Could not find batch user:"+BATCH_USER);
				}
			}
		}
	}
}
