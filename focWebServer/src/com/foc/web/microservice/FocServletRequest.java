package com.foc.web.microservice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.foc.web.microservice.FocMicroServlet.SessionAndApplication;

public class FocServletRequest {
	private SessionAndApplication sessionAndApp = null;
	private HttpServletRequest  request  = null;
	private HttpServletResponse response = null;
	
	public FocServletRequest(SessionAndApplication sessionAndApp, HttpServletRequest request, HttpServletResponse response) {
		this.sessionAndApp = sessionAndApp;
		this.request       = request;
		this.response      = response;
	}
	
	public void dispose() {
		sessionAndApp = null;
		request  = null;
		response = null;
	}

	public SessionAndApplication getSessionAndApp() {
		return sessionAndApp;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}
}
