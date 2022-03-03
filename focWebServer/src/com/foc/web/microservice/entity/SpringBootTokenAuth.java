package com.foc.web.microservice.entity;

import java.util.ArrayList;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.foc.ConfigInfo;
import com.foc.Globals;

public class SpringBootTokenAuth {

	public static ArrayList<Algorithm> keys = null;
	public static String issuer = null;
	
	public SpringBootTokenAuth() {
		
	}
	
	public static String getIssuer() {
		if (issuer == null) {
			issuer = ConfigInfo.getProperty("jwt.issuer");
			if(issuer == null) issuer = "siren";
		}
		return issuer;
	}
	
	public static synchronized void reloadKeys() {
		Globals.logString("Auth: Reloading the JWT Keys");
		StringBuffer request = new StringBuffer("select \"create_date_time\", \"key\" from \"XSharedSecret\"");
		ArrayList<String[]> securityKeyList = Globals.getApp().getDataSource().command_SelectRequest(request, 2);
		
		if(securityKeyList != null) {
			keys = new ArrayList<Algorithm>();
			for(int i=0; i<securityKeyList.size(); i++) {
				String[] strKeys = securityKeyList.get(i);
				Algorithm algorithm = Algorithm.HMAC256(strKeys[1]);
				keys.add(algorithm);
			}
		}
	}

	private static String validateToken_WithSingleKey(Algorithm algorithm, String token) {
		String subject;
		try{
			JWTVerifier verifier = JWT.require(algorithm).withIssuer(getIssuer()).build(); // instance
			DecodedJWT jwt = verifier.verify(token);
			subject = jwt.getSubject();
		}catch (JWTVerificationException exception){
			// Invalid signature/claims
			subject = null;
		}
		return subject;
	}
	
	public static String validateToken_WithAllKeys(String token) {
		String subject = null;
		if (keys != null) {
			for(int i=0; i<keys.size() && subject == null; i++) {
				subject = validateToken_WithSingleKey(keys.get(i), token);
			}
		}
		
		return subject;
	}
	
	public static String validateToken(String token) {
		String subject = validateToken_WithAllKeys(token);
	
		if (subject == null) {
			reloadKeys();
			subject = validateToken_WithAllKeys(token);
		}
		return subject;
	}	

}
