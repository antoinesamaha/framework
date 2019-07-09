package com.foc.business.workflow.implementation.join;

import java.sql.Date;

import com.foc.business.workflow.WFSite;
import com.foc.business.workflow.implementation.WFLog;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocDesc;
import com.foc.property.FReference;

public class WFLogJoin extends WFLog {

	public WFLogJoin(FocConstructor constr) {
		super(constr);
	}
	
	@Override
	public long logEvent_GetLogEventReference() {
		FReference prop = (FReference) getFocPropertyByName("L-REF");
		return prop != null ? prop.getLong() : 0;
	}
	
	@Override
	public String logEvent_GetEntityName() {
		WFLogJoinDesc joinDesc = (WFLogJoinDesc) getThisFocDesc();
		FocDesc masterFocDesc = joinDesc != null ? joinDesc.getMasterFocDesc() : null;
		String storageName = masterFocDesc != null ? masterFocDesc.getStorageName() : "";
		return storageName;
	}

	@Override
	public long logEvent_GetEntityReference() {
		long ref = 0;
		WFLogJoinDesc joinDesc = (WFLogJoinDesc) getThisFocDesc();
		FocDesc masterFocDesc = joinDesc != null ? joinDesc.getMasterFocDesc() : null;
		if(masterFocDesc != null) {
			FReference prop = (FReference) getFocPropertyByName("T-"+masterFocDesc.getRefFieldName());
			ref = prop != null ? prop.getLong() : 0;
		}
		return ref;
	}
	
	@Override
	public String logEvent_GetEntityCompanyName() {
		return getCompany() != null ? getCompany().getName() : "";   
	}

	@Override
	public String logEvent_GetEntitySiteName() {
		WFSite site = (WFSite) getPropertyObject("T-SITE");
		String siteName = site != null ? site.getName() : "";   
		return siteName;
	}

	@Override
	public String logEvent_GetEntityCode() {
		String code = getPropertyString("T-CODE");   
		return code;
	}

	@Override
	public Date logEvent_GetEntityDate() {
		return getPropertyDate("T-DATE");   
	}

}
