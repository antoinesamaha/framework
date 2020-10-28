package com.foc.web.microservice.entity;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.foc.Globals;
import com.foc.admin.FocLoginAccess;
import com.foc.admin.FocUser;
import com.foc.admin.FocUserDesc;
import com.foc.admin.GrpMobileModuleRights;
import com.foc.business.workflow.implementation.FocWorkflowObject;
import com.foc.db.DBManager;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.desc.field.FField;
import com.foc.desc.parsers.ParsedFocDesc;
import com.foc.join.FocRequestDesc;
import com.foc.join.TableAlias;
import com.foc.list.FocLinkSimple;
import com.foc.list.FocList;
import com.foc.list.FocListWithFilter;
import com.foc.shared.json.B01JsonBuilder;
import com.foc.shared.json.JSONObjectWriter;
import com.foc.util.Encryptor;
import com.foc.util.Utils;
import com.foc.web.microservice.FocObjectServlet;
import com.foc.web.microservice.FocServletRequest;

// RIGHTS
// LIST
// JSON
// GET
// PUSH
// DELETE
public class FocJoinEntityServlet<O extends FocObject, J extends FocObject> extends FocObjectServlet<O> {

	private static String uiclassname = null;
	
	public static int AUTH_NONE                  = 0;
	public static int AUTH_BEARER                = 1;
	public static int AUTH_USERNAME_PASSWORD     = 2;
	public static int AUTH_BEARER_THEN_USER_PASS = 3;
	
	/**
	 * Can be overwriden by the Servlet implementation 
	 * @return the Authentication method that this servlet requires
	 */
	public int getAuthenticationMethod() {
		return AUTH_BEARER;
	}

	/**
	 * The first API call needs to know the Vaadin UI class to be initialized. 
	 * The first call is done when we launch the JETTY server, so in the code of the
	 * Jetty server we specify this header parameter.    
	 */
	public void extractUIClassname(HttpServletRequest request) {
		if(uiclassname == null) {
			uiclassname = request.getParameter("uiclass");
		}
	}
	
	@Override
	protected String getUIClassName() {
		return uiclassname;
	}
	
	protected void setUIClassName(String classname) {
		uiclassname = classname;
	}

	@Override
	public String getNameInPlural() {
		return "list";
	}

	@Override
	public FocDesc getFocDesc() {
		return getFocDesc(null);
	}
	
	public FocDesc getFocDesc(FocServletRequest focRequest) {
		return null;
	}

	public FocDesc getJoinFocDesc(FocServletRequest focRequest) {
		return getFocDesc(focRequest);
	}

	public FocDesc getFilterFocDesc(FocServletRequest focRequest) {
		return null;
	}

	@Override
	public void fillFocObjectFromJson(O focObj, JSONObject jsonObj) throws Exception {
	}
	
  @Override
	public void afterPost(FocServletRequest focServletRequest, O focObj, boolean created) {
	}

