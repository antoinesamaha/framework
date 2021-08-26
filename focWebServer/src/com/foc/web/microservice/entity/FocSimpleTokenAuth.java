package com.foc.web.microservice.entity;

import java.lang.reflect.Constructor;
import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.foc.ConfigInfo;
import com.foc.Globals;
import com.foc.admin.FocUser;
import com.foc.admin.FocUserDesc;
import com.foc.desc.FocDesc;
import com.foc.list.FocList;
import com.foc.util.Utils;
import com.foc.web.server.FocWebServer;

public class FocSimpleTokenAuth {
//	public static final String GUEST_TOKEN_START = "GUEST_TOKEN(";
//	public static final String GUEST_TOKEN_END   = ")";
//	
//	public static final String MOBILE_APP_TOKEN_START = "MOBILE_APP_TOKEN(";
//	public static final String MOBILE_APP_TOKEN_END   = ")";
	protected Algorithm algorithm = null;
	boolean attemptSpringBootAuth = false;

	public FocSimpleTokenAuth() {
		String key = ConfigInfo.getJWTTokenAlgorithmKey();
		if (key == null) {
			key = getKEY();
		}
		algorithm = Algorithm.HMAC256(key);
		
		String attemptSpringBootAuth_string = ConfigInfo.getProperty("jwt.attempt.auth.service");
		if (attemptSpringBootAuth_string == null) {
			attemptSpringBootAuth_string = ConfigInfo.getProperty("fenix.attempt.auth.service");
		}
		if(!Utils.isStringEmpty(attemptSpringBootAuth_string)) {
			attemptSpringBootAuth = attemptSpringBootAuth_string.equals("1") || attemptSpringBootAuth_string.equals("true");  
		}
	}
	
	protected String getKEY() {
		return "!@#dfdfSDFSdFSDFsdvkikhdcvq";
	}
	
	public String generateToken(String username) {
		String token;

		try{
			FocList list = FocUserDesc.getInstance().getFocList(FocList.LOAD_IF_NEEDED);
			list.loadIfNotLoadedFromDB();
			FocUser user = (FocUser) list.searchByPropertyStringValue(FocUserDesc.FLD_NAME, username);
			if(user != null){
				Date today = new Date();
				Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
				Builder builder = JWT.create();
				builder.withSubject(username);
				builder.withIssuer("auth0");
				builder.withClaim("full_name", user.getFullName());
				builder.withIssuedAt(today);
				builder.withExpiresAt(tomorrow);
				token = builder.sign(algorithm);
			}else{
				token = null;
			}
		}catch (JWTCreationException exception){
			token = null;
			// Invalid Signing configuration / Couldn't convert Claims.
		}
		return token;
	}

	public String verifyToken(String token) {
		String subject = null;
		try {
			if (attemptSpringBootAuth) {
				subject = SpringBootTokenAuth.validateToken(token);
			}

			if (subject == null) {		
		
				try{
					JWTVerifier verifier = JWT.require(algorithm).withIssuer("auth0").build(); // instance
					DecodedJWT jwt = verifier.verify(token);
					subject = jwt.getSubject();
				}catch (JWTVerificationException exception){
					// Invalid signature/claims
					subject = null;
				}
				
			}
		}catch (Exception e) {
			Globals.logException(e);
		}				
				
		return subject;
	}
	
	public DecodedJWT getDecodedJWT(String token) {
		DecodedJWT jwt=null;
		try{
			JWTVerifier verifier = JWT.require(algorithm).withIssuer("auth0").build(); // instance
			jwt = verifier.verify(token);
		}catch (JWTVerificationException exception){
			// Invalid signature/claims
		}
		return jwt;
	}

	public static boolean tokenHandler_TriedToLoadIt = false;
	public static IAuthTokenHandler tokenHandler = null;
	public static synchronized IAuthTokenHandler getTokenHandler() {
		if (!tokenHandler_TriedToLoadIt && tokenHandler == null) {
			tokenHandler_TriedToLoadIt = true;
			
			String className = ConfigInfo.getProperty("jwt.token.handler");
			
			if (!Utils.isStringEmpty(className)) {
		    try {    
		      Class<IAuthTokenHandler> cls = (Class<IAuthTokenHandler>) Class.forName(className);
		      
	        Class[]  param = null;
	        Object[] args  = null;
	        
	        Constructor<IAuthTokenHandler> constr = cls.getConstructor(param);
	        tokenHandler = constr.newInstance(args);
		    } catch (Exception e) {
		      Globals.logException(e);
		    }
			}
		}
		return tokenHandler;
	}
}
