package com.foc.web.microservice.entity;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.foc.Globals;
import com.foc.admin.FocUser;
import com.foc.admin.FocUserDesc;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.desc.field.FField;
import com.foc.list.FocList;
import com.foc.shared.json.B01JsonBuilder;
import com.foc.web.microservice.FocObjectServlet;
import com.foc.web.microservice.FocServletRequest;

public class FocEntityServlet<O extends FocObject> extends FocObjectServlet<O> {

	private static String uiclassname = null;
	
	public void extractUIClassname(HttpServletRequest request) {
		if(uiclassname == null) {
			uiclassname = request.getParameter("uiclass");
		}
	}
	
	@Override
	protected String getUIClassName() {
		return uiclassname;
	}

	@Override
	public String getNameInPlural() {
		return "list";
	}

	@Override
	public FocDesc getFocDesc() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public FocDesc getFocDesc(FocServletRequest focRequest) {
		return null;
	}

	public FocDesc getJoinFocDesc(FocServletRequest focRequest) {
		return null;
	}

	public FocDesc getFilterFocDesc(FocServletRequest focRequest) {
		return null;
	}

	public FocList list_New(FocServletRequest focRequest) {
		FocDesc focDesc = getJoinFocDesc(focRequest);
		if(focDesc == null) focDesc = getFocDesc(focRequest);
		FocList list = focDesc != null ? focDesc.getFocList() : null;
		return list;
	}
	
	public String list_GetSQLOrder(FocList list, FocServletRequest focRequest) {
		String order = null;
		if (list != null && list.getFilter() != null) {
			FocDesc focDesc = list.getFocDesc();
			if (focDesc != null) {
				order = "";
				if(focDesc.getFieldByID(FField.FLD_DATE) != null) {
					order += "\""+FField.FNAME_DATE +"\" DESC";
				}
				if(focDesc.getFieldByID(FField.REF_FIELD_ID) != null) {
					if(order.length() > 0) order += " ";
					order += "\""+FField.REF_FIELD_NAME+"\" DESC";
				}
			}
		}
		return order;
	}
	
	@Override
	public boolean useCachedList(FocServletRequest focRequest) {
		boolean useCash = false;
		FocDesc focDesc = getFocDesc();
		if (focDesc != null) {
			if (focDesc.isListInCache()) {
				useCash = true;
			} else {
				useCash = false;
			}
		}
		return useCash;
	}
	
	public FocList newEntityList(FocServletRequest focRequest, boolean load) {
		FocList list = null;
		
		if(useCachedList(focRequest)) {
			FocDesc focDesc = getJoinFocDesc(focRequest);
			focDesc = focDesc == null ? getFocDesc(focRequest) : null;
			if (focDesc != null && focDesc.isListInCache()) {
				list = focDesc.getFocList();
				if (load) {
					list.loadIfNotLoadedFromDB();
				}
			}
		} else {
			HttpServletRequest request = focRequest.getRequest();
			
			int start = getStartParameter(request);
			int count = getCountParameter(request);
			list = list_New(focRequest);
			if (list != null) {
				String orderBy = list_GetSQLOrder(list, focRequest);
				if (orderBy != null) {
					list.getFilter().setOrderBy(orderBy);
				}
				if(list.getFilter() != null && start >= 0 && count >= 0) {
					list.getFilter().setOffset(start, count);
				}
				if(load) {
					list.loadIfNotLoadedFromDB();
				}
			}
		}
		
		return list;
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
			String authTokenHeader = request.getHeader("Authorization");
			if(authTokenHeader != null && authTokenHeader.startsWith("Bearer")){
				String token = authTokenHeader.substring("Bearer".length()).trim();
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
			}else{
				Globals.logString(" = Authorization header with 'Bearer' missing");
				session.logout();
				session = null;
			}
		}
		return session;
	}

	// ------------------------------------
	// ------------------------------------
	// GET
	// ------------------------------------
	// ------------------------------------
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			if(request != null && request.getSession() != null) {
				Globals.logString("Session ID Started request"+request.getSession().getId());
			}
			SessionAndApplication sessionAndApp = pushSession(request, response);
			if(sessionAndApp == null){
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				setCORS(response);
				String responseBody = "{\"message\": \"Unauthorised\"}";
				response.getWriter().println(responseBody);
			} else {
				FocServletRequest focRequest = null;
				try {
					focRequest = newFocServletRequest(sessionAndApp, request, response);
					
					Globals.logString(" => GET Begin "+getNameInPlural());
					if(!allowGet(focRequest)){
						response.setStatus(HttpServletResponse.SC_FORBIDDEN);
						setCORS(response);
						String responseBody = "{\"message\": \"Forbidden\"}";
						response.getWriter().println(responseBody);
					} else {
						logRequestHeaders(request);
						
						String userJson = "";
						String responseBody = "";
						B01JsonBuilder builder = newJsonBuiler(request);
						
						FocList list = newEntityList(focRequest, true);
						
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
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					setCORS(response);
					String responseBody = "{\"Exception\": \""+e.getMessage()+"\"}";
					response.getWriter().println(responseBody);
					
					Globals.logException(e);
				} finally {
					focRequest.dispose();
					sessionAndApp.logout();				
				}
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			setCORS(response);
			String responseBody = "{\"Exception\": \""+e.getMessage()+"\"}";
			response.getWriter().println(responseBody);

			Globals.logException(e);
			throw e;
		}
	}
}
