package com.foc.log;

import java.util.ArrayList;

public interface FocLogListener {
	public void addLogEvent(FocLogEvent event);
	public void getLastHash(ArrayList<HashedDocument> documents, IFocLogLastHash lastHash);
}