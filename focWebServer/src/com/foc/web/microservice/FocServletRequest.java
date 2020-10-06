package com.foc.web.microservice;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.foc.db.DBManager;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.desc.field.FField;
import com.foc.desc.field.FObjectField;
import com.foc.desc.field.FStringField;
import com.foc.list.FocList;
import com.foc.util.Utils;
import com.foc.vaadin.FocWebApplication;
import com.foc.web.microservice.FocMicroServlet.SessionAndApplication;
import com.foc.web.server.session.FocWebSession;

public class FocServletRequest {
	private SessionAndApplication sessionAndApp = null;
	private HttpServletRequest  request  = null;
	private HttpServletResponse response = null;

	private boolean   masterOwner = false;
	private FocObject master      = null;
	private long      masterRef   = 0;
	
	private boolean listOwner = false;
	private FocList list      = null;
	private long    ref       = 0;
	
	public FocServletRequest(SessionAndApplication sessionAndApp, HttpServletRequest request, HttpServletResponse response) {
		this.sessionAndApp = sessionAndApp;
		this.request       = request;
		this.response      = response;
		
		if(request != null) {
			String refStr = request.getParameter("REF");
			if(refStr == null) refStr = request.getParameter("ref");
			long ref = refStr != null ? Utils.parseLong(refStr, 0) : 0;
			setRef(ref);
			
			String masterRefStr = request.getParameter("master_ref");
			long masterRef = masterRefStr != null ? Utils.parseLong(masterRefStr, 0) : 0;
			setMasterRef(masterRef);
			
			FocWebSession focWebSession = FocWebApplication.getInstanceForThread().getFocWebSession();
			focWebSession.putParameter(FocWebSession.PARAM_KEY_IP, request.getRemoteAddr());
		}
	}
	
	public void dispose() {
		sessionAndApp = null;
		request  = null;
		response = null;
		
		if(list != null && isListOwner()){
			list.dispose();
			list = null;
		}
		
		if (masterOwner && master != null) {
			master.dispose();
			master = null;
		}
	}

	public SessionAndApplication getSessionAndApp() {
		return sessionAndApp;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public boolean isListOwner() {
		return listOwner;
	}

	public void setListOwner(boolean listOwner) {
		this.listOwner = listOwner;
	}
	
	public FocList getList() {
		return list;
	}

	public void setList(FocList list) {
		this.list = list;
	}

	public long getRef() {
		return ref;
	}

	public void setRef(long ref) {
		this.ref = ref;
	}

	public long getMasterRef() {
		return masterRef;
	}

	public void setMasterRef(long masterRef) {
		this.masterRef = masterRef;
	}

	public FocObject getMaster() {
		return master;
	}

	public void setMaster(FocObject master) {
		this.master = master;
	}

	public boolean isMasterOwner() {
		return masterOwner;
	}

	public void setMasterOwner(boolean masterOwner) {
		this.masterOwner = masterOwner;
	}
	
	public void applyFiltersToList(FocList list) {
		if (getRequest() != null && list != null) {
			FocDesc focDesc = list.getFocDesc();
			if (focDesc != null) {
				Enumeration<String> paramsEnum = getRequest().getParameterNames();
				
				if (paramsEnum != null) {
					while(paramsEnum.hasMoreElements()) {
						String name = paramsEnum.nextElement();
						if(name.startsWith("filter_")) {
							String fieldName = name.substring("filter_".length());
							FField field = focDesc.getFieldByName(fieldName);
							if (field != null) {
								if (list.getFilter() != null) {

									if(field instanceof FStringField) {
										String fieldNameSQL = DBManager.provider_ConvertFieldName(focDesc.getProvider(), fieldName);
										String value = getRequest().getParameter(name);
										list.getFilter().putAdditionalWhere("PARAM_FILTER"+fieldName, fieldNameSQL+"='"+value+"'");
									} else if(field instanceof FObjectField) {
										String fieldNameSQL = fieldName+"_"+FField.REF_FIELD_NAME;
										fieldNameSQL = DBManager.provider_ConvertFieldName(focDesc.getProvider(), fieldNameSQL);
										String value = getRequest().getParameter(name);
										long ref = Utils.parseLong(value, -1);
										list.getFilter().putAdditionalWhere("PARAM_FILTER"+fieldName, fieldNameSQL+"="+ref+"");
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
