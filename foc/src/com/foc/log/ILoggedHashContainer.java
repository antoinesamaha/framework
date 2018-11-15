package com.foc.log;

public interface ILoggedHashContainer {
	public int    size();
	
	public String getEntityName(int at);
	public long   getEntityReference(int at);
	
	public void   setStoredHash(int at, String hash);
	public void   setStoredJSON(int at, String json);
	public void   setStoredVersion(int at, int version);
	
	public void   done();
}
