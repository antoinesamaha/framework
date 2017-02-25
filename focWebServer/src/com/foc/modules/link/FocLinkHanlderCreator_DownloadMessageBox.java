package com.foc.modules.link;


public class FocLinkHanlderCreator_DownloadMessageBox implements FocLinkHandlerCreator {

	public AbstractSpecificHandler newFocLinkHandler() {
		FocLinkHandler_DownloadMessageBox downloadHandler = new FocLinkHandler_DownloadMessageBox();
		return downloadHandler;
	}

}
