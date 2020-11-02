package com.foc.web.microservice.servlets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.foc.Globals;
import com.foc.admin.FocUser;
import com.foc.business.adrBook.Contact;
import com.foc.business.adrBook.ContactDesc;
import com.foc.desc.FocDesc;
import com.foc.shared.json.B01JsonBuilder;
import com.foc.util.Utils;
import com.foc.web.microservice.FocServletRequest;
import com.foc.web.microservice.entity.FocEntityServlet;

public class FocUserPasswordChangeServlet extends FocEntityServlet<Contact> {

	public int getAuthenticationMethod() {
		return AUTH_BEARER;
	}
	
  @Override
  public FocDesc getFocDesc(FocServletRequest request) {
    return ContactDesc.getInstance();
  }
  
  @Override
  public boolean useCachedList(FocServletRequest focRequest) {
    return true;
  }

  @Override
  public boolean allowGet(FocServletRequest focRequest) {
  	return false;//super.allowGet(focRequest);
  }
  
  @Override
  public boolean allowDelete(FocServletRequest focRequest) {
  	return false;//super.allowGet(focRequest);
  }

  @Override  
	public boolean toJson_DetailedObject(B01JsonBuilder builder, Contact dist) {
  	if(dist != null) {
//      builder.beginObject();
//      builder.appendKeyValue("_IsEditable", dist.isEditableByCurrentUser());      
//      dist.appendKeyValueForFieldName(builder, null, FField.REF_FIELD_NAME);
      
//      dist.appendKeyValueForFieldName_FullObject(builder, null, MinistryNeed.FIELD_Ministry);
//      boolean fullObject = builder.isPrintForeignKeyFullObject();
//  		builder.setPrintForeignKeyFullObject(true);
//  		dist.appendKeyValueForFieldName(builder, null, MinistryNeed.FIELD_Ministry);
//  		builder.setPrintForeignKeyFullObject(fullObject);
//      
//      dist.appendKeyValueForFieldName(builder, null, MinistryNeed.FIELD_Category);
//      dist.appendKeyValueForFieldName(builder, null, MinistryNeed.FIELD_Sector);
//      dist.appendKeyValueForFieldName(builder, null, MinistryNeed.FIELD_Quantity);
//      dist.appendKeyValueForFieldName(builder, null, MinistryNeed.FIELD_Product);      
//      builder.endObject();
  	}
		return false;
	}

  @Override
  public boolean toJson_ListObject(B01JsonBuilder builder, Contact join) {
  	return toJson_DetailedObject(builder, join); 
  }
    
  protected void passwordChangedSuccessfully(FocUser user) {
  	
  }
  
	protected void doPost_Core(FocServletRequest focRequest) throws Exception {
		if(focRequest != null) {
			HttpServletRequest  request  = focRequest.getRequest();
			HttpServletResponse response = focRequest.getResponse();
			
			//Here starts the CORE Poste 
			//--------------------------
			String userJson = "";
		
			StringBuffer buffer = getRequestAsStringBuffer(request);
			String       reqStr = buffer.toString();
			
			if (reqStr != null) Globals.logString(" = Body: "+reqStr);
		
			B01JsonBuilder builder = newJsonBuilderForPostResponse();
			JSONObject     jsonObj = new JSONObject(reqStr);

			FocUser user = Globals.getApp().getUser_ForThisSession();
			if (user == null) {
				userJson = "{\"message\": \"Session has no user\"}";
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				setCORS(response);
				response.getWriter().println(userJson);
				
			} else {
				boolean missingFields = !jsonObj.has("old_password") || !jsonObj.has("new_password") || !jsonObj.has("confirm_password");
				if (missingFields) {
					userJson = "{\"message\": \"Missing fields\"}";
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					setCORS(response);
					response.getWriter().println(userJson);
					
				} else {
					String oldPassword = jsonObj.getString("old_password");
					String newPassword = jsonObj.getString("new_password");
					String confirmPassword = jsonObj.getString("confirm_password");
					
					String errorMessage = user.canChangePassword(oldPassword, newPassword, confirmPassword, true);
					
					if (!Utils.isStringEmpty(errorMessage)) {
						userJson = "{\"message\": \""+errorMessage+"\"}";
						response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
						setCORS(response);
						response.getWriter().println(userJson);
						
					} else {
						user.changePassword(newPassword);
						
						passwordChangedSuccessfully(user);

						userJson = "{\"message\": \"Password changed successfully\"}";
						response.setStatus(HttpServletResponse.SC_OK);
						setCORS(response);
						response.getWriter().println(userJson);
					}
				}	
			}
		}
	}
  
}
