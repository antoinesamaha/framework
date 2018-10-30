package com.foc.log;

public interface FocLogListener {
	public void addLogEvent(FocLogEvent event);
	public HashedDocument getLastHash(String entityName, long entityReference);
}