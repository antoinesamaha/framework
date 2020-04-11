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
package com.foc.web.microservice;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.foc.Globals;
import com.foc.SrvConst_ServerSide;
import com.foc.admin.FocLoginAccess;
import com.foc.util.Encryptor;
import com.foc.vaadin.FocWebApplication;
import com.foc.web.server.FocWebServer;
import com.foc.web.server.session.FocWebSession;

@SuppressWarnings("serial")
public abstract class FocMicroServlet extends HttpServlet implements SrvConst_ServerSide {
	
	protected abstract String getUIClassName();
	
	protected String getParameterString(HttpServletRequest request, String paramName) {
		String value = null;
		Map<String, String[]> map = request.getParameterMap();
		if(map != null) {
			String[] valueArray = map.get(paramName);
			value = valueArray != null && valueArray.length > 0 ? valueArray[0] : null;
		}
		return value;
	}
	
	protected String getJsonString(JSONObject jsonObj, String key) {
		String value = null;
		try {
			value = jsonObj != null ? jsonObj.getString(key) : null;
		}catch(Exception e) {
			Globals.logException(e);
		}
		return value;
	}
	
	protected int getJsonInteger(JSONObject jsonObj, String key, int defaultValue) {
		int value = defaultValue;
		try {
			value = jsonObj != null ? jsonObj.getInt(key) : null;
		}catch(Exception e) {
			Globals.logException(e);
		}
		return value;
	}
	
	protected long getJsonLong(JSONObject jsonObj, String key, long defaultValue) {
		long value = defaultValue;
		try {
			value = jsonObj != null ? jsonObj.getLong(key) : null;
		}catch(Exception e) {
			Globals.logException(e);
		}
		return value;
	}
	
	protected boolean getJsonBoolean(JSONObject jsonObj, String key, boolean defaultValue) {
		boolean value = defaultValue;
		try {
			value = jsonObj != null ? jsonObj.getBoolean(key) : null;
		}catch(Exception e) {
			Globals.logException(e);
		}
		return value;
	}
	
	protected StringBuffer getRequestAsStringBuffer(HttpServletRequest request) {
		StringBuffer sb = new StringBuffer();
		String s = null;
		try {
			while ((s = request.getReader().readLine()) != null) {
				sb.append(s);
			}
		}catch(Exception e){
			Globals.logException(e);
		}
		return sb;
	}

	protected long getParameterLong(HttpServletRequest request, String paramName, long defaultValue) {
		long value = defaultValue;
		Map<String, String[]> map = request.getParameterMap();
		if(map != null) {
			String[] valueArray = map.get(paramName);
			String valueStr = valueArray != null && valueArray.length > 0 ? valueArray[0] : null;
			if(valueStr != null) {
				try {
					value = Long.valueOf(valueStr);
				} catch(Exception e) {
					
				}
			}

		}
		return value;
	}

	protected int getParameterInteger(HttpServletRequest request, String paramName, int defaultValue) {
		int value = defaultValue;
		Map<String, String[]> map = request.getParameterMap();
		if(map != null) {
			String[] valueArray = map.get(paramName);
			String valueStr = valueArray != null && valueArray.length > 0 ? valueArray[0] : null;
			if(valueStr != null) {
				try {
					value = Integer.valueOf(valueStr);
				} catch(Exception e) {
					
				}
			}

		}
		return value;
	}

	protected boolean getParameterBoolean(HttpServletRequest request, String paramName, boolean defaultValue) {
		boolean value = defaultValue;
		Map<String, String[]> map = request.getParameterMap();
		if(map != null) {
			String[] valueArray = map.get(paramName);
			String valueStr = valueArray != null && valueArray.length > 0 ? valueArray[0] : null;
			if(valueStr != null) {
				try {
					value = Boolean.valueOf(valueStr);
				} catch(Exception e) {
					
				}
			}

		}
		return value;
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setHeader("Pragma", "No-cache");
		response.setDateHeader("Expires", 0);
		response.setHeader("Cache-Control", "no-cache");

		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<head><title>Everpro Servlet</title></head>");
		out.println("<body>");
		out.println("<h1>Everpro Servlet</h1>");
		out.println("<p> Please post a request to this servlet in order to get a valid response.");
		out.println("</body></html>");
		out.flush();
	}

