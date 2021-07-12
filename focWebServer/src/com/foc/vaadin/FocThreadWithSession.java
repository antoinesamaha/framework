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
package com.foc.vaadin;

import java.lang.reflect.Constructor;

import com.foc.Globals;
import com.foc.admin.FocUser;
import com.foc.web.server.FocWebServer;
import com.foc.web.server.session.FocWebSession;

public class FocThreadWithSession extends Thread {
	
	public static final String BATCH_USER = "BATCH LAUNCHER";
	private long         initiallSleep = 120000;
	private String       classNameFocWebApplication = null;
	private FocWebServer webServer = null;
	
	private FocWebApplication webApplication = null; 
	private FocWebSession     webSession     = null;
	
	public FocThreadWithSession(FocWebApplication initialWebApplication, FocWebServer webServer) {
		super();
		
		this.webServer = webServer;
		
		if(initialWebApplication != null) {
			classNameFocWebApplication = initialWebApplication.getClass().getName();
		}
	}
	
	public FocThreadWithSession(String classNameFocWebApplication, FocWebServer webServer) {
		super();
		
		this.webServer = webServer;
		this.classNameFocWebApplication = classNameFocWebApplication;
	}
	
	public void dispose() {
		closeSession();
		webServer = null;
	}
	
	public void run() {
		try{
			if(initiallSleep > 0) sleep(initiallSleep);
			while(!webServer.isReady()) {
				sleep(10000);
			}
			
			if(!initSession()) {
				main();
			}
		}catch (InterruptedException e){
			Globals.logException(e);
		}
	}

	public void main() {
		//Implement here your core execution
	}
	
	public boolean initSession() {
		boolean error = false;
		
		if(webServer != null) {
			FocUser batchUser = FocUser.findUser(BATCH_USER);
			if(batchUser != null) {
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
			
				webSession.setFocUser(batchUser);
			} else {
				error = true;
				Globals.logString(" ERROR : Could not find batch user:"+BATCH_USER);
			}
		}
		return error;
	}
	
	public void closeSession() {
		if (webApplication != null) {
			if(webServer != null) webServer.removeApplication(webApplication);
			webApplication.dispose();
			webApplication = null;
		}
		if(webSession != null) {
			webSession.dispose();
			webSession = null;
		}
	}

	public void setInitiallSleep(long initiallSleep) {
		this.initiallSleep = initiallSleep;
	}

	public String getClassNameFocWebApplication() {
		return classNameFocWebApplication;
	}

	public FocWebServer getWebServer() {
		return webServer;
	}
}
