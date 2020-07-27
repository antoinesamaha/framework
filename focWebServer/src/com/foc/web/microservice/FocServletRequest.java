package com.foc.web.microservice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.foc.list.FocList;
import com.foc.web.microservice.FocMicroServlet.SessionAndApplication;

public class FocServletRequest {
	private SessionAndApplication sessionAndApp = null;
	private HttpServletRequest  request  = null;
	private HttpServletResponse response = null;
	
	private FocList list = null;
	private long    ref  = 0;
	
	public FocServletRequest(SessionAndApplication sessionAndApp, HttpServletRequest request, HttpServletResponse response) {
		this.sessionAndApp = sessionAndApp;
		this.request       = request;
		this.response      = response;
	}
	
	public void dispose() {
		sessionAndApp = null;
		request  = null;
		response = null;
		list     = null;
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
	
	public FocList getList() {
		return list;
	}

	public void setList(FocList list) {
		this.list = list;
	}

	public long getRef() {
		return ref;
	}

	public void setRef(long ref) {
		this.ref = ref;
	}
}
