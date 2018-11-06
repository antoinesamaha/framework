package com.foc.log;

public interface FocLogListener {
	public void addLogEvent(FocLogEvent event);
	public void getLastHash(String entityName, long entityReference, IFocLogLastHash lastHash);
}