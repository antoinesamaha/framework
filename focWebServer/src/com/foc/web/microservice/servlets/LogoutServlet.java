package com.foc.web.microservice.servlets;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.foc.Globals;
import com.foc.web.microservice.entity.FocSimpleMicroServlet;

public class LogoutServlet extends FocSimpleMicroServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SessionAndApplication sessionAndApp = pushSession(request, response);
		
		String userJson = "";
		
		if (sessionAndApp != null) {
			Globals.logString(" => POST Begin Logout");
//			DeviceInformation deviceInformation = DeviceInformation.fromRequest(request);
//
//			WSUserActivity.logLogout(deviceInformation);
			
			setCORS(response);
			response.getWriter().println(userJson);
			Globals.logString(" <= POST End Logout");
			sessionAndApp.logout();
			
		} else {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			setCORS(response);
			userJson = "{\"message\": \"Unauthorised\"}";
			response.getWriter().println(userJson);
			
		}
		
	}

	@Override
	protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Globals.logString(" => POST Begin UserProfileServlet /profile");
		super.doOptions(request, response);
		// The following are CORS headers. Max age informs the
		// browser to keep the results of this call for 1 day.
		setCORS(response);
		Globals.logString(" <= POST End UserProfileServlet /profile");
	}

}
