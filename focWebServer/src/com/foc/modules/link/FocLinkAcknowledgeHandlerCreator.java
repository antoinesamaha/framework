package com.foc.modules.link;


public class FocLinkAcknowledgeHandlerCreator implements FocLinkHandlerCreator {

	public AbstractSpecificHandler newFocLinkHandler() {
		FocLinkAcknowledgeHandler ackHandler = new FocLinkAcknowledgeHandler();
		
		return ackHandler;
	}

}
