package com.foc.forecast;

public class ForecastTitle {
	private String lowTitle  = null;
	private String highTitle = null;
	
	public ForecastTitle(String dateStringWithDash, char delimiter){
  	int dashIndex = dateStringWithDash.indexOf(delimiter);
  	if(dashIndex > 0){
	  	lowTitle  = dateStringWithDash.substring(0, dashIndex);
	  	highTitle = dateStringWithDash.substring(dashIndex+1, dateStringWithDash.length());
  	}else{
	  	lowTitle  = null;
	  	highTitle = dateStringWithDash;
  	}
	}
	
	public ForecastTitle(String dateStringWithDash){
		this(dateStringWithDash, '-');
	}

	public String getLowTitle() {
		return lowTitle;
	}

	public String getHighTitle() {
		return highTitle;
	}
}
