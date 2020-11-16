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
import com.foc.admin.ActiveUser;
import com.foc.admin.ActiveUserDesc;
import com.foc.admin.FocUser;
import com.foc.admin.FocUserDesc;
import com.foc.business.company.Company;
import com.foc.business.company.CompanyDesc;
import com.foc.business.workflow.WFSite;
import com.foc.business.workflow.WFSiteDesc;
import com.foc.business.workflow.WFTitle;
import com.foc.business.workflow.WFTitleDesc;
import com.foc.list.FocLinkSimple;
import com.foc.list.FocList;
import com.foc.shared.json.B01JsonBuilder;
import com.foc.util.Utils;
import com.foc.web.microservice.entity.FocSimpleMicroServlet;

public class ActiveUsersServlet extends FocSimpleMicroServlet {

	public static final String PARAM_TABLE_LIST = "activeUsers";
	public static final String PARAM_USER_REF = "user_ref";
	public static final String PARAM_COMPANY_REF = "company_ref";
	public static final String PARAM_SITE_REF = "site_ref";
	public static final String PARAM_TITLE_REF = "title_ref";
	public static final String PARAM_LAST_HEART_BEAT = "last_heart_beat";
	
	@Override
	public int getAuthenticationMethod() {
		return AUTH_NONE;
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		SessionAndApplication sessionAndApp = pushSession(request, response);
  	if(ActiveUserDesc.getInstance() != null) {
			FocList list = ActiveUserDesc.getInstance().getFocList();
			if(list != null) {
		  	B01JsonBuilder builder = new B01JsonBuilder();				
				builder.beginObject();
				builder.appendKey(PARAM_TABLE_LIST);
				builder.beginList();
				for(int i=0; i < list.size(); i++) {
					ActiveUser curr = (ActiveUser) list.getFocObject(i);
					builder.beginObject();
					builder.appendKeyValue(PARAM_USER_REF, curr.getUser().getReferenceInt());
					builder.appendKeyValue(PARAM_COMPANY_REF, curr.getCompanyRef());
					builder.appendKeyValue(PARAM_SITE_REF, curr.getUserSite().getReferenceInt());
					builder.appendKeyValue(PARAM_TITLE_REF, curr.getUserTitle().getReferenceInt());
					builder.appendKeyValue(PARAM_LAST_HEART_BEAT, curr.getLastHeartBeat().getTime());
					builder.endObject();
				}
				builder.endList();
				builder.endObject();
				String result = builder.toString();
				response.getWriter().println(result);
			}
  	}
	}
	
	
	// static method to call the get servlet above //
	
	public static FocList getNewWSActiveUsersList() {
		FocList list = new FocList(new FocLinkSimple(ActiveUserDesc.getInstance()));
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
						if(mainObject.has(ActiveUsersServlet.PARAM_TABLE_LIST)) {
							JSONArray activeUsersList = new JSONArray(mainObject.getString(ActiveUsersServlet.PARAM_TABLE_LIST));
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
										if(activeUser.has(ActiveUsersServlet.PARAM_USER_REF)) userRef = activeUser.getLong(ActiveUsersServlet.PARAM_USER_REF);
										if(activeUser.has(ActiveUsersServlet.PARAM_COMPANY_REF)) companyRef = activeUser.getLong(ActiveUsersServlet.PARAM_COMPANY_REF);
										if(activeUser.has(ActiveUsersServlet.PARAM_SITE_REF)) siteRef = activeUser.getLong(ActiveUsersServlet.PARAM_SITE_REF);
										if(activeUser.has(ActiveUsersServlet.PARAM_TITLE_REF)) titleRef = activeUser.getLong(ActiveUsersServlet.PARAM_TITLE_REF);
										if(activeUser.has(ActiveUsersServlet.PARAM_LAST_HEART_BEAT)) lastHeartbeat = activeUser.getLong(ActiveUsersServlet.PARAM_LAST_HEART_BEAT);
										if(userRef > 0 && lastHeartbeat > 0){
											ActiveUser newActiveUser = cerateActiveUserFromString(userRef, lastHeartbeat, companyRef,siteRef, titleRef);
											list.add(newActiveUser);
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
		return list;
	}
	
	public static ActiveUser cerateActiveUserFromString(long userRef, long lastHeartbeat, long companyRef, long siteRef, long titleRef) {
		ActiveUser newItem = null;
		try{
			if(ActiveUserDesc.getInstance() != null && ActiveUserDesc.getInstance().getFocList() != null && userRef > 0 && lastHeartbeat > 0){
				FocUser user = null;
				Company company = null;
				WFSite site = null;
				WFTitle title = null;
				if(FocUserDesc.getInstance() != null && FocUserDesc.getInstance().getFocList() != null) user = (FocUser) FocUserDesc.getInstance().getFocList(FocList.LOAD_IF_NEEDED).searchByRealReferenceOnly(userRef);
				if(companyRef > 0) {
					if(CompanyDesc.getInstance() != null && CompanyDesc.getInstance().getFocList() != null) company = (Company) CompanyDesc.getInstance().getFocList(FocList.LOAD_IF_NEEDED).searchByRealReferenceOnly(companyRef);
				} else if(user != null) {
					company = user.getCurrentCompany();
				}
				if(siteRef > 0) {
				if(WFSiteDesc.getInstance() != null && WFSiteDesc.getInstance().getFocList() != null) site = (WFSite) WFSiteDesc.getInstance().getFocList(FocList.LOAD_IF_NEEDED).searchByRealReferenceOnly(siteRef);
				} else if(user != null) {
					site = user.getCurrentSite();
				}
				if(titleRef > 0) {
					if(WFTitleDesc.getInstance() != null && WFTitleDesc.getInstance().getFocList() != null) title = (WFTitle) WFTitleDesc.getInstance().getFocList(FocList.LOAD_IF_NEEDED).searchByRealReferenceOnly(titleRef);
				} else if(user != null) {
					title = user.getCurrentTitle();
				}
				if(user != null) {
					newItem = (ActiveUser) ActiveUserDesc.getInstance().getFocList().newEmptyDisconnectedItem();
					java.sql.Date date = new java.sql.Date(lastHeartbeat);
					newItem.setLastHeartBeat(date);
					newItem.setUser(user);
					newItem.setCompany(company);
					newItem.setUserSite(site);
					newItem.setUserTitle(title);
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return newItem;
	}
}
