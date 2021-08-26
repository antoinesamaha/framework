package com.foc.web.microservice.entity;

import com.foc.web.microservice.FocMicroServlet.SessionAndApplication;

public interface IAuthTokenHandler {
	public AuthTokenHandlerResult decodeToken(SessionAndApplication session, String token);

	public class AuthTokenHandlerResult {
		private String username     = null; 
		private String errorMessage = null;
		
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getErrorMessage() {
			return errorMessage;
		}
		public void setErrorMessage(String errorMessage) {
			this.errorMessage = errorMessage;
		} 
	}

}