	@Override
	public SessionAndApplication pushSession(HttpServletRequest request, HttpServletResponse response) throws IOException {
		SessionAndApplication session = super.pushSession(request, response);
		if(session != null){
			int authMethod = getAuthenticationMethod();
			String token = null;
			if(authMethod == AUTH_BEARER || authMethod == AUTH_BEARER_THEN_USER_PASS) {
				String authTokenHeader = request.getHeader("Authorization");
				if(authTokenHeader != null && authTokenHeader.startsWith("Bearer")){
					token = authTokenHeader.substring("Bearer".length()).trim();
					authMethod = AUTH_BEARER; 
				} else if(authMethod == AUTH_BEARER_THEN_USER_PASS){
					authMethod = AUTH_USERNAME_PASSWORD;
				}
			}
			
			if(authMethod == AUTH_BEARER) {
				if(!Utils.isStringEmpty(token)){
					FocSimpleTokenAuth auth = new FocSimpleTokenAuth();
					String username = auth.verifyToken(token);
	
					if(username != null){
						FocList list = FocUserDesc.getInstance().getFocList();
						if(list != null){
							list.loadIfNotLoadedFromDB();
							FocUser user = (FocUser) list.searchByPropertyStringValue(FocUserDesc.FLD_NAME, username, false);
							// Reload once if we don't find. This is in case a new user was
							// created
							if(user == null){
								Globals.logString(" = Username: " + username + " not found reloading user list");
								list.reloadFromDB();
								user = (FocUser) list.searchByPropertyStringValue(FocUserDesc.FLD_NAME, username, false);
							}
	
							if(user != null && !user.isSuspended()){
								session.getWebSession().setFocUser(user);
								Globals.logString(" = Session opened for username: " + username);
							}else{
								Globals.logString(" = Username: " + username + " not found, logout()");
								session.logout();
								session = null;
							}
						}else{
							Globals.logString(" = FocUser list null");
							session.logout();
							session = null;
						}
					}else{
						Globals.logString(" = Token Subject (Username) null!");
						session.logout();
						session = null;
					}
				} else {
					Globals.logString(" = Authorization header with 'Bearer' missing");
					session.logout();
					session = null;
				}
			} else if(authMethod == AUTH_USERNAME_PASSWORD) {
				String username = request.getHeader("username");
				String password = request.getHeader("password");
				if(username == null){
					username = (String) request.getAttribute(HEADER_KEY_USERNAME);
				}
				if(password == null){
					password = (String) request.getAttribute(HEADER_KEY_PASSWORD);
				}

				if (username != null && password != null) {
					Globals.logString(username);
					Globals.logString(password);
					String encryptedPassword = Encryptor.encrypt_MD5(String.valueOf(password));
					FocLoginAccess loginAccess = new FocLoginAccess();
	
					int status = loginAccess.checkUserPassword(username, encryptedPassword, false);
	
					if(status == com.foc.Application.LOGIN_VALID){
						// webSession = newApplication.getFocWebSession();
						session.getWebSession().setFocUser(loginAccess.getUser());
					}
					if(status == com.foc.Application.LOGIN_WRONG){
						Globals.logString(" = Authorization with Username password failed - Incorrect credentials");
						session.logout();
						session = null;						
						// PrintWriter printWriter = response.getWriter();
						// printWriter.println("Error: Login credentials are incorrect.");
					}
				} else {
					Globals.logString(" = Authorization with need to specify Username and password");
					session.logout();
					session = null;						
				}
			} else if(authMethod == AUTH_NONE) {
					
			} else {
				Globals.logString(" = Servlet does not specify Authorization Method");
				session.logout();
				session = null;
			}
		}
		return session;
	}

	// ------------------------------------
	// ------------------------------------
	// RIGHTS
	// ------------------------------------
	// ------------------------------------
	
	protected String mobileModule_GetModuleName(FocServletRequest focRequest) {
		return null;
	}
	
	protected GrpMobileModuleRights mobileModule_GetModule(FocServletRequest focRequest, String mobileModuleName) {
		GrpMobileModuleRights mobileModule = null;
		if (Globals.getApp() != null && Globals.getApp().getUser_ForThisSession() != null && Globals.getApp().getUser_ForThisSession().getGroup() != null) {
			mobileModule = Globals.getApp().getUser_ForThisSession().getGroup().getMobileModuleRightsObject(mobileModuleName);
		}
		return mobileModule;
	}
	
	protected boolean mobileModule_HasRight(FocServletRequest focRequest, char crud) {
		boolean right = false;
		String moduleName = mobileModule_GetModuleName(focRequest);
		if (Utils.isStringEmpty(moduleName)) {
			right = true;
		} else {
			GrpMobileModuleRights module = mobileModule_GetModule(focRequest, moduleName);
			if(module == null) {
				right = false;
			} else if(module.getRight() > 0){
				switch (crud) {
					case 'C':
						right = module.getCreate();
						break;
					case 'R':
						right = module.getRead();
						break;
					case 'U':
						right = module.getUpdate();
						break;
					case 'D':
						right = module.getDelete();
						break;
				}
			}
		}
		return right;
	}
	
