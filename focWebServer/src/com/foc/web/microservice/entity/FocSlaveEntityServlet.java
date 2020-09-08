package com.foc.web.microservice.entity;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.foc.Globals;
import com.foc.business.workflow.implementation.FocWorkflowObject;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.shared.json.B01JsonBuilder;
import com.foc.web.microservice.FocServletRequest;

public abstract class FocSlaveEntityServlet<O extends FocObject, M extends FocObject> extends FocEntityServlet<O> {

	public abstract FocDesc getMasterFocDesc();
	public abstract FocList getSlaveList(M master);
	
	public M master_Get(FocServletRequest focRequest) {
		M master = null;
		
		if (focRequest != null) {
			master = (M) focRequest.getMaster();
			
			if (master == null) {
		  	FocDesc masterDesc = getMasterFocDesc();
		  	
		  	if(masterDesc != null) {
		  		if(useCachedList(focRequest)) {
		  			FocList masterList = masterDesc.getFocList();
		  			masterList.loadIfNotLoadedFromDB(); 
		  			master = (M) masterList.searchByReference(focRequest.getMasterRef());
		  			focRequest.setMaster(master);
		  			focRequest.setMasterOwner(false);
		  		} else {
		  			FocConstructor constr = new FocConstructor(masterDesc);
		  			master = (M) constr.newItem();
		  			master.setReference(focRequest.getMasterRef());
		  			master.load();
		  			focRequest.setMaster(master);
		  			focRequest.setMasterOwner(true);
		  		}
		  	}
			}
		}
		
  	return master;
	}
	
  @Override
  public FocList list_Create(FocServletRequest focRequest, boolean load) {
  	FocList list = null;
  	
  	M master = master_Get(focRequest);
  	if (master != null) {
  		list = getSlaveList(master);
  		if (load) {
  			list.loadIfNotLoadedFromDB();
  		}
  	}
  	
		return list;
  }
  
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SessionAndApplication sessionAndApp = null;
		FocServletRequest focRequest = null;		
		try {
			if(request != null && request.getSession() != null) {
				Globals.logString("Session ID Started request"+request.getSession().getId());
			}
			sessionAndApp = pushSession(request, response);
			if(sessionAndApp == null){
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				setCORS(response);
				String responseBody = "{\"message\": \"Unauthorised\"}";
				response.getWriter().println(responseBody);
			} else {
				focRequest = newFocServletRequest(sessionAndApp, request, response);
				
				Globals.logString(" => POST Begin "+getNameInPlural());
				if(!allowPost(focRequest)){
					
					response.setStatus(HttpServletResponse.SC_FORBIDDEN);
					setCORS(response);
					String responseBody = "{\"message\": \"Forbidden\"}";
					response.getWriter().println(responseBody);
				} else {
					logRequestHeaders(request);
					
					//Here starts the CORE Poste 
					//--------------------------
					String userJson = "";

					StringBuffer buffer = getRequestAsStringBuffer(request);
					String       reqStr = buffer.toString();
					
					if (reqStr != null) Globals.logString(" = Body: "+reqStr);
		
					B01JsonBuilder builder = newJsonBuilderForPostResponse();
					JSONObject     jsonObj = new JSONObject(reqStr);

					String errorMessage = null;
					
					O focObj = null;
					//This should be called first to GET the master 
					FocList list = list_Create(focRequest, true);
					
					M master = (M) focRequest.getMaster();
					if (master == null) {
						errorMessage = "Could not find master object";
					} else {
						if(list == null) {
							errorMessage = "Could not find slave list";
						} else {
							long ref = focRequest.getRef();
	
							if(ref != 0) {
								focObj = (O) list.searchByRealReferenceOnly(ref);
							} else {
								focObj = (O) list.newEmptyItem();
								focObj.code_resetCode();
							}
						}

						if(focObj != null){
							fillFocObjectFromJson(focObj, jsonObj);
	
							boolean created = focObj.isCreated();
							if(created) {
								if(focObj instanceof FocWorkflowObject) {
									((FocWorkflowObject) focObj).setSiteToAnyValueIfEmpty();
								}
							}
							
							boolean errorSaving = false;
							
							if(list != null){
								list.add(focObj);
								errorSaving = !focObj.validate(true);
								if(!errorSaving) {
									errorSaving = !list.validate(true);
								}
								if(!errorSaving) {
									errorSaving = !master.validate(true);
								}
							}
	
							if (errorSaving) {
								userJson = "{\"message\": \"Could not save\"}";
								response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
								setCORS(response);
								response.getWriter().println(userJson);
								
							} else {
								afterPost(focRequest, focObj, created);
		
								builder = xmlBuilder_New(focRequest, true, true);
								userJson = toJsonDetails(focObj, builder);
								
								response.setStatus(HttpServletResponse.SC_OK);
								setCORS(response);
								response.getWriter().println(userJson);
							}
						}
					} 
					
					if(errorMessage != null){
						userJson = "{\"message\": \" " + errorMessage + " \"}";
						response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
						setCORS(response);
						response.getWriter().println(userJson);
					}
				}
				
				Globals.logString(" <= POST End "+getNameInPlural()+" "+response.getStatus());
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			setCORS(response);
			String responseBody = "{\"Exception\": \""+e.getMessage()+"\"}";
			response.getWriter().println(responseBody);

			Globals.logException(e);
		} finally {
			if(focRequest != null) focRequest.dispose();
			if(sessionAndApp != null) sessionAndApp.logout();
		}
	}

}
