package com.foc.web.microservice.entity;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.foc.ConfigInfo;
import com.foc.admin.FocUser;
import com.foc.admin.FocUserDesc;
import com.foc.list.FocList;

public class FocSimpleTokenAuth {
//	public static final String GUEST_TOKEN_START = "GUEST_TOKEN(";
//	public static final String GUEST_TOKEN_END   = ")";
//	
//	public static final String MOBILE_APP_TOKEN_START = "MOBILE_APP_TOKEN(";
//	public static final String MOBILE_APP_TOKEN_END   = ")";
	Algorithm algorithm = null;

	public FocSimpleTokenAuth() {
		String key = ConfigInfo.getJWTTokenAlgorithmKey();
		if (key == null) {
			key = "!@#dfdfSDFSdFSDFsdvkikhdcvq";
		}
		algorithm = Algorithm.HMAC256(key);
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

	/*
	public String generateGuestToken(String guestTokenString) {
		String token = null;

		try{
			Date today = new Date();
			Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
			Builder builder = JWT.create();
			builder.withSubject(GUEST_TOKEN_START+guestTokenString+GUEST_TOKEN_END);
			builder.withIssuer("auth0");
			builder.withClaim("full_name", guestTokenString);
			builder.withIssuedAt(today);
			builder.withExpiresAt(tomorrow);
			token = builder.sign(algorithm);
		}catch (JWTCreationException exception){
			token = null;
			// Invalid Signing configuration / Couldn't convert Claims.
		}
		return token;
	}
	
	public String generateMobileAppToken(Long ref) {
		String token = null;

		try{
			Date today = new Date();
			LocalDateTime localDate= LocalDateTime.now().plusYears(1);
			Date nextYear = Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant());
			Builder builder = JWT.create();
			builder.withSubject(MOBILE_APP_TOKEN_START+String.valueOf(ref)+MOBILE_APP_TOKEN_END);
			builder.withIssuer("auth0");
			builder.withClaim("ref", ref);
			builder.withIssuedAt(today);
			builder.withExpiresAt(nextYear);
			token = builder.sign(algorithm);
		}catch (JWTCreationException exception){
			token = null;
			// Invalid Signing configuration / Couldn't convert Claims.
		}
		return token;
	}
	*/

	public String verifyToken(String token) {
		String subject;
		try{
			JWTVerifier verifier = JWT.require(algorithm).withIssuer("auth0").build(); // instance
			DecodedJWT jwt = verifier.verify(token);
			subject = jwt.getSubject();
		}catch (JWTVerificationException exception){
			// Invalid signature/claims
			subject = null;
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
}
