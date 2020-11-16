package com.foc.web.microservice.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.foc.ConfigInfo;
import com.foc.Globals;
import com.foc.admin.ActiveUserDesc;
import com.foc.admin.ActiveUserList;
import com.foc.util.Utils;
import com.foc.web.microservice.entity.FocSimpleMicroServlet;

public class ActiveUsersServlet extends FocSimpleMicroServlet {

	@Override
	public int getAuthenticationMethod() {
		return AUTH_NONE;
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ActiveUserList list = ActiveUserList.getInstance();
		if(list != null) {
			response.getWriter().println(list.toJson());
		}
	}

	// static method to call the get servlet above //

	public static void fillActiveUsersList(ActiveUserList list) {
		try{
			String url = ConfigInfo.getProperty("activeUsers.url");
			if(!Utils.isStringEmpty(url)) {
				HttpGet request = new HttpGet(url);
				HttpClient client = HttpClientBuilder.create().build();
				HttpResponse response = client.execute(request);
				if(response != null && response.getStatusLine() != null && response.getStatusLine().getStatusCode() == HttpServletResponse.SC_OK) {
					HttpEntity entity = response.getEntity();
					String responseString = EntityUtils.toString(entity, "UTF-8");
					System.out.println("reponding to WSActiveUsers GET call : " + responseString);
					try{
						JSONObject mainObject = new JSONObject(responseString);
						if(mainObject.has(ActiveUserList.PARAM_TABLE_LIST)) {
							JSONArray activeUsersList = new JSONArray(mainObject.getString(ActiveUserList.PARAM_TABLE_LIST));
							if (activeUsersList != null) {
								for(int i=0; i<activeUsersList.length(); i++) {
									String activeUserStr = activeUsersList.getString(i);
									if(!Utils.isStringEmpty(activeUserStr)) {
										JSONObject activeUser = new JSONObject(activeUserStr);
										long userRef = 0;
										long companyRef = 0;
										long siteRef = 0;
										long titleRef = 0;
										long lastHeartbeat = 0;
										if(activeUser.has(ActiveUserList.PARAM_USER_REF)) userRef = activeUser.getLong(ActiveUserList.PARAM_USER_REF);
										if(activeUser.has(ActiveUserList.PARAM_COMPANY_REF)) companyRef = activeUser.getLong(ActiveUserList.PARAM_COMPANY_REF);
										if(activeUser.has(ActiveUserList.PARAM_SITE_REF)) siteRef = activeUser.getLong(ActiveUserList.PARAM_SITE_REF);
										if(activeUser.has(ActiveUserList.PARAM_TITLE_REF)) titleRef = activeUser.getLong(ActiveUserList.PARAM_TITLE_REF);
										if(activeUser.has(ActiveUserList.PARAM_LAST_HEART_BEAT)) lastHeartbeat = activeUser.getLong(ActiveUserList.PARAM_LAST_HEART_BEAT);

										if(userRef > 0 && lastHeartbeat > 0){
											list.addActiveUser(userRef, lastHeartbeat, ActiveUserDesc.ORIGIN_MOBILE, companyRef,siteRef, titleRef);
										}
									}
								}
							}
						}
					} catch (JSONException e){
						e.printStackTrace();
					}
				}
			}
		}catch (Exception e){
			Globals.logException(e);
		}
	}
	
}