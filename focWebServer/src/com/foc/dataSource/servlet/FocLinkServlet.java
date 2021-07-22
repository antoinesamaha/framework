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
package com.foc.dataSource.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.foc.Globals;
import com.foc.SrvConst_ServerSide;
import com.foc.admin.FocLoginAccess;
import com.foc.vaadin.FocWebApplication;
import com.foc.web.server.FocWebServer;
import com.foc.web.server.session.FocWebSession;

@SuppressWarnings("serial")
public class FocLinkServlet extends HttpServlet implements SrvConst_ServerSide {

   public void doGet (HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
   {
      response.setContentType("text/html"); 
      response.setHeader("Pragma", "No-cache");
      response.setDateHeader("Expires", 0);
      response.setHeader("Cache-Control", "no-cache");
      
      PrintWriter out = response.getWriter(); 
      out.println("<html>");  
      out.println("<head><title>Everpro Servlet</title></head>");
      out.println("<body>");
      out.println("<h1>Everpro Servlet</h1>");
      out.println ("<p> Please post a request to this servlet in order to get a valid response.");
      out.println("</body></html>");    
      out.flush();

   }
   
	public SessionAndApplication doPost_Init(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String requestSessionID = request.getSession().getId();
		Globals.logString("SESSION_ID = " + requestSessionID);
		
		// Using the Basic authorization HTTP protocol creates an
		// IllegalArgumentException in Java. Known bug. Apparently fixed in JDK7
		// String auth = request.getHeader("Authorization");

		//FocWebServer webServer = FocWebServer.connect(request.getSession().getServletContext());
		// FocThreadLocal.setWebServer(webServer);//This line is important when the
		// WebServer already exists. In the second call or if the WebServer is
		// launched by the GUI

		FocWebApplication webApplication = null; 
		FocWebSession webSession = null;
		int status = com.foc.Application.LOGIN_WRONG;
		
		{
			webApplication = FocWebServer.findWebApplicationBySessionID(requestSessionID, request.getSession().getServletContext());
			if (webApplication == null) {
				Globals.logString("EverproLinkServlet is creating a new FocWebApplication(UI)");
				try {
					Class cls = Class.forName("siren.isf.fenix.main.FenixUI");
					Class[] param = new Class[0];
					Constructor constr = cls.getConstructor(param);
					Object[] argsNew = new Object[0];

					webApplication = (FocWebApplication) constr.newInstance(argsNew);
					FocWebApplication.setInstanceForThread(webApplication);
					webApplication.initialize(null, request.getServletContext(), request.getSession(), false);
					webApplication.setData(FocWebServer.getInstance());
				} catch (Exception e) {
					Globals.logException(e);
				}
			}
			
			if (webApplication != null) {
				webSession = webApplication.getFocWebSession();
			}

			// ---------------------------------------------
			// If the FocWebSession is not found, create a new FocWebApplication and a
			// new FocWebSession.
			if (webSession == null) {
				Globals.logString("EverproLinkServlet is creating a new FocWebSession and adding the FocWebApplication(UI) to the webServer");
				webApplication.setFocWebSession(request.getSession(), new FocWebSession(request.getSession()));
				FocWebServer.getInstance().addApplication(webApplication);
				webSession = FocWebServer.findWebSessionBySessionID(requestSessionID, FocWebServer.getInstance());
			}

			// ---------------------------------------------
			// If the FocWebSession has no user, try to log in by reading the user
			// name and password from the HTTP request header.
			if (webSession != null && webSession.getFocUser() == null) {
				String username = request.getHeader("username");
				String password = request.getHeader("password");
				if (username == null) {
					username = (String) request.getAttribute(HEADER_KEY_USERNAME);
				}
				if (password == null) {
					password = (String) request.getAttribute(HEADER_KEY_PASSWORD);
				}

				Globals.logString(username);
				Globals.logString(password);
				FocLoginAccess loginAccess = new FocLoginAccess(username, password);
				status = loginAccess.getLoginStatus();

				if (status == com.foc.Application.LOGIN_VALID) {
					// webSession = newApplication.getFocWebSession();
					webSession.setFocUser(loginAccess.getUser());
				}
				if (status == com.foc.Application.LOGIN_WRONG) {
					Globals.logString("Error: Login credentials are incorrect.");
//					PrintWriter printWriter = response.getWriter();
//					printWriter.println("Error: Login credentials are incorrect.");
				}
			}
		}
		
		SessionAndApplication session = new SessionAndApplication(webSession, webApplication, status);
		return session;
	}
   
  public class SessionAndApplication {
  	private FocWebSession     webSession     = null;
  	private FocWebApplication webApplication = null;
  	private int status = com.foc.Application.LOGIN_WRONG; 
  	 
  	public SessionAndApplication(FocWebSession webSession, FocWebApplication webApplication, int status){
  		this.webSession     = webSession;
  		this.webApplication = webApplication;
  		this.status = status;
  	}

  	public void dispose(){
  		webSession = null;
  		webApplication = null;
  	}

		public FocWebSession getWebSession() {
			return webSession;
		}

		public void setWebSession(FocWebSession webSession) {
			this.webSession = webSession;
		}

		public FocWebApplication getWebApplication() {
			return webApplication;
		}

		public void setWebApplication(FocWebApplication webApplication) {
			this.webApplication = webApplication;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}
  }
  
  
	public SessionAndApplication doPost_Init2(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String requestSessionID = request.getSession().getId();
		Globals.logString("SESSION_ID = " + requestSessionID);
		
		// Using the Basic authorization HTTP protocol creates an
		// IllegalArgumentException in Java. Known bug. Apparently fixed in JDK7
		// String auth = request.getHeader("Authorization");

		FocWebServer webServer = FocWebServer.connect(request.getSession().getServletContext(), false);
		// FocThreadLocal.setWebServer(webServer);//This line is important when the
		// WebServer already exists. In the second call or if the WebServer is
		// launched by the GUI

		FocWebApplication webApplication = null; 
		FocWebSession webSession = null;
		int status = com.foc.Application.LOGIN_WRONG;
		
		if (webServer == null) {
			Globals.logString("Could not connect to the WebServer webServer = null");
		} else {
			webApplication = FocWebServer.findWebApplicationBySessionID(requestSessionID, webServer);
			if (webApplication == null) {
				Globals.logString("EverproLinkServlet is creating a new FocWebApplication(UI)");
				try {
					Class cls = Class.forName("b01.everpro.custom.application.CustomEverproWebApplication");
					Class[] param = new Class[0];
					Constructor constr = cls.getConstructor(param);
					Object[] argsNew = new Object[0];

					webApplication = (FocWebApplication) constr.newInstance(argsNew);
					FocWebApplication.setInstanceForThread(webApplication);
					webApplication.setData(webServer);
				} catch (Exception e) {
					Globals.logException(e);
				}
			}

			
			if (webApplication != null) {
				webSession = webApplication.getFocWebSession();
			}

			// ---------------------------------------------
			// If the FocWebSession is not found, create a new FocWebApplication and a
			// new FocWebSession.
			if (webSession == null) {
				Globals.logString("EverproLinkServlet is creating a new FocWebSession and adding the FocWebApplication(UI) to the webServer");
				webApplication.setFocWebSession(request.getSession(), new FocWebSession(request.getSession()));
				webServer.addApplication(webApplication);
				webSession = FocWebServer.findWebSessionBySessionID(requestSessionID, webServer);
			}

			// ---------------------------------------------
			// If the FocWebSession has no user, try to log in by reading the user
			// name and password from the HTTP request header.
			if (webSession != null && webSession.getFocUser() == null) {
				String username = request.getHeader("username");
				String password = request.getHeader("password");
				if (username == null) {
					username = (String) request.getAttribute(HEADER_KEY_USERNAME);
				}
				if (password == null) {
					password = (String) request.getAttribute(HEADER_KEY_PASSWORD);
				}

				Globals.logString(username);
				Globals.logString(password);
				FocLoginAccess loginAccess = new FocLoginAccess(username, password);
				status = loginAccess.getLoginStatus();

				if (status == com.foc.Application.LOGIN_VALID) {
					// webSession = newApplication.getFocWebSession();
					webSession.setFocUser(loginAccess.getUser());
				}
				if (status == com.foc.Application.LOGIN_WRONG) {
					Globals.logString("Error: Login credentials are incorrect.");
//					PrintWriter printWriter = response.getWriter();
//					printWriter.println("Error: Login credentials are incorrect.");
				}
			}
		}
		
		SessionAndApplication session = new SessionAndApplication(webSession, webApplication, status);
		return session;
	}

}

