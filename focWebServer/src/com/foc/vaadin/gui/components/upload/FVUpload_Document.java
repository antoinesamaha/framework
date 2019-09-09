/*******************************************************************************
 * Copyright 2016 Antoine Nicolas SAMAHA
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.foc.vaadin.gui.components.upload;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.foc.Globals;
import com.foc.IFocEnvironment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;

@Deprecated
@SuppressWarnings("serial")
public class FVUpload_Document extends CustomComponent implements Upload.SucceededListener, Upload.FailedListener, Upload.Receiver {

	private VerticalLayout        root                  = null;
	private Upload                upload                = null;
	private String                fileName              = null;
	private ByteArrayOutputStream byteArrayOutputStream = null;
	
	private FVDocumentReceiver       documentReceiver         = null;
	
	public FVUpload_Document() {
		root = new VerticalLayout();
		root.setSpacing(false);
		root.setMargin(false);
		setCompositionRoot(root);

		// Create the Upload component.
		upload = new Upload(null, this);
		upload.setImmediate(true);
		
		upload.setButtonCaption("Upload");

		upload.addSucceededListener((Upload.SucceededListener) this);
		upload.addFailedListener((Upload.FailedListener) this);

		root.addComponent(upload);
	}

	public void dispose(){
		root          = null;
		upload        = null;
		documentReceiver = null;
	}

	public FVDocumentReceiver getDocumentReceiver() {
		return documentReceiver;
	}

	public void setImageReceiver(FVDocumentReceiver documentReceiver) {
		this.documentReceiver = documentReceiver;
	}

	@Override
	public OutputStream receiveUpload(String fileName, String MIMEType) {
		Globals.logString("Receiving: " + fileName);
		this.fileName = fileName;
		byteArrayOutputStream = new ByteArrayOutputStream();
		return byteArrayOutputStream;
	}
	
	// This is called if the upload is finished.
	public void uploadSucceeded(Upload.SucceededEvent event) {
		root.addComponent(new Label("File " + event.getFilename() + " of type '" + event.getMIMEType() + "' uploaded."));
		long uploadSize = event.getLength();
		
		if(uploadSize < 1048576){
			Globals.logString("Received: " + fileName);
			try{
				byte[] documentInByte = byteArrayOutputStream.toByteArray();
				byteArrayOutputStream.close();
		
				InputStream in = new ByteArrayInputStream(documentInByte);
				
				if(documentReceiver != null){
					documentReceiver.documentReceived(event, in);
				}
				
			}catch(Exception e){
				Globals.logException(e);
			}
		}else{
			Globals.showNotification("Upload Failed", "Attachment file size accessed the allowed size", IFocEnvironment.TYPE_ERROR_MESSAGE);
		}
	}

	// This is called if the upload fails.
	public void uploadFailed(Upload.FailedEvent event) {
		root.addComponent(new Label("Uploading " + event.getFilename() + " of type '" + event.getMIMEType() + "' failed."));
	}
}
