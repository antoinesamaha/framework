package com.foc.web.microservice.loockups;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.foc.Globals;
import com.foc.util.Utils;
import com.foc.web.microservice.FocServletRequest;
import com.foc.web.microservice.simple.FocSimpleMicroServlet;

public class WSLookupServlet extends FocSimpleMicroServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request != null && request.getSession() != null) {
			Globals.logString("Session ID Started request"+request.getSession().getId());
		}
		SessionAndApplication sessionAndApp = pushSession(request, response);
		if(sessionAndApp != null){
			FocServletRequest focRequest = null;
			try {
				focRequest = newFocServletRequest(sessionAndApp, request, response);
				
				Globals.logString(" => GET Begin Lookups");
				if(allowGet(null)){
					logRequestHeaders(request);
					
					WSLookupFactory factory = WSLookupFactory.getInstance();
					
					if (factory != null) {
						String[] filters = request.getParameterValues("filter");
						if (filters != null) {
							boolean first = true;
							StringBuffer json = new StringBuffer("{");
							for(int i=0; i<filters.length; i++) {
								String filter = filters[i];
								if(!Utils.isStringEmpty(filter)) {
									WSSingleLookup lookup = factory.getLookup(filter);
									if(lookup != null) {
										if(!first) json.append(",");
										first = false;
										json.append("\"");
										json.append(lookup.getKey());
										json.append("\":");
										json.append(lookup.getJson());
									}
								}
							}
							json.append("}"); 
							
							response.setStatus(HttpServletResponse.SC_OK);
							setCORS(response);
							response.getWriter().println(json.toString());							
						} else {
							response.setStatus(HttpServletResponse.SC_OK);
							setCORS(response);
							response.getWriter().println("{}");
						}
					} else {
						response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
						setCORS(response);
						String responseBody = "{\"message\": \"lookup factory null\"}";
						response.getWriter().println(responseBody);
					}
				}
				Globals.logString(" <= GET End Lookups "+response.getStatus());
			} catch (Exception e) {
				Globals.logException(e);
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				setCORS(response);
				String responseBody = "{\"message\": \""+e.getMessage()+"\"}";
				response.getWriter().println(responseBody);
			} finally {
				focRequest.dispose();
				sessionAndApp.logout();				
			}

		}else{
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			setCORS(response);
			String responseBody = "{\"message\": \"Unauthorised\"}";
			response.getWriter().println(responseBody);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SessionAndApplication sessionAndApp = pushSessionInternal(request, response);
		Globals.logString(" => POST Begin ReloadListServlet /lookups");
		String userJson   = "";
		String loggedJson = "";
		if(sessionAndApp != null){
			try{
				String[] list = request.getParameterValues("filter");
				if(list.length > 0  && WSLookupFactory.getInstance() != null) {
					if(WSLookupFactory.getInstance() != null) {
						WSLookupFactory factory = WSLookupFactory.getInstance();
						boolean error = false;
						boolean oneSuccessfullReload = false;
						for(int i=0; i < list.length; i++) {
							String lookupName = list[i];
							WSSingleLookup lookup = factory.getLookup(lookupName);
							if(lookup != null){
								lookup.refresh();
								oneSuccessfullReload = true;
								lookup.refresh();
							} else {
								error = true;
								Globals.logString("  = ERROR: Couldn't reload list " + lookupName);
							}
						}
						if(oneSuccessfullReload) {
							userJson = "{\"message\": \"The lists were successfully reloaded";
							if(error) userJson += ", but some errors occured";
							userJson += "\"}";
							loggedJson = userJson;
//							response.addHeader("message", "The lists were successfully reloaded");
							response.setStatus(HttpServletResponse.SC_OK);
						} else {
							userJson = "{\"code\": \"invalid_data\",\"message\": \"List names incorrect\"}";
							loggedJson = userJson;
//								response.addHeader("message", "List names incorrect");
							response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
						}
					}
				} else {
					userJson = "{\"code\": \"invalid_data\",\"message\": \"List names not filled\"}";
					loggedJson = userJson;
//						response.addHeader("message", "List names not filled");
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				}
				setCORS(response);
				Globals.logString("  = Returned: "+loggedJson);
				response.getWriter().println(userJson);
			}catch (Exception e){
				Globals.logException(e);
				setCORS(response);
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().println(e.getMessage());
			}
		} else {
			Globals.logString("  = ERROR: ReloadListServlet Invalid Session");
		}
		Globals.logString(" <= POST End  ReloadListServlet /lookups");
		if(sessionAndApp != null){
			sessionAndApp.logout();
		}
	}
}
