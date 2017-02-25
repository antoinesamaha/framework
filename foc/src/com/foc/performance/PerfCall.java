package com.foc.performance;

public class PerfCall {
	
	long startTime = 0;
	long endTime   = 0;
	
	public PerfCall(){
		startTime = 0;
		endTime   = 0;
	}
	
	public void dispose(){
	}
	
	public void start(){
		startTime = System.currentTimeMillis();
	}
	
	public void end(){
		endTime = System.currentTimeMillis();
	}
}
