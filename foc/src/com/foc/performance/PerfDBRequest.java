package com.foc.performance;

public class PerfDBRequest {
	private String request = null;
	private long   time    = 0;
	
	public PerfDBRequest(String request, long time){
		this.request = request;
		this.time    = time;
	}
	
	public void dispose(){
		request = null;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
}
