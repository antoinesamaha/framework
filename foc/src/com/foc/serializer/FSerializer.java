package com.foc.serializer;

public interface FSerializer {
	public static final String TYPE_JSON = "JSON";
	public static final String TYPE_HTML = "HTML";
	
	public void serializeToBuffer();
	public int getVersion();
	public void dispose();
}
