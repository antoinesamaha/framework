package com.foc.web.microservice.entity;

import java.util.ArrayList;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.foc.ConfigInfo;
import com.foc.Globals;
import com.foc.admin.ActiveUserList;
import com.foc.admin.FocUser;
import com.foc.admin.FocUserDesc;
import com.foc.admin.UserSession;
import com.foc.list.FocList;
import com.foc.web.microservice.FocMicroServlet.SessionAndApplication;

public class AuthTokenHandler_1 implements IAuthTokenHandler {

	public ArrayList<Algorithm> keys = null;
	public String issuer = null;
	public String authDBSchema   = null;
	public String authDBKeyField = null;
	
	public AuthTokenHandler_1() {
		issuer = ConfigInfo.getProperty("jwt.issuer");
		if(issuer == null) issuer = "siren";

		authDBSchema = ConfigInfo.getProperty("jwt.auth.schema");
		if(authDBSchema == null) {
			authDBSchema = "";
		} else {
			authDBSchema += ".";
		}
		
		authDBKeyField = ConfigInfo.getProperty("jwt.auth.keyfieldname");
		if(authDBKeyField == null) authDBKeyField = "key";
	}
		
	private synchronized void reloadKeys() {
		Globals.logString("Auth: Reloading the JWT Keys");
		StringBuffer request = new StringBuffer("select \""+authDBKeyField+"\" from "+authDBSchema+"\"XSharedSecret\"");
		ArrayList<String[]> securityKeyList = Globals.getApp().getDataSource().command_SelectRequest(request, 1);
		
		if(securityKeyList != null) {
			keys = new ArrayList<Algorithm>();
			for(int i=0; i<securityKeyList.size(); i++) {
				String[] strKeys = securityKeyList.get(i);
				Algorithm algorithm = Algorithm.HMAC256(strKeys[0]);
				keys.add(algorithm);
			}
		}
	}

	private String validateToken_WithSingleKey(Algorithm algorithm, String token) {
		String subject;
		try{
			JWTVerifier verifier = JWT.require(algorithm).withIssuer(issuer).build(); // instance
			DecodedJWT jwt = verifier.verify(token);
			subject = jwt.getSubject();
		}catch (JWTVerificationException exception){
			// Invalid signature/claims
			subject = null;
		}
		return subject;
	}
	
	private String validateToken_WithAllKeys(String token) {
		String subject = null;
		if (keys != null) {
			for(int i=0; i<keys.size() && subject == null; i++) {
				subject = validateToken_WithSingleKey(keys.get(i), token);
			}
		}
		
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
		
		String username = validateToken(token);
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
		}else{
			Globals.logString(" = Token Subject (Username) null!");
		}
		
		return result;
	}
	
}
