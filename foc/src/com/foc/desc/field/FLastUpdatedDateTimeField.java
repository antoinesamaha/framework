package com.foc.desc.field;

import com.foc.db.DBManager;

public class FLastUpdatedDateTimeField extends FDateTimeField {
	
	private boolean includeFractionedSeconds = false;

	public FLastUpdatedDateTimeField(String name, String title, int id, boolean key) {
		super(name, title, id, key);
	}

	public FLastUpdatedDateTimeField(String name, String title, int id, boolean key, boolean includeFractionedSeconds) {
		this(name, title, id, key);
		this.includeFractionedSeconds = includeFractionedSeconds;
	}
	
	public boolean includeFractionedSeconds() {
		return includeFractionedSeconds;
	}
	
	public void setIncludeFractionedSeconds(boolean includeFractionedSeconds) {
		this.includeFractionedSeconds = includeFractionedSeconds;
	}
	
	@Override
	public String getCreationString(String name) {
	  	if(getProvider()== DBManager.PROVIDER_ORACLE){
	  		if (includeFractionedSeconds()) {
	  			return " \"" + name + "\" TIMESTAMP DEFAULT SYSTIMESTAMP";
	  		} else {
	  	  		return " \"" + name + "\" DATE";
	  		}
	  	} else if(getProvider()== DBManager.PROVIDER_POSTGRES) {
	  		if (includeFractionedSeconds()) {
	  			return " \"" + name + "\" TIMESTAMP DEFAULT CURRENT_TIMESTAMP";
	  		} else {
	  			return " \"" + name + "\" TIMESTAMP";
	  		}
	  	} else{
	  		return " " + name + " DATETIME";
	  	}
	}
}
