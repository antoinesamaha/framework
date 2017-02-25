package com.foc.shared.json;

public class B01JsonSaxParser {
	
	private String                       str2Parse = null;
	private B01JsonSaxCallBack_Interface callBack  = null;
	
	private boolean      insideAString = false;
	private boolean      insideAValue  = false;
	private StringBuffer currentString = new StringBuffer();
	
	private String  lastKey                       = null;
	private String  lastValue                     = null;
	private String  objectKey                     = null;
	private boolean isWaitingForFirstKeyValuePair = false;
	
	public B01JsonSaxParser(String str2Parse, B01JsonSaxCallBack_Interface callBack){
		this.str2Parse = str2Parse;
		this.callBack  = callBack;
	}
	
	public void dispose(){
		str2Parse = null;
		callBack  = null;
	}
	
	public void parse(){
		insideAString = false;
		insideAValue  = false;
		currentString = new StringBuffer();
		//looking for { or [
		for(int i=0; i<str2Parse.length(); i++){
			char c = str2Parse.charAt(i);
			
			if(!insideAString){
				if(c == '{'){
					callBack.startObject();
					if(lastKey != null && lastValue == null){
						objectKey = lastKey;
					}
					isWaitingForFirstKeyValuePair = true;
				}else if(c == '}'){
					if(insideAValue){
						valueNotification();
					}					
					callBack.endObject();
				}else if(c == '['){
					callBack.startList();
					if(lastKey != null && lastValue == null){
						callBack.newList(lastKey);
					}
				}else if(c == ']'){
					callBack.endList();
				}else if(c == ':'){
					callBack.newKey(currentString);
					insideAValue = true;
					lastKey = currentString.toString();
					currentString = new StringBuffer();
				}else if(c == ','){
					valueNotification();
				}else if(c == '"'){
					insideAString = true;
					callBack.startString();
				}else{
					currentString.append(c);
				}
			}else{
				if(c == '"'){
					insideAString = false;
					callBack.endString(currentString);
				}else{
					currentString.append(c);
				}
			}
		}
	}
	
	private void valueNotification(){
		lastValue = currentString.toString();
		callBack.newValue(currentString);
	
		//Key Value Pair
		//--------------
		if(isWaitingForFirstKeyValuePair){
			callBack.newObject(objectKey, lastKey, lastValue);
			objectKey = null;
		}else if(lastKey != null){
			callBack.newKeyValuePair(lastKey, lastValue);
		}
		isWaitingForFirstKeyValuePair = false;
		//--------------
		
		lastKey   = null;
		lastValue = null;
		
		currentString = new StringBuffer();
		insideAValue = false;
	}
}
