package com.foc.web.microservice;

import java.io.IOException;
import java.sql.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.foc.Globals;
import com.foc.admin.ActiveUser;
import com.foc.admin.ActiveUserDesc;
import com.foc.admin.FocUser;
import com.foc.business.workflow.implementation.FocWorkflowObject;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.desc.field.FField;
import com.foc.list.FocList;
import com.foc.shared.json.B01JsonBuilder;
import com.foc.util.Utils;

public abstract class FocObjectServlet<O extends FocObject> extends FocMicroServlet {

	public abstract FocDesc getFocDesc();

	public abstract String getNameInPlural();

	public abstract void fillFocObjectFromJson(O focObj, JSONObject jsonObj) throws Exception;
	
	public void afterPost(FocServletRequest focServletRequest, O focObj, boolean created) {
		
	}
	
	public void addFilterToJSONBuilder(FocServletRequest request, B01JsonBuilder builder) {
		
	}

	public boolean allowGet(FocServletRequest focRequest) {
		return true;
	}

	public boolean allowPost(FocServletRequest focRequest) {
		return true;
	}

	public boolean allowPut(FocServletRequest focRequest) {
		return true;
	}

	public boolean allowDelete(FocServletRequest focRequest) {
		return true;
	}

	public boolean allowOptions(FocServletRequest focRequest) {
		return true;
	}

	public boolean useCachedList(FocServletRequest focRequest) {
		return true;
	}

	@Deprecated
	protected void copyDATEFromJson(FocObject focObj, JSONObject jsonObj) {
		if(focObj != null){
			focObj.jsonParseDATE(jsonObj);
		}		
	}
	
	@Deprecated
	protected void copyStringFromJson(FocObject focObj, JSONObject jsonObj, String fieldName) {
		if(focObj != null){
			focObj.jsonParse(jsonObj, fieldName);
		}		
	}
		
	@Deprecated
	protected void copyDateFromJson(FocObject focObj, JSONObject jsonObj, String fieldName) {
		if(focObj != null){
			focObj.jsonParse(jsonObj, fieldName);
		}		
	}
	
	@Deprecated
	protected void copyBooleanFromJson(FocObject focObj, JSONObject jsonObj, String fieldName) {
		if(focObj != null){
			focObj.jsonParse(jsonObj, fieldName);
		}		
	}

	@Deprecated
	protected void copyIntFromJson(FocObject focObj, JSONObject jsonObj, String fieldName) {
		if(focObj != null){
			focObj.jsonParse(jsonObj, fieldName);
		}
	}
	
	@Deprecated
	protected void copyLongFromJson(FocObject focObj, JSONObject jsonObj, String fieldName) {
		if(focObj != null){
			focObj.jsonParse(jsonObj, fieldName);
		}		
	}
	
	@Deprecated
	protected void copyDoubleFromJson(FocObject focObj, JSONObject jsonObj, String fieldName) {
		if(focObj != null){
			focObj.jsonParse(jsonObj, fieldName);
		}
	}

	@Deprecated
	protected void copyForeignKeyFromJson(FocObject focObj, JSONObject jsonObj, String fieldName) {
		if(focObj != null){
			focObj.jsonParse(jsonObj, fieldName);
		}
	}
	
	private static final long EXPIRY_TIME = 1 * 60 * 1000;
	private long lastLoadTime = 0;

	public FocList newFocList(HttpServletRequest request, HttpServletResponse response, boolean loaded) {
		FocDesc focDesc = getFocDesc();
		FocList list = focDesc != null ? focDesc.getFocList() : null;
		if(loaded) {
			//If refresh cached list is functional on the foc level then here we do not need to do it 
//			if(false && !ConfigInfo.isRefreshCachedLists() && System.currentTimeMillis() - lastLoadTime > EXPIRY_TIME) {
//				lastLoadTime = System.currentTimeMillis();
//				list.reloadFromDB();
//			} else {
				list.loadIfNotLoadedFromDB();
//			}
		}
		return list; 
	}

	public void disposeFocList(FocServletRequest focRequest, FocList focList) {
		if(focList != null) {
			focList.dispose();
			focList = null;
		}
	}
		
	public void applyRefFilterIfNeeded(HttpServletRequest request, FocList list) {
		if(list != null && request != null) {
			long filter_Ref = doGet_GetReference(request, list);
			if(filter_Ref > 0 && list.getFilter() != null) list.getFilter().putAdditionalWhere("REF", "\"REF\"="+filter_Ref);
		}
	}
	
	protected long doPost_GetReference(JSONObject jsonObj, FocList list) {
		int ref = 0;
		if(jsonObj != null && jsonObj.has("REF")){
			try {
				String strValue = jsonObj.getString("REF");
				if (!strValue.equalsIgnoreCase("null")) {
					ref = Utils.parseInteger(strValue, 0);
				}
			} catch (Exception e) {
				Globals.logException(e);
			}
		}
		return ref;
	}
	
