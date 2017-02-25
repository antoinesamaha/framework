package com.foc.modules.link;


public class FocLinkHandlerCreator_Check implements FocLinkHandlerCreator {

	
	public AbstractSpecificHandler newFocLinkHandler() {
		FocLinkHandler_Check checkHandler = new FocLinkHandler_Check();

		return checkHandler;
	}

}
