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
	
	public FocThreadWithSession(FocWebApplication initialWebApplication, FocWebServer webServer) {
		super();
		
		this.webServer = webServer;
		
		if(initialWebApplication != null) {
			classNameFocWebApplication = initialWebApplication.getClass().getName();
		}
	}
	
	public void run() {
		try{
			sleep(initiallSleep);
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
			FocWebApplication webApplication = null; 
			FocWebSession     webSession     = null;

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

	public void setInitiallSleep(long initiallSleep) {
		this.initiallSleep = initiallSleep;
	}
}
