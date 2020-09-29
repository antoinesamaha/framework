package com.foc.web.microservice.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.foc.Globals;
import com.foc.admin.FocGroup;
import com.foc.admin.FocGroupDesc;
import com.foc.admin.FocLoginAccess;
import com.foc.admin.FocUser;
import com.foc.admin.FocUserDesc;
import com.foc.business.adrBook.AdrBookParty;
import com.foc.business.adrBook.AdrBookPartyDesc;
import com.foc.business.adrBook.Contact;
import com.foc.business.adrBook.ContactDesc;
import com.foc.business.company.Company;
import com.foc.business.workflow.WFSite;
import com.foc.business.workflow.WFTitle;
import com.foc.desc.FocDesc;
import com.foc.desc.field.FField;
import com.foc.list.FocList;
import com.foc.shared.json.B01JsonBuilder;
import com.foc.util.Encryptor;
import com.foc.web.microservice.entity.FocSimpleMicroServlet;
import com.foc.web.microservice.entity.FocSimpleTokenAuth;
import com.foc.web.modules.restmodule.LoginToken;
import com.foc.web.modules.restmodule.LoginTokenList;

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
				String username    = jsonObj.getString("username");
				String password    = jsonObj.getString("password");
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
					boolean login_token = jsonObj.has("login_token") ? jsonObj.getBoolean("login_token") : false;
					
					FocUser user = FocUser.findUser(username);
					String userProfile = buildUserProfileJson(user);
					
					B01JsonBuilder builder = new B01JsonBuilder();
					builder.beginObject();
					
					if(login_token) {
						LoginToken loginToken = LoginTokenList.generateToken(user);
						if (loginToken != null) {
							builder.appendKeyValue("access_token", loginToken.getToken());
						}
					} else {
						String token = jwt.generateToken(username);
						if(token != null){
							builder.appendKeyValue("access_token", token);		
						}
					}
					builder.appendKey("UserProfile");
					builder.append(userProfile);
					builder.endObject();
					userJson = builder.toString();
					loggedJson = "{\"access_token\": \" *** \", \"UserProfile\": "+userProfile+"}";
					response.setStatus(HttpServletResponse.SC_OK);
						
//						if(user != null) {
//							DeviceInformation deviceInformation = DeviceInformation.fromRequest(request);
//
//							WSUserActivity.logLogin(user, deviceInformation);
//						}
						
