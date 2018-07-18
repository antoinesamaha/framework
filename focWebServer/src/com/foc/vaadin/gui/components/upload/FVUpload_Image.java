package com.foc.vaadin.gui.components.upload;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.foc.ConfigInfo;
import com.foc.Globals;
import com.foc.IFocEnvironment;
import com.foc.vaadin.gui.FVIconFactory;
import com.vaadin.server.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class FVUpload_Image extends CustomComponent implements Upload.SucceededListener, Upload.FailedListener, Upload.Receiver {

	private int maxSizeAllowed = 6048576;
	
	private VerticalLayout        root                  = null; // Root element for contained components.
	private Upload                upload                = null;
	private String                fileName              = null;
//	private BufferedImage         bufferedImage         = null;
	private ByteArrayOutputStream byteArrayOutputStream = null;
	
	private FVImageReceiver       imageReceiver         = null;
	private Button                uploadButton          = null;                                         
	
	public FVUpload_Image() {
		root = new VerticalLayout();
		root.setSpacing(false);
		root.setMargin(false);
		setCompositionRoot(root);

		// Create the Upload component.
		upload = new Upload(null, this);
		upload.setImmediate(true);
		if(ConfigInfo.isArabic()) {
			upload.setButtonCaption("تحميل");//To hide the default ugly button we should set this to null
		} else {
			upload.setButtonCaption("Upload");//To hide the default ugly button we should set this to null
		}
		
		// Use a custom button caption instead of plain "Upload".
		//upload.setButtonCaption(null);
		/*
		uploadButton = new FVButton("", new Button.ClickListener() {
      public void buttonClick(ClickEvent event) {
      	if(upload != null) upload.startUpload();
      }
    });
		
		uploadButton.setIcon(new ThemeResource("../runo/icons/32/document-add.png"));
		uploadButton.setStyleName(BaseTheme.BUTTON_LINK);
		*/

		// Listen for events regarding the success of upload.
		upload.addSucceededListener((Upload.SucceededListener) this);
		upload.addFailedListener((Upload.FailedListener) this);

		root.addComponent(upload);
		root.setComponentAlignment(upload, Alignment.TOP_RIGHT);
		
//		uploadButton = new Button();
//		uploadButton.setIcon(FVIconFactory.getInstance().getFVIcon_24(FVIconFactory.ICON_UPLOAD));
//		uploadButton.setDescription("Upload");
//		uploadButton.addClickListener(new Button.ClickListener() {
//			@Override
//			public void buttonClick(ClickEvent event) {
//				if(upload != null){
//					upload.submitUpload();
//				}
//			}
//		});
//		root.addComponent(uploadButton);
	}

	public void dispose(){
		root          = null;
		upload        = null;
		imageReceiver = null;
	}

	public FVImageReceiver getImageReceiver() {
		return imageReceiver;
	}

	public void setImageReceiver(FVImageReceiver imageReceiver) {
		this.imageReceiver = imageReceiver;
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
		// Log the upload on screen.
//		root.addComponent(new Label("File " + event.getFilename() + " of type '" + event.getMIMEType() + "' uploaded."));
		long uploadSize = event.getLength();
		
		if(uploadSize < getMaxSizeAllowed()){
			Globals.logString("Received: " + fileName);
			try{
				byte[] imageInByte = byteArrayOutputStream.toByteArray();
				byteArrayOutputStream.close();
		
				// convert byte array back to BufferedImage
				InputStream in = new ByteArrayInputStream(imageInByte);
	//			bufferedImage = ImageIO.read(in);
				
				if(imageReceiver != null){
					imageReceiver.imageReceived(event, in);
				}
				
			}catch(Exception e){
				Globals.logException(e);
			}
		}else{
			Globals.showNotification("Upload Failed", "Attachment file size accessed the allowed size", IFocEnvironment.TYPE_ERROR_MESSAGE);
		}
		// Display the uploaded file in the image panel.
		/*
		final FileResource imageResource = new FileResource(file, getApplication());
		imagePanel.removeAllComponents();
		imagePanel.addComponent(new Embedded("", imageResource));
		*/
	}

	// This is called if the upload fails.
	public void uploadFailed(Upload.FailedEvent event) {
		// Log the failure on screen.
		root.addComponent(new Label("Uploading " + event.getFilename() + " of type '" + event.getMIMEType() + "' failed."));
	}

	public int getMaxSizeAllowed() {
		return maxSizeAllowed;
	}

	public void setMaxSizeAllowed(int maxSizeAllowed) {
		this.maxSizeAllowed = maxSizeAllowed;
	}
}