package com.foc.modules.link.xmlHanlder.admin;

import com.foc.modules.link.AbstractSpecificHandler;
import com.foc.modules.link.FocLinkHandlerCreator;


public class FocLinkHandlerCreator_Admin_InitEAccount implements FocLinkHandlerCreator {

	public AbstractSpecificHandler newFocLinkHandler() {
		FocLinkHandler_Admin_InitEAccount opretationHandler = new FocLinkHandler_Admin_InitEAccount();		
		
		return opretationHandler;
	}
}