//					}else{
//						userJson = "{\"message\": \"Error in login\"}";
//						loggedJson = userJson; 
//						response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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

	protected void appendKeyValue_User(B01JsonBuilder builder, FocUser user){
		
	}
	
	private String buildUserProfileJson(FocUser user) {
		B01JsonBuilder builder = new B01JsonBuilder();

		builder.beginObject();

		if (user != null) {
			FocDesc userDesc = user.getThisFocDesc();
			
			WFSite  site    = user.getCurrentSite();
			WFTitle title   = user.getCurrentTitle();
			Company company = user.getCurrentCompany();
			
			user.appendKeyValueForFieldName(builder, FField.REF_FIELD_NAME);
			user.appendKeyValueForFieldName(builder, userDesc.getFieldNameByID(FocUserDesc.FLD_NAME));
			user.appendKeyValueForFieldName(builder, userDesc.getFieldNameByID(FocUserDesc.FLD_FULL_NAME));
	
			if (site != null) {
				builder.appendKey(userDesc.getFieldNameByID(FocUserDesc.FLD_CURRENT_SITE));
				builder.beginObject_InValue();
				site.appendKeyValueForFieldName(builder, FField.REF_FIELD_NAME);
				site.appendKeyValueForFieldName(builder, FField.FNAME_NAME);
				builder.endObject();
			}
			
			if (title != null) {
				builder.appendKey(userDesc.getFieldNameByID(FocUserDesc.FLD_CURRENT_TITLE));
				builder.beginObject_InValue();
				title.appendKeyValueForFieldName(builder, FField.REF_FIELD_NAME);
				title.appendKeyValueForFieldName(builder, FField.FNAME_NAME);
				builder.endObject();
			}
			
			if (company != null) {
				builder.appendKey(userDesc.getFieldNameByID(FocUserDesc.FLD_CURRENT_COMPANY));
				builder.beginObject_InValue();
				company.appendKeyValueForFieldName(builder, FField.REF_FIELD_NAME);
				company.appendKeyValueForFieldName(builder, FField.FNAME_NAME);
				builder.endObject();
			}

			appendKeyValue_User(builder, user);
			
			Contact contact = user.getContact();
			if (contact != null) {
				FocDesc contactDesc = contact.getThisFocDesc();
				builder.appendKey(userDesc.getFieldNameByID(FocUserDesc.FLD_CONTACT));
				builder.beginObject_InValue();
				contact.appendKeyValueForFieldName(builder, FField.REF_FIELD_NAME);
				contact.appendKeyValueForFieldName(builder, ContactDesc.FNAME_Title);
				contact.appendKeyValueForFieldName(builder, ContactDesc.FNAME_FirstName);
				contact.appendKeyValueForFieldName(builder, ContactDesc.FNAME_FamilyName);
				contact.appendKeyValueForFieldName(builder, ContactDesc.FNAME_Phone1);
				contact.appendKeyValueForFieldName(builder, ContactDesc.FNAME_Mobile);
				contact.appendKeyValueForFieldName(builder, ContactDesc.FNAME_EMail);
				contact.appendKeyValueForFieldName(builder, ContactDesc.FNAME_EMail2);
				contact.appendKeyValueForFieldName(builder, ContactDesc.FNAME_Position);
				
				AdrBookParty party = contact.getAdrBookParty();
				if (party != null) {
					FocDesc partyDesc = party.getThisFocDesc();
					if (partyDesc != null) {
						builder.appendKey(contactDesc.getFieldNameByID(ContactDesc.FLD_ADR_BOOK_PARTY));
						builder.beginObject_InValue();
						party.appendKeyValueForFieldName(builder, FField.REF_FIELD_NAME);
						party.appendKeyValueForFieldName(builder, partyDesc.getFieldNameByID(AdrBookPartyDesc.FLD_CODE));
						party.appendKeyValueForFieldName(builder, partyDesc.getFieldNameByID(AdrBookPartyDesc.FLD_NAME));
						
						Contact defaultContact = party.getDefaultContact();
						if(defaultContact != null) {
							builder.appendKey(partyDesc.getFieldNameByID(AdrBookPartyDesc.FLD_DEFAULT_CONTACT));
							builder.beginObject_InValue();
							defaultContact.appendKeyValueForFieldName(builder, FField.REF_FIELD_NAME);
							builder.endObject();
						}
						
						builder.endObject();
					}
				}
				
				builder.endObject();
			}
			
			FocGroup group = (FocGroup) user.getGroup(); 
			if(group != null) {
				group.load();
	
				builder.appendKey(userDesc.getFieldNameByID(FocUserDesc.FLD_GROUP));
				builder.beginObject_InValue();
				group.appendKeyValueForFieldName(builder, FField.REF_FIELD_NAME);
				group.appendKeyValueForFieldName(builder, FField.FNAME_NAME);
				group.appendKeyValueForFieldName(builder, FocGroupDesc.FNAME_GUEST_APPLICABLE);
				group.appendKeyValueForFieldName(builder, userDesc.getFieldNameByID(FocGroupDesc.FLD_MOBILE_MODULE_RIGHTS_LIST));
				builder.endObject();
				
	//			String mobileProfile = group.getMobileProfile();
	//			if(mobileProfile == null) mobileProfile = "";
	//			userJson += ",\"mobile_profile\": \"" + mobileProfile + "\"";
	
				/*
				FocList mobileModulesList = group.getMobileModuleRightsList();
				if(mobileModulesList != null && mobileModulesList.size() > 0) {
					userJson += "\"mobile_modules_access\": [";
					firstModule = true;
	
					boolean first = true; 
					for (int i=0; i<mobileModulesList.size(); i++) {
						GrpMobileModuleRights rights = (GrpMobileModuleRights) mobileModulesList.getFocObject(i);
						if(rights != null) {
							if(!first) userJson += ",";
							userJson += "{";
							userJson += "  \"module_name\":\""+rights.getModuleName()+"\"";
							
					  	if(rights.getCreate()) {
					  		userJson += "  ,\"Create\":true";
					  	}
					  	if(rights.getRead()) {
					  		userJson += "  ,\"Read\":true";
					  	}
					  	if(rights.getUpdate()) {
					  		userJson += "  ,\"Update\":true";
					  	}
					  	if(rights.getDelete()) {
					  		userJson += "  ,\"Delete\":true";
					  	}
							userJson += "}";
							first = false;
						}
					}
					
					userJson += "]";
				}
				*/
			}
		}
		
		builder.endObject();	
		return builder.toString();
	}
}
