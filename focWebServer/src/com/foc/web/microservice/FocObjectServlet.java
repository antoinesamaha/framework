package com.foc.web.microservice;

import java.io.IOException;
import java.util.Enumeration;

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

	public void applyRefFilterIfNeeded(HttpServletRequest request, FocList list) {
		if(list != null && request != null) {
			long filter_Ref = getFilterRef(request);
			if(filter_Ref > 0 && list.getFilter() != null) list.getFilter().putAdditionalWhere("REF", "\"REF\"="+filter_Ref);
		}
	}
	
	public long getFilterRef(HttpServletRequest request) {
		String refStr = request != null ? request.getParameter("REF") : null;
		long   ref    = refStr != null ? Long.parseLong(refStr) : 0;
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
						long filterRef = getFilterRef(request);
						
						FocList list = newFocList(request, response, true);
						if(filterRef > 0) {
							if(list.size() == 1) {
								userJson = toJsonDetails(list.getFocObject(0), builder); 
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
							if(list.getFilter() != null && list.getFilter().getOffset() >= 0 && list.getFilter().getOffsetCount() >= 0) {
								totalCount =	requestTotalCount(list);
//								totalCount =	list.requestCount();
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
							list.dispose();
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

					int ref = 0;
					if(jsonObj.has("REF")){
						ref = jsonObj.getInt("REF");
					}

					O focObj = null;
					FocList list = null;

					if(useCachedList(null)){
						list = newFocList(request, response, false);
						if(list != null){
							list.loadIfNotLoadedFromDB();
							if(ref > 0){
								focObj = (O) list.searchByRealReferenceOnly(ref);
							}else{
								focObj = (O) list.newEmptyItem();
								focObj.code_resetCode();
							}
						}
					}else{
						FocConstructor constr = new FocConstructor(getFocDesc());
						focObj = (O) constr.newItem();

						if(ref > 0){
							focObj.setReference(ref);
							focObj.load();
						}else{
							focObj.setCreated(true);
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
	
							focObj.toJson(builder);
							userJson = builder.toString();
							response.setStatus(HttpServletResponse.SC_OK);
							setCORS(response);
							response.getWriter().println(userJson);
						}
					}else{
						userJson = "{\"message\": \" Does not exists \"}";
						response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
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
				long ref = getFilterRef(request);

				FocList list = getFocDesc().getFocList(FocList.LOAD_IF_NEEDED);
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
}
