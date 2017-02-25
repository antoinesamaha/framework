package com.foc.shared.json;

import java.util.ArrayList;

public class B01JsonBuilder {
	private StringBuffer buffer = null;
	
	private ArrayList<Boolean> firstIndicator = new ArrayList<Boolean>();
	
	public B01JsonBuilder(){
		buffer = new StringBuffer();
	}
	
	public B01JsonBuilder(StringBuffer buffer){
		this.buffer = buffer;
	}
	
	public boolean firstIndicator_IsCurrentFirst(){
		return firstIndicator.get(firstIndicator.size()-1);
	}
	
	public boolean firstIndicator_SetCurrentFirst(boolean first){
		return firstIndicator.set(firstIndicator.size()-1, first);
	}

	public boolean firstIndicator_AddLevel(){
		return firstIndicator.add(true);
	}

	public boolean firstIndicator_RemoveLevel(){
		return firstIndicator.remove(firstIndicator.size()-1);
	}

	public void beginObject_InValue(){
		append("{");
		firstIndicator_AddLevel();
	}

	public void beginObject(){
		if(!firstIndicator_IsCurrentFirst()){
			append(",");
		}else{
			firstIndicator_SetCurrentFirst(false);
		}
		append("{");
		firstIndicator_AddLevel();
	}

	public void endObject(){
		append("}");
		firstIndicator_RemoveLevel();
	}

	public void beginList(){
		append("[");
		firstIndicator_AddLevel();
	}

	public void endList(){
		append("]");
		firstIndicator_RemoveLevel();
	}

	public void append(String str){
		if(buffer != null) buffer.append(str);
	}

	public void appendKey(String key){
		if(buffer != null){
			if(!firstIndicator_IsCurrentFirst()){
				append(",");
			}else{
				firstIndicator_SetCurrentFirst(false);
			}
			buffer.append("\"");
			buffer.append(key);
			buffer.append("\":");
		}
	}

	public void appendValue(String value){
		if(buffer != null){
			buffer.append("\"");
			buffer.append(value);
			buffer.append("\"");
		}
	}

	public void appendKeyValue(String key, int value){
		if(buffer != null){
			appendKey(key);
			appendValue(String.valueOf(value));
		}
	}
		
	public void appendKeyValue(String key, double value){
		if(buffer != null){
			appendKey(key);
			appendValue(String.valueOf(value));
		}
	}
	
	public void appendKeyValue(String key, String value){
		if(buffer != null){
			appendKey(key);
			appendValue(value);
		}
	}
	
	@Override
	public String toString(){
		return buffer != null ? buffer.toString() : "";
	}
}