	public boolean mobileModule_HasCreate(FocServletRequest focRequest) {
		return mobileModule_HasRight(focRequest, 'C'); 
	}
	
	public boolean mobileModule_HasRead(FocServletRequest focRequest) {
		return mobileModule_HasRight(focRequest, 'R'); 
	}
	
	public boolean mobileModule_HasUpdate(FocServletRequest focRequest) {
		return mobileModule_HasRight(focRequest, 'U'); 
	}
	
	public boolean mobileModule_HasDelete(FocServletRequest focRequest) {
		return mobileModule_HasRight(focRequest, 'D'); 
	}
	
	// ------------------------------------
	// ------------------------------------
	// LIST
	// ------------------------------------
	// ------------------------------------

	//Tells the Servlet if the list we are building is in Cache or not. Because it should not be disposed if in cache
	@Override
	public boolean useCachedList(FocServletRequest focRequest) {
		boolean useCash = false;
		FocDesc focDesc = getFocDesc(focRequest);
		if (focDesc != null) {
			if (focDesc.isListInCache()) {
				useCash = true;
			} else {
				useCash = false;
			}
		}
		return useCash;
	}

	/*
	 * Creates the FocList if needed and puts it in the FocServletRequest
	 * The newEntityList is called once in the servlet call
	 */
	public FocList list_CreateIfNeeded(FocServletRequest focRequest) {
		FocList list = focRequest != null ? focRequest.getList() : null;
		if (list == null) {
			list = list_Create(focRequest, true);
			focRequest.setList(list);
			focRequest.setListOwner(!useCachedList(focRequest));
		}
		return list;
	}

	public FocList list_Get_CreateIfNeeded(FocServletRequest focRequest) {
		return list_CreateIfNeeded(focRequest);
	}
	
	public FocList list_Post_CreateIfNeeded(FocServletRequest focRequest) {
		return list_Get_CreateIfNeeded(focRequest);
	}

	public FocList list_Delete_CreateIfNeeded(FocServletRequest focRequest) {
		return list_Get_CreateIfNeeded(focRequest);
	}

	public FocList list_Create(FocServletRequest focRequest, boolean load) {
		FocList list = null;
		
		if(useCachedList(focRequest)) {
			FocDesc focDesc = null;
			if (focRequest.getRef() != 0) {
				focDesc = getFocDesc(focRequest);
			} else {
				focDesc = getJoinFocDesc(focRequest);
				focDesc = focDesc == null ? getFocDesc(focRequest) : focDesc;
			}
			if (focDesc != null && focDesc.isListInCache()) {
				list = focDesc.getFocList();
			}
		} else {
			HttpServletRequest request = focRequest.getRequest();
			
			list = list_Create_NotCached(focRequest);
			
			if (list != null) {
				long ref = doGet_GetReference(request, list);
				if (ref != 0) {
					FocDesc desc = list.getFocDesc();
					String fieldName = desc.getRefFieldName();
					fieldName = DBManager.provider_ConvertFieldName(desc.getProvider(), fieldName);
					list.getFilter().putAdditionalWhere("REF", fieldName+"="+ref);
					//list.getFilter().putAdditionalWhere("REF", "I.\"REF\"="+ref);
				} else {
					int start = getStartParameter(request);
					int count = getCountParameter(request);
					String orderBy = list_GetSQLOrder(list, focRequest);
					if (orderBy != null) {
						list.getFilter().setOrderBy(orderBy);
					}
					if(list.getFilter() != null && start >= 0 && count >= 0) {
						list.getFilter().setOffset(start, count);
					}
				}
			}
		}
		if (list != null) {
			if(focRequest != null) {
				if(list instanceof FocListWithFilter) {
					focRequest.applyFiltersToListWithFilter((FocListWithFilter) list);
				} else {
					focRequest.applyFiltersToList(list);
				}
			}
			if(load) list.loadIfNotLoadedFromDB();  
		}
		
		return list;
	}
	
