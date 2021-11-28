package com.foc.web.microservice.entity;

import java.util.ArrayList;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.foc.Globals;
import com.foc.admin.FocExternalAuthConfig;
import com.foc.admin.UserSession;
import com.foc.util.Utils;
import com.foc.web.microservice.FocMicroServlet.SessionAndApplication;

public class AuthTokenHandler_FocDBConfigurable implements IAuthTokenHandler {

	public ArrayList<Algorithm> keys = null;
	
	public AuthTokenHandler_FocDBConfigurable() {
	}
	
	public boolean rejectSQL(String sql) {
		String s = sql != null ? sql.toUpperCase() : "";
		boolean error = s.contains("DROP") || s.contains("DELETE") || s.contains("ALTER") || s.contains("CREATE");
		return error;
	}
	
	private synchronized void reloadKeys() {
		Globals.logString("Auth: Reloading the JWT Keys");
		
		FocExternalAuthConfig config = FocExternalAuthConfig.getInstance();
		if (config != null) {
			String sql = config.getSQLSelectKeys();
			boolean error = rejectSQL(sql);

			if (!error) {
				ArrayList<String[]> securityKeyList = Globals.getApp().getDataSource().command_SelectRequest(new StringBuffer(sql), 1);
				
				if(securityKeyList != null) {
					keys = new ArrayList<Algorithm>();
					Globals.logString(" Found "+securityKeyList.size()+" keys");
					for(int i=0; i<securityKeyList.size(); i++) {
						String[] strKeys = securityKeyList.get(i);
						Algorithm algorithm = Algorithm.HMAC256(strKeys[0]);
						keys.add(algorithm);
					}
				}
			} else {
				Globals.logError("Reload Keys: SQL contains forbidden keywords");
			}
		} else {
			Globals.logError("Reload Keys: Could not find the External Auth Config record");
		}
	}

	private String validateToken_WithSingleKey(Algorithm algorithm, String token) {
		String subject = null;
		try{
			Globals.logError("Validating token");
			FocExternalAuthConfig config = FocExternalAuthConfig.getInstance();
			if (config != null) {
				String issuer = config.getJWTIssuer();
				JWTVerifier verifier = JWT.require(algorithm).withIssuer(issuer).build(); // instance
				DecodedJWT jwt = verifier.verify(token);
				subject = jwt.getSubject();
			} else {
				Globals.logError("Validating Token: Could not find the External Auth Config record");	
			}
		}catch (JWTVerificationException exception){
			Globals.logString(exception.getMessage());
			subject = null;
		}
		return subject;
	}
	
	private String validateToken_WithAllKeys(String token) {
		Globals.logString(" AuthTokenHAndler_1 validating token "+token);
		String subject = null;
		if (keys != null) {
			for(int i=0; i<keys.size() && subject == null; i++) {
				subject = validateToken_WithSingleKey(keys.get(i), token);
			}
		}
		Globals.logString(" AuthTokenHAndler_1 when validating token subject = " + (subject != null ? subject : " null "));
		return subject;
	}
	
	public String validateToken(String token) {
		String subject = validateToken_WithAllKeys(token);
	
		if (subject == null) {
			reloadKeys();
			subject = validateToken_WithAllKeys(token);
		}
		return subject;
	}	
	
	@Override
	public AuthTokenHandlerResult decodeToken(SessionAndApplication session, String token) {
		AuthTokenHandlerResult result = null;
		
		String username = null; 
				
		FocExternalAuthConfig config = FocExternalAuthConfig.getInstance();
		if (config == null) {
			username = "NO_AUTH_USER";
		} else {
			username = validateToken(token);	
		}
		
		if(username != null){
			result = new AuthTokenHandlerResult();
			result.setUsername(username);
			
			if (session != null && session.getWebSession() != null) {
				UserSession userSession = session.getWebSession().getUserSession();
				if (userSession != null) {
					userSession.putSessionParameter("username", username);
					userSession.putSessionParameter("provider", "auth");
				}
			}
		} else {
			Globals.logString(" = Token Subject (Username) null!");
		}
		
		return result;
	}

	@Override
	public AuthRolesAndPermissions loadRolesAndPermissions(SessionAndApplication session) {
		AuthRolesAndPermissions rolesPermissions = null;
		if (session != null && session.getWebSession() != null) {
			UserSession userSession = session.getWebSession().getUserSession();
			
			FocExternalAuthConfig config = FocExternalAuthConfig.getInstance();
			if (userSession != null && config != null) {
				rolesPermissions = (AuthRolesAndPermissions) userSession.getSessionParameter("roles_and_permissions");
				if (rolesPermissions == null) {
					String username = (String) userSession.getSessionParameter("username");
					String sql      = config.getSQLSelectRolePermission(); 
			
					if (!Utils.paramSanityCheck(username)) {
						if(!rejectSQL(sql)) {
							sql =	sql.replace("$F{username}", username);
							if(!rejectSQL(sql)) {
								ArrayList<String[]> rolesPermissionsList = Globals.getApp().getDataSource().command_SelectRequest(new StringBuffer(sql), 2);
								if(rolesPermissionsList != null) {
									rolesPermissions = new AuthRolesAndPermissions();							
									for(int i=0; i<rolesPermissionsList.size(); i++) {
										String[] row = rolesPermissionsList.get(i);
										if (row != null) {
											rolesPermissions.push(row[0], row[1]);
										}
									}
								}
							} else {
								Globals.logError("loadRolesAndPermissions REJECTED because UNSECURED sql:"+sql);
							}
						}else {
							Globals.logError("loadRolesAndPermissions REJECTED because UNSECURED sql:"+sql);
						}
					} else {
						Globals.logError("loadRolesAndPermissions REJECTED because UNSECURED username:"+username);
					}					
				}
			}
		}
		return rolesPermissions;
	}
}
