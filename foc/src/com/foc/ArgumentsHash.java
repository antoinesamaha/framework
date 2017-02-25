package com.foc;

import java.util.HashMap;

public class ArgumentsHash {
	private HashMap<String, String> argsHash = null;
	
	public ArgumentsHash(String[] args){
		argsHash = new HashMap<String, String>();

		if(args != null){
			for(int i=0; i<args.length; i++){
				String name  = null;
				String value = null;
				if (args[i] != null && args[i].startsWith("/")){
					String fullNoSlash  = args[i].substring(1);
					int separatorIndex = fullNoSlash.indexOf(':');
					if(separatorIndex < 0){
						name = fullNoSlash;
					}else{
						name  = fullNoSlash.substring(0, separatorIndex);
						if(separatorIndex+1 < fullNoSlash.length()){
							value = fullNoSlash.substring(separatorIndex+1);
						}
					}
				}
	    	if(value != null && name != null && name.trim().compareTo("") != 0 && value.trim().compareTo("") != 0){
	    		argsHash.put(name.toUpperCase(), value);
	    	}
	  		name  = null;
	  		value = null;
			}
		}
	}
	
	public String get(String key){
		return (String)argsHash.get(key.toUpperCase());
	}
}
