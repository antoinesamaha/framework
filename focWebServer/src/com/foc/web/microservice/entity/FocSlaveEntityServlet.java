package com.foc.web.microservice.entity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.json.JSONArray;
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
	protected void doPost_Core(FocServletRequest focRequest) throws Exception {
		if (focRequest != null) {
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
						toJson_DetailedObject(builder, focObj);
						userJson = builder.toString();
						
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
				
	}

	@Override
	public void doDelete_Core(FocServletRequest focRequest) throws Exception {
		
		if (focRequest != null) {
			HttpServletRequest  request  = focRequest.getRequest();
			HttpServletResponse response = focRequest.getResponse();

			//Here starts the CORE Poste 
			//--------------------------
			StringBuffer buffer = getRequestAsStringBuffer(request);
			String       reqStr  = buffer != null ? buffer.toString() : null;
			JSONArray    jsonArr = new JSONArray(reqStr);
			
			if (reqStr != null) Globals.logString(" = Body: "+reqStr);

			B01JsonBuilder builder = newJsonBuilderForPostResponse();

			String errorMessage = null;
			
			//This should be called first to GET the master 
			FocList list = list_Create(focRequest, true);
			
			M master = (M) focRequest.getMaster();
			if (master == null) {
				errorMessage = "Could not find master object";
			} else {
				if(list == null) {
					errorMessage = "Could not find slave list";
				} else {

					builder.beginList();
					
					if(jsonArr != null) {
						for(int i=0; i<jsonArr.length(); i++) {
							JSONObject obj = (JSONObject) jsonArr.get(i);
							if(obj != null && obj.has("REF")) {
								long ref = obj.getLong("REF");
								
								if(ref != 0) {
									builder.beginObject();

									list.loadIfNotLoadedFromDB();
									O focObj = (O) list.searchByReference(ref);
									if(focObj != null) {
										focObj.delete();
										list.validate(true);
										master.validate(true);
										builder.appendKeyValue("REF", ref);
										builder.appendKeyValue("Status", "Deleted");
									} else {
										builder.appendKeyValue("REF", ref);
										builder.appendKeyValue("Status", "Object not found");
									}
									
									builder.endObject();
								}
							}
						}
					}
					
					builder.endList();

					String userJson = builder.toString();
					response.setStatus(HttpServletResponse.SC_OK);
					setCORS(response);
					response.getWriter().println(userJson);
				}
			}
		}
		
	}
}
