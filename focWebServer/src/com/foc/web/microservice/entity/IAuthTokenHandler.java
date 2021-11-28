package com.foc.web.microservice.entity;

import java.util.HashSet;

import com.foc.web.microservice.FocMicroServlet.SessionAndApplication;

public interface IAuthTokenHandler {
	public AuthTokenHandlerResult decodeToken(SessionAndApplication session, String token);
	public AuthRolesAndPermissions loadRolesAndPermissions(SessionAndApplication session);

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

	public class AuthRolesAndPermissions {
		
		private HashSet<String> roles = new HashSet<String>();
		private HashSet<String> permissions = new HashSet<String>();
		
		public void push(String role, String permission) {
			roles.add(role);
			permissions.add(permission);
		}

		public boolean permissionExist(String permission) {
			return permissions != null && permissions.contains(permission);
		}
	}
}