	protected long doGet_GetReference(HttpServletRequest request, FocList list) {
		return getFilterRef(request);
	}
	
	public long getFilterRef(HttpServletRequest request) {
		String refStr = request != null ? request.getParameter("REF") : null;
		long   ref    = refStr != null ? Utils.parseLong(refStr, 0) : 0;
		return ref;
	}

	protected B01JsonBuilder newJsonBuiler(HttpServletRequest request) {
		B01JsonBuilder builder = new B01JsonBuilder();
		builder.setPrintForeignKeyFullObject(true);
		builder.setHideWorkflowFields(true);
		builder.setScanSubList(true);
		return builder;
	}

	protected void logRequestHeaders(HttpServletRequest request) {
		if (request != null) {
			Enumeration<String> enums = request.getHeaderNames();
			if (enums != null) {
				while(enums.hasMoreElements()) {
					String key = enums.nextElement();
					if(key != null) {
						String value = "";							
						if(key.equals("Authorization")) {
							value = "***";
						} else {
							value = request.getHeader(key);
						}
						Globals.logString(" = Header: "+key+": "+value);
					}
				}
			}
		}
	}
	
	protected FocServletRequest newFocServletRequest(SessionAndApplication sessionAndApp, HttpServletRequest request, HttpServletResponse response) {
		FocServletRequest focRequest = new FocServletRequest(sessionAndApp, request, response);
		return focRequest;
	}

	protected int getStartParameter(HttpServletRequest request) { 
		String startStr = request != null ? request.getParameter("start") : null;
		int    start    = startStr != null ? Integer.valueOf(startStr) : -1;
		return start; 
	}
	
	protected int getCountParameter(HttpServletRequest request) {
		String countStr = request != null ? request.getParameter("count") : null;
		int    count    = countStr != null ? Integer.valueOf(countStr) : -1;
		return count;
	}
	
	protected int requestTotalCount(FocList list) {
		int totalCount = list != null ? list.requestCount() : 0;
		return totalCount; 
	}
	
