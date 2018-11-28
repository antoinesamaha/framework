package com.foc.log;

public interface FocLogListener {
	public void addLogEvent(FocLogEvent event);
//	public void getLastHash(ArrayList<IHashedDocument> documents, IFocLogLastHash lastHash);
	public void getLastHash(ILoggedHashContainer lastHash);
}