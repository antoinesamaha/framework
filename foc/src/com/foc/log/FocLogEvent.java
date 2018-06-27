package com.foc.log;

import java.util.Date;

public interface FocLogEvent {
	//Entity identification
	public String logEvent_GetEntityName();
	public long   logEvent_GetEntityReference();
	
	//Transaction identification (applicable when the Entity is a transaction) 
	public String logEvent_GetEntityCompanyName();
	public String logEvent_GetEntitySiteName();
	public String logEvent_GetEntityCode();
	public Date   logEvent_GetEntityDate();
	
	//Event Info
	public int    logEvent_GetEventType();
	public String logEvent_GetUsername();
	public Date   logEvent_GetDateTime();
	public String logEvent_GetSQLRequests();
}
