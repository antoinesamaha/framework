package com.foc.vaadin.gui.components.upload;

import java.io.InputStream;

import com.vaadin.ui.Upload;

public interface FVImageReceiver {
	public void imageReceived(Upload.SucceededEvent event, InputStream image);
}
