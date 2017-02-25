package com.foc.shared.json;

public interface B01JsonSaxCallBack_Interface {
	//These are low level functions they can be left unimplemented
	public void startObject();
	public void startList();
	public void endObject();
	public void endList();
	public void startString();
	public void endString(StringBuffer strBuff);

	//These are low level functions better use newKeyValuePair method
	public void newKey(StringBuffer key);
	public void newValue(StringBuffer value);
	
	//For an easier SAX implement these functions
	public void newKeyValuePair(String key, String value);
	public void newObject(String objectKey, String key, String reference);
	public void newList(String key);
	
	public void dispose();
}
