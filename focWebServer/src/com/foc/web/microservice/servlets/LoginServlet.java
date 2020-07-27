package com.foc.web.microservice.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.foc.Globals;
import com.foc.admin.FocLoginAccess;
import com.foc.admin.FocUser;
import com.foc.admin.FocUserDesc;
import com.foc.list.FocList;
import com.foc.util.Encryptor;
import com.foc.web.microservice.entity.FocSimpleMicroServlet;
import com.foc.web.microservice.entity.FocSimpleTokenAuth;

public class LoginServlet extends FocSimpleMicroServlet {

	FocSimpleTokenAuth jwt = new FocSimpleTokenAuth();

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SessionAndApplication sessionAndApp = pushSessionLogin(request, response);
		Globals.logString(" => POST Begin LoginServlet /login");
		
		try{
			StringBuffer buffer = getRequestAsStringBuffer(request);
			String reqStr = buffer != null ? buffer.toString() : null;

			String userJson   = "";
			String loggedJson = "";

			JSONObject jsonObj = reqStr != null ? new JSONObject(reqStr) : null;

			if(jsonObj != null && jsonObj.has("username") && jsonObj.has("password")){
				String username = jsonObj.getString("username");
				String password = jsonObj.getString("password");
				String encryptedPassword = Encryptor.encrypt_MD5(String.valueOf(password));
				// Login
				FocLoginAccess loginAccess = new FocLoginAccess();
				Globals.logString("  = Username " + username + " Password encrypted " + encryptedPassword);
				int status = loginAccess.checkUserPassword(username, encryptedPassword, false);

				if(status != com.foc.Application.LOGIN_VALID) {
					FocList listOfUsers = FocUserDesc.getList();
					listOfUsers.reloadFromDB();
					status = loginAccess.checkUserPassword(username, encryptedPassword, false);
				}
				
				if(status == com.foc.Application.LOGIN_VALID){
					String token = jwt.generateToken(username);
					if(token != null){
						userJson = "{\"access_token\": \"" + token + "\"}";
						loggedJson = "{\"access_token\": \" *** \"}";
						response.setStatus(HttpServletResponse.SC_OK);
						
						FocUser user = FocUser.findUser(username);
//						if(user != null) {
//							DeviceInformation deviceInformation = DeviceInformation.fromRequest(request);
//
//							WSUserActivity.logLogin(user, deviceInformation);
//						}
					}else{
						userJson = "{\"message\": \"Error in login\"}";
						loggedJson = userJson; 
						response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					}
				}else{
					userJson = "{\"message\": \"Invalid Credentials\"}";
					loggedJson = userJson;
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				}
			}else{
				userJson = "{\"message\": \"Username Password not sent\"}";
				loggedJson = userJson;
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
			
			setCORS(response);
			Globals.logString("  = Returned: "+loggedJson);
			response.getWriter().println(userJson);
		}catch (Exception e){
			Globals.logException(e);
			setCORS(response);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println(e.getMessage());
		}
		
		Globals.logString(" <= POST End LoginServlet /login");
		
		if(sessionAndApp != null){
			sessionAndApp.logout();
		}		
	}

	@Override
	protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Globals.logString(" => Options Begin LoginServlet /login");
		super.doOptions(request, response);
		// The following are CORS headers. Max age informs the
		// browser to keep the results of this call for 1 day.
		setCORS(response);
		Globals.logString(" <= Options E LoginServlet /login");
	}

}