	/*
	 * Creation of the list. See also the method that tells the servlet if the list is a cached list
	 * This is important so that the servlet knows if it should dispose or not the list.
	 * 
	 * Can be overriden when the default behavior does not satisopy the need.
	 * Example: If we want a collection list built from a Cached list...
	 */
	protected FocList list_Create_NotCached(FocServletRequest focRequest) {
		
		//Determining the FocDesc
		//If the request is about a REF then we need the detailed FocDesc
		//Otherwise we need the List/Join FocDesc
		//Note that often the list FocDesc redirects to the Detailed focDesc
		FocDesc focDesc = null;
		if (focRequest.getRef() != 0) {
			focDesc = getFocDesc(focRequest);
		} else {
			focDesc = getJoinFocDesc(focRequest);
			if(focDesc == null) focDesc = getFocDesc(focRequest);
		}
		
		//The default building of the list depends on weather it is Cached or not
		//Developer should override the useCachedList method  
		FocList list = null;
		if(useCachedList(focRequest)) {
			list = focDesc != null ? focDesc.getFocList() : null;
		} else {
			list = new FocList(new FocLinkSimple(focDesc));
		}
		
		return list;
	}
	
	/*
	 * SQL Order is only needed for Pagination when the List is not cached.
	 * By default we consider the Order on Date and REF. This does not work 
	 * When the list is a Join you need to override in order to set the Aliases 
	 */
	public String list_GetSQLOrder(FocList list, FocServletRequest focRequest) {
		String order = null;
		if (list != null && list.getFilter() != null) {
			FocDesc focDesc = list.getFocDesc();
			if (focDesc != null) {
				String alias = "";
				
				if(focDesc.isJoin() && focDesc instanceof ParsedFocDesc) {
					ParsedFocDesc parsedDesc = (ParsedFocDesc) focDesc;
					FocRequestDesc reqDesc = parsedDesc.getFocRequestDesc();
					TableAlias rootAlias = reqDesc.getRootTableAlias();
					alias = rootAlias.getAlias()+".";
				}
				
				order = "";
				if(focDesc.getFieldByID(FField.FLD_DATE) != null) {
					order += alias+"\""+FField.FNAME_DATE +"\" ";
				}
				if(focDesc.getFieldByID(FField.REF_FIELD_ID) != null) {
					if(order.length() > 0) order += ",";
					order += alias+"\""+FField.REF_FIELD_NAME+"\" ";
				}
				order += " DESC";
			}
		}
		return order;
	}

	// ------------------------------------
	// ------------------------------------
	// JSON
	// ------------------------------------
	// ------------------------------------
	
	public boolean toJson_ListObject(B01JsonBuilder builder, J focObject) {
		focObject.toJson_InList(builder);
		return false;
	}

	public boolean toJson_DetailedObject(B01JsonBuilder builder, O focObject) {
		focObject.toJson_Detailed(builder);
		return false;
	}

	public B01JsonBuilder xmlBuilder_New(FocServletRequest request, boolean getRequest, boolean detail) {
		B01JsonBuilder builder = new B01JsonBuilder();
		builder.setPrintForeignKeyFullObject(true);
//		builder.setPrintObjectNamesNotRefs(true);
		builder.setHideWorkflowFields(true);
		builder.setScanSubList(true);
		
		if(getRequest && !detail) {
			//Put json serializer to Join
			FocDesc desc = getJoinFocDesc(request);
			if(desc != null) {
				JSONObjectWriter writer = new JSONObjectWriter<J>() {
					@Override
					public boolean writeJson(B01JsonBuilder builder, J focObject) {
						toJson_ListObject(builder, focObject);
						return false;
					}
				};
				builder.putJsonObjectWriter(desc.getStorageName(), writer);
			}
		} else if(getRequest && detail) {
			//Put json serializer to Join
			FocDesc desc = getFocDesc(request);
			if(desc != null) {
				JSONObjectWriter writer = new JSONObjectWriter<O>() {
					@Override
					public boolean writeJson(B01JsonBuilder builder, O focObject) {
						toJson_DetailedObject(builder, focObject);
						return false;
					}
				};
				builder.putJsonObjectWriter(desc.getStorageName(), writer);
			}			
		} else {
		}
		
		return builder;
	}

