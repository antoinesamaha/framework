package com.foc.vaadin.gui.components.upload;

import java.io.InputStream;

import com.vaadin.ui.Upload;

public interface FVDocumentReceiver {
	public void documentReceived(Upload.SucceededEvent event, InputStream document);
}