	public SessionAndApplication pushSessionLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		return pushSessionInternal(request, response);
	}
	
	public synchronized SessionAndApplication pushSession(HttpServletRequest request, HttpServletResponse response) throws IOException {
		return pushSessionInternal(request, response);
	}
	
	public synchronized SessionAndApplication pushSessionInternal(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String requestSessionID = request.getSession().getId();
//		Globals.logString("SESSION_ID = " + requestSessionID);

		// Using the Basic authorization HTTP protocol creates an
		// IllegalArgumentException in Java. Known bug. Apparently fixed in JDK7
		// String auth = request.getHeader("Authorization");

		// FocWebServer webServer =
		// FocWebServer.connect(request.getSession().getServletContext());
		// FocThreadLocal.setWebServer(webServer);//This line is important when the
		// WebServer already exists. In the second call or if the WebServer is
		// launched by the GUI

		FocWebApplication webApplication = null;
		FocWebSession webSession = null;
		int status = com.foc.Application.LOGIN_WRONG;

		{
			webApplication = FocWebServer.findWebApplicationBySessionID(requestSessionID, request.getSession().getServletContext());
			if(webApplication == null){
				Globals.logString("FocMicorServlet is creating a new FocWebApplication(UI)");
				try{
					Class cls = Class.forName(getUIClassName());
					Class[] param = new Class[0];
					Constructor constr = cls.getConstructor(param);
					Object[] argsNew = new Object[0];

					webApplication = (FocWebApplication) constr.newInstance(argsNew);
					FocWebApplication.setInstanceForThread(webApplication);
					webApplication.initialize(null, request.getServletContext(), request.getSession(), true);
					webApplication.setData(FocWebServer.getInstance());
					FocWebServer.getInstance().setWebServicesOnly(true);
				}catch (Exception e){
					Globals.logException(e);
				}
			}

			if(webApplication != null){
				webApplication.setJustSessionID(requestSessionID);
				webSession = webApplication.getFocWebSession();
			}

			// ---------------------------------------------
			// If the FocWebSession is not found, create a new FocWebApplication and a
			// new FocWebSession.
			if(webSession == null){
				Globals.logString("EverproLinkServlet is creating a new FocWebSession and adding the FocWebApplication(UI) to the webServer");
				webApplication.setFocWebSession(request.getSession(), new FocWebSession(request.getSession()));
				FocWebServer.getInstance().addApplication(webApplication);
				webSession = FocWebServer.findWebSessionBySessionID(requestSessionID, FocWebServer.getInstance());
			}

			// ---------------------------------------------
			// If the FocWebSession has no user, try to log in by reading the user
			// name and password from the HTTP request header.
			if(webSession != null && webSession.getFocUser() == null){
				String username = request.getHeader("username");
				String password = request.getHeader("password");
				if(username == null){
					username = (String) request.getAttribute(HEADER_KEY_USERNAME);
				}
				if(password == null){
					password = (String) request.getAttribute(HEADER_KEY_PASSWORD);
				}

				if (username != null && password != null) {
					Globals.logString(username);
					Globals.logString(password);
					String encryptedPassword = Encryptor.encrypt_MD5(String.valueOf(password));
					FocLoginAccess loginAccess = new FocLoginAccess();
	
					status = loginAccess.checkUserPassword(username, encryptedPassword, false);
	
					if(status == com.foc.Application.LOGIN_VALID){
						// webSession = newApplication.getFocWebSession();
						webSession.setFocUser(loginAccess.getUser());
					}
					if(status == com.foc.Application.LOGIN_WRONG){
						Globals.logString("Error: Login credentials are incorrect.");
						// PrintWriter printWriter = response.getWriter();
						// printWriter.println("Error: Login credentials are incorrect.");
					}
				}
			}
		}

		SessionAndApplication session = new SessionAndApplication(webSession, webApplication, status);
		return session;
	}

	protected void setCORS(HttpServletResponse response) {
		if(response != null){
			response.setHeader("Content-Type", "application/json; charset=UTF-8");
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS");
			response.setHeader("Access-Control-Allow-Headers", "Content-Type, X-CSRF-Token, X-Requested-With, Accept, Accept-Version, Content-Length, Content-MD5, Date, X-Api-Version, X-File-Name, X-Pagination, Content-Disposition, showLoader, Authorization");
			response.setHeader("Access-Control-Expose-Headers", "Content-Type, X-CSRF-Token, X-Requested-With, Accept, Accept-Version, Content-Length, Content-MD5, Date, X-Api-Version, X-File-Name, X-Pagination, Content-Disposition, showLoader, Authorization");
			response.setHeader("Access-Control-Max-Age", "86400");
			response.setHeader("Allow", "GET, HEAD, POST, TRACE, OPTIONS");
		}
	}
	
	public class SessionAndApplication {
		private FocWebSession webSession = null;

		private FocWebApplication webApplication = null;

		private int status = com.foc.Application.LOGIN_WRONG;

		private HashMap<String, Object> userDataMap = null;
		
		public SessionAndApplication(FocWebSession webSession, FocWebApplication webApplication, int status) {
			this.webSession = webSession;
			this.webApplication = webApplication;
			this.status = status;
		}

		public void dispose() {
			webSession = null;
			webApplication = null;
		}
		
		public void logout() { 
			if(getWebApplication() != null){
				getWebApplication().logout(null);
			}
			FocWebServer.disconnect();
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
		
		public void putUserData(String key, Object value) {
			if (key != null && value != null) {
				if(userDataMap == null) {
					userDataMap = new HashMap<String, Object>();
				}
				userDataMap.put(key, value);
			}
		}
		
		public Object getUserData(String key) {
			Object value = null;
			if(userDataMap != null) {
				value = userDataMap.get(key);
			}
			return value;
		}
	}
}