	// ------------------------------------
	// ------------------------------------
	// GET
	// ------------------------------------
	// ------------------------------------

	public void doGet_Core(FocServletRequest focRequest) throws Exception {
		HttpServletRequest  request  = focRequest.getRequest();
		HttpServletResponse response = focRequest.getResponse();
		
		//Here starts the CORE Get 
		//------------------------
		
		String userJson = "";
		String responseBody = "";
		B01JsonBuilder builder = null;//newJsonBuiler(request);
		
		long filterRef = focRequest.getRef();
		if(filterRef > 0) {
			//Get a single object
			//-------------------
			FocObject focObject = null;
			if(!useCachedList(null)){
				FocDesc focDesc = getFocDesc(focRequest);
				if (focDesc != null) {
					FocConstructor constr = new FocConstructor(focDesc);
					focObject = constr.newItem();
					focObject.setReference(filterRef);
					focObject.load();
				}
			} else {
				FocList list = list_Get_CreateIfNeeded(focRequest);
				focObject = list != null ? list.searchByReference(filterRef) : null;
			}
			
			if(focObject != null) {
				builder = xmlBuilder_New(focRequest, true, true);
				userJson = toJsonDetails(focObject, builder); 
				responseBody = userJson;
			} else {
				userJson = "{}";
				responseBody = userJson;
			}
		} else {
			FocList list = list_Get_CreateIfNeeded(focRequest);

			int start = -1;
			int count = -1;
			if(useCachedList(null)){
				start = getStartParameter(request);
				count = getCountParameter(request);
			}
			int totalCount = list.size();
			builder = xmlBuilder_New(focRequest, true, false);
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
			responseBody = "{ \"" + getNameInPlural() + "\":" + userJson + ", \"totalCount\":"+totalCount+"}";
		}
								
		response.setStatus(HttpServletResponse.SC_OK);
		setCORS(response);
		response.getWriter().println(responseBody);
		String log = responseBody;
		if(log.length() > 500) log = log.substring(0, 499)+"...";
		
		Globals.logString("  = Returned: "+log);
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
				
				Globals.logString(" => GET Begin "+getNameInPlural());
				
				if(!allowGet(focRequest) || !mobileModule_HasRead(focRequest)){
					response.setStatus(HttpServletResponse.SC_FORBIDDEN);
					setCORS(response);
					String responseBody = "{\"message\": \"Forbidden\"}";
					response.getWriter().println(responseBody);
				} else {
					logRequestHeaders(request);

					doGet_Core(focRequest);
				}
				
				Globals.logString(" <= GET End "+getNameInPlural()+" "+response.getStatus());
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
	
	// ------------------------------------
	// ------------------------------------
	// PUSH
	// ------------------------------------
	// ------------------------------------
	
	protected void doPost_Core(FocServletRequest focRequest) throws Exception {
		if(focRequest != null) {
			long ref = focRequest.getRef();
			
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
		
			O focObj = null;
			FocList list = null;
			
			if(useCachedList(focRequest)){
				list = list_Post_CreateIfNeeded(focRequest); 
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
				FocConstructor constr = new FocConstructor(getFocDesc(focRequest));
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
						((FocWorkflowObject) focObj).setCompanyAndSiteToAnyValueIfEmpty();
//						((FocWorkflowObject) focObj).setSiteToAnyValueIfEmpty();
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
		
					builder = xmlBuilder_New(focRequest, true, true);
					userJson = toJsonDetails(focObj, builder);
					
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
		}
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
				long ref = focRequest.getRef();
				
				Globals.logString(" => POST Begin "+getNameInPlural());
				if(			!allowPost(focRequest) 
						|| (ref != 0 && !mobileModule_HasUpdate(focRequest))
						|| (ref == 0 && !mobileModule_HasCreate(focRequest))
						){
					response.setStatus(HttpServletResponse.SC_FORBIDDEN);
					setCORS(response);
					String responseBody = "{\"message\": \"Forbidden\"}";
					response.getWriter().println(responseBody);
				} else {
					logRequestHeaders(request);
					
					doPost_Core(focRequest);
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
	
	// ------------------------------------
	// ------------------------------------
	// DELETE
	// ------------------------------------
	// ------------------------------------
	
	public void doDelete_Core(FocServletRequest focRequest) throws Exception {
		HttpServletRequest  request  = focRequest.getRequest();
		HttpServletResponse response = focRequest.getResponse();

		// Here starts the CORE Delete 
		//----------------------------
		StringBuffer buffer  = getRequestAsStringBuffer(request);
		String       reqStr  = buffer != null ? buffer.toString() : null;
		JSONArray    jsonArr = new JSONArray(reqStr);

		B01JsonBuilder builder = newJsonBuilderForPostResponse();
		builder.beginList();
		
		if(jsonArr != null) {
			for(int i=0; i<jsonArr.length(); i++) {
				JSONObject obj = (JSONObject) jsonArr.get(i);
				if(obj != null && obj.has("REF")) {
					long ref = obj.getLong("REF");
					
					if(ref != 0) {
						builder.beginObject();
						
						if(useCachedList(focRequest)) {
							FocList list = list_Delete_CreateIfNeeded(focRequest); 
							if(list != null){
								list.loadIfNotLoadedFromDB();
								O focObj = (O) list.searchByReference(ref);
								if(focObj != null) focObj.delete();
							}
							list.validate(true);
							builder.appendKeyValue("REF", ref);
							builder.appendKeyValue("Status", "Deleted");
						} else {
							FocConstructor constr = new FocConstructor(getFocDesc());
							O focObj = (O) constr.newItem();
							focObj.setReference(ref);
							
							focObj.load();
							focObj.setDeleted(true);
							boolean error = !focObj.validate(true);
							builder.appendKeyValue("REF", ref);
							if (error) {
								builder.appendKeyValue("Status", "Error, could not delete");
							} else {
								builder.appendKeyValue("Status", "Deleted");	
							}
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

//		if(focObj != null){
//
//			if (errorSaving) {
//				userJson = "{\"message\": \"Could not save\"}";
//				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//				setCORS(response);
//				response.getWriter().println(userJson);
//				
//			} else {
//				afterPost(focRequest, focObj, created);
//
//				builder = xmlBuilder_New(focRequest, true, true);
//				userJson = toJsonDetails(focObj, builder);
//				
//				response.setStatus(HttpServletResponse.SC_OK);
//				setCORS(response);
//				response.getWriter().println(userJson);
//			}
//		}
	}
	
	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
				long ref = focRequest.getRef();
				
				Globals.logString(" => DELETE Begin "+getNameInPlural());
				if(		 !allowPost(focRequest) 
						|| !mobileModule_HasDelete(focRequest)
						){
					response.setStatus(HttpServletResponse.SC_FORBIDDEN);
					setCORS(response);
					String responseBody = "{\"message\": \"Forbidden\"}";
					response.getWriter().println(responseBody);
				} else {
					logRequestHeaders(request);

					doDelete_Core(focRequest);
				}
				
				Globals.logString(" <= DELETE End "+getNameInPlural()+" "+response.getStatus());
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
