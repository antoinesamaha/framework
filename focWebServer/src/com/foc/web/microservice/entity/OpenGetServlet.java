package com.foc.web.microservice.entity;

import com.foc.desc.FocObject;
import com.foc.web.microservice.FocServletRequest;

public class OpenGetServlet extends FocJoinEntityServlet<FocObject, FocObject> {
	
	@Override
	public int getAuthenticationMethod() {
		return AUTH_NONE;
	}

	@Override
	public boolean allowDelete(FocServletRequest focRequest) {
		return false;
	}

	@Override
	public boolean allowPost(FocServletRequest focRequest) {
		return false;
	}
	
	public void doGet_Core(FocServletRequest focRequest) throws Exception {
		
	}
	
}