	protected String toJsonDetails(FocObject focObject, B01JsonBuilder builder) {
		focObject.toJson(builder);
		return builder.toString();
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			if(request != null && request.getSession() != null) {
				Globals.logString("Session ID Started request"+request.getSession().getId());
			}
			SessionAndApplication sessionAndApp = pushSession(request, response);
			if(sessionAndApp != null){
				FocServletRequest focRequest = null;
				try {
					focRequest = newFocServletRequest(sessionAndApp, request, response);
					
					Globals.logString(" => GET Begin "+getNameInPlural());
					if(allowGet(null)){
						logRequestHeaders(request);
						
						String userJson = "";
						String responseBody = "";
						B01JsonBuilder builder = newJsonBuiler(request);
						
						//We get this only to see if we return a list or just an object
						
						FocList list = newFocList(request, response, true);
						
						long filterRef = doGet_GetReference(request, list);
						if(filterRef > 0) {
							FocObject focObject = null;
							if(!useCachedList(null)){
								if(list.size() == 1) {
									focObject = list.getFocObject(0); 
								}
							} else {
								focObject = list.searchByReference(filterRef);
							}
							
							if(focObject != null) {
								userJson = toJsonDetails(focObject, builder); 
								responseBody = userJson;
							} else {
								userJson = "{}";
								responseBody = userJson;
							}
						} else {
							int start = -1;
							int count = -1;
							if(useCachedList(null)){
								start = getStartParameter(request);
								count = getCountParameter(request);
							}
							int totalCount = list.size();
							if(useCachedList(focRequest) && builder.getObjectFilter() != null) {
								totalCount = list.toJson_TotalCount(builder);
							}
							if(list.getFilter() != null && list.getFilter().getOffset() >= 0 && list.getFilter().getOffsetCount() >= 0) {
								totalCount =	requestTotalCount(list);
							}
							
							builder.setListStart(start);
							builder.setListCount(count);
							list.toJson(builder);
							userJson = builder.toString();
							//responseBody = "{ \"" + getNameInPlural() + "\":" + userJson + "}";					
						  // add total if start or count is present in the request. If not paginated, no need to do a count query
							responseBody = "{ \"" + getNameInPlural() + "\":" + userJson + ", \"totalCount\":"+totalCount+"}";
//							responseBody = "{ \"list\":" + userJson + ", \"totalCount\":"+totalCount+"}";
						}
						
						if(!useCachedList(null)){
							disposeFocList(focRequest, list);
							list = null;
						}
						
						response.setStatus(HttpServletResponse.SC_OK);
						setCORS(response);
						response.getWriter().println(responseBody);
						String log = responseBody;
						if(log.length() > 500) log = log.substring(0, 499)+"...";
						Globals.logString("  = Returned: "+log);
					}
					Globals.logString(" <= GET End "+getNameInPlural()+" "+response.getStatus());
				} catch (Exception e) {
					Globals.logException(e);
				} finally {
					focRequest.dispose();
					sessionAndApp.logout();				
				}
	
			}else{
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				setCORS(response);
				String responseBody = "{\"message\": \"Unauthorised\"}";
				response.getWriter().println(responseBody);
			}
		} catch (Exception e) {
			Globals.logException(e);
			throw e;
		}
	}

	protected B01JsonBuilder newJsonBuilderForPostResponse() {
		B01JsonBuilder builder = new B01JsonBuilder();
		builder.setPrintForeignKeyFullObject(true);
		builder.setHideWorkflowFields(true);
		builder.setScanSubList(true);
		return builder;
	}
	
	protected O newFocObject_POST(FocServletRequest focRequest, long ref) {
		FocConstructor constr = new FocConstructor(getFocDesc());
		O focObj = (O) constr.newItem();

		if(ref > 0){
			focObj.setReference(ref);
			focObj.load();
		}else{
			focObj.setCreated(true);
			focObj.code_resetCode();
		}
		return focObj;
	}
	
	protected void disposeFocObject_POST(FocServletRequest focRequest, O focObj) {
		if(focObj != null) {
			focObj.dispose();
		}
	}
	
	protected String doPost_CheckError(FocServletRequest focRequest, JSONObject jsonObj) {
		return null;
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SessionAndApplication sessionAndApp = pushSession(request, response);
		if(sessionAndApp != null){
			FocServletRequest focRequest = newFocServletRequest(sessionAndApp, request, response);
			
			String userJson = "";
			
			Globals.logString(" => POST Begin "+getNameInPlural());
			if(allowPost(focRequest)){
				logRequestHeaders(request);
				
				StringBuffer buffer = getRequestAsStringBuffer(request);
				String reqStr = buffer.toString();
				
				if (reqStr != null) {
					Globals.logString(" = Body: "+reqStr);
				}
	
				B01JsonBuilder builder = newJsonBuilderForPostResponse();

				try{
					JSONObject jsonObj = new JSONObject(reqStr);

					O focObj = null;
					FocList list = null;

					String checkErrorJson = doPost_CheckError(focRequest, jsonObj);
					if (checkErrorJson == null) {
						if(useCachedList(null)){
							list = newFocList(request, response, false);
							if(list != null){
								list.loadIfNotLoadedFromDB();
								long ref = doPost_GetReference(jsonObj, list);
								if(ref > 0){
									focObj = (O) list.searchByRealReferenceOnly(ref);
								}else{
									focObj = (O) list.newEmptyItem();
									focObj.code_resetCode();
								}
							}
						}else{
							long ref = doPost_GetReference(jsonObj, list);
							focObj = newFocObject_POST(focRequest, ref);
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
							}else{
								errorSaving = !focObj.validate(true);
							}
	
							if (errorSaving) {
								userJson = "{\"message\": \"Could not save\"}";
								response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
								setCORS(response);
								response.getWriter().println(userJson);
								
							} else {
								afterPost(focRequest, focObj, created);
		
								userJson = toJsonDetails(focObj, builder); 
								//focObj.toJson(builder);
								//userJson = builder.toString();
								
								response.setStatus(HttpServletResponse.SC_OK);
								setCORS(response);
								response.getWriter().println(userJson);
							}
							
							if(!useCachedList(null)){
								disposeFocObject_POST(focRequest, focObj);
							}
							
						}else{
							userJson = "{\"message\": \" Does not exists \"}";
							response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
							setCORS(response);
							response.getWriter().println(userJson);
						}
					} else { 
						response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
						userJson = checkErrorJson;
						setCORS(response);
						response.getWriter().println(userJson);
					}

				}catch (Exception e){
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					userJson = "{\"message\": \"Bad Request\"}";
					Globals.logException(e);
					setCORS(response);
					response.getWriter().println(userJson);
				}
				
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				userJson = "{\"message\": \"Post not allowed by server\"}";
				setCORS(response);
				response.getWriter().println(userJson);
			}
			Globals.logString("  = Returned: "+userJson);
			Globals.logString(" <= POST End "+getNameInPlural()+" "+response.getStatus());
		
			sessionAndApp.logout();		
		}else{
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			String userJson = "{\"message\": \"Unauthorised\"}";
			setCORS(response);
			response.getWriter().println(userJson);
		}
	}

	@Override
	protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Globals.logString(" => OPTIONS Begin "+getNameInPlural());
		if(allowOptions(null)){
			super.doOptions(request, response);
			// The following are CORS headers. Max age informs the
			// browser to keep the results of this call for 1 day.
			setCORS(response);
		}
		Globals.logString(" <= OPTIONS End "+getNameInPlural());
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Globals.logString(" => DELETE Begin "+getNameInPlural());
		
		String userJson = "";
		if(allowDelete(null)){
			B01JsonBuilder builder = new B01JsonBuilder();
			builder.setPrintForeignKeyFullObject(true);
			builder.setHideWorkflowFields(true);
			SessionAndApplication sessionAndApp = pushSession(request, response);
			if(sessionAndApp != null){
				FocList list = getFocDesc().getFocList(FocList.LOAD_IF_NEEDED);
				
				long ref = doGet_GetReference(request, list);
				O focObj = null;
				if(ref > 0){
					focObj = (O) list.searchByRealReferenceOnly(ref);
					if(focObj != null){
						focObj.toJson(builder);
						userJson = builder.toString();

						focObj.setDeleted(true);
						focObj.validate(true);
						list.validate(true);
					}else{
						userJson = "{\"message\": \" Does not exists \"}";
					}
					response.setStatus(HttpServletResponse.SC_OK);
				}
				sessionAndApp.logout();
			}else{
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				userJson = "{\"message\": \"Unauthorised\"}";
			}
			setCORS(response);
			response.getWriter().println(userJson);
		}
		Globals.logString("  = Returned: "+userJson);
		Globals.logString(" <= DELETE End "+getNameInPlural());
	}
	
	public void postSlaveList(FocObject focObject, FocList list, JSONObject jsonObj, ICopyFromJsonToSlave copyFromJsonToSlave) throws Exception {
		if(list != null && jsonObj != null && copyFromJsonToSlave != null) {
			HashMap<Long, FocObject> toDelete = new HashMap<Long, FocObject>();
			for(int i=0; i<list.size(); i++) {
				FocObject slaveObj = (FocObject) list.getFocObject(i);
				toDelete.put(slaveObj.getReferenceInt(), slaveObj);
			}
			
			String tableName = list.getFocDesc().getStorageName();
			String listFieldName = tableName+"_LIST";
			if(list != null && jsonObj.has(listFieldName)) {
				JSONArray jsonArray = jsonObj.getJSONArray(listFieldName);
				if(jsonArray != null) {
					for(int i=0; i<jsonArray.length(); i++) {
						JSONObject slaveJson = (JSONObject) jsonArray.get(i);
						FocObject slaveObj = null;
						if (slaveJson.has(FField.REF_FIELD_NAME)) {
							//long ref = slaveJson.getLong(FField.REF_FIELD_NAME);
							String strValue = slaveJson.getString(FField.REF_FIELD_NAME);
							long ref = Utils.parseLong(strValue, 0);
							if (ref > 0) {
								slaveObj = list.searchByRealReferenceOnly(ref);
								if(slaveObj != null) {
									toDelete.remove(slaveObj.getReferenceInt());
								}
							}
						}
						
						if(slaveObj == null) {
							slaveObj = list.newEmptyItem();
							slaveObj.setCreated(true);
						}
	
						copyFromJsonToSlave.copyJsonToObject(slaveObj, slaveJson);
						
						slaveObj.validate(false);
					}
				}
			}
			
			Iterator<FocObject> iter = toDelete.values().iterator();
			while(iter != null && iter.hasNext()) {
				FocObject slaveObj = iter.next();
				slaveObj.setDeleted(true);
				list.remove(slaveObj);
				slaveObj.validate(false);
			}
		}
		
	}

	public interface ICopyFromJsonToSlave {
		public void copyJsonToObject(FocObject slaveObj, JSONObject slaveJson);
	}
	
	protected static void registerUserLastHeartbeat(FocUser user) {
		if(user != null && ActiveUserDesc.getInstance() != null) {
			FocList list = ActiveUserDesc.getInstance().getFocList();
			if(list != null) {
				ActiveUser activeUser = null;
				for(int i=0; i < list.size() && activeUser == null; i++) {
					ActiveUser curr = (ActiveUser) list.getFocObject(i);
					if(curr.getUser() != null && curr.getUser().equalsRef(user)) {
						activeUser = curr;
					}
				}
				if(activeUser == null) {
					activeUser = (ActiveUser) list.newEmptyItem();
					activeUser.setUserCompany(user.getCompany());
					activeUser.setUser(user);
					activeUser.setUserSite(user.getCurrentSite());
					activeUser.setUserTitle(user.getCurrentTitle());
				}
				long lastHeartBeat = System.currentTimeMillis();
				Date lastHeartBeatDate = new Date(lastHeartBeat);
				activeUser.setLastHeartBeat(lastHeartBeatDate);
				list.add(activeUser);
				activeUser.validate(false);
				list.validate(false);
			}
		}
	}
}
