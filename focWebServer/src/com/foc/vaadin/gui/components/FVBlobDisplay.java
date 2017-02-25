package com.foc.vaadin.gui.components;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import org.xml.sax.Attributes;

import com.foc.Globals;
import com.foc.IFocEnvironment;
import com.foc.desc.FocObject;
import com.foc.property.FCloudStorageProperty;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.FVIconFactory;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentDelegate;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.components.upload.FVImageReceiver;
import com.foc.vaadin.gui.components.upload.FVUpload_Image;
import com.foc.vaadin.gui.layouts.FVVerticalLayout;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.DownloadStream;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.themes.Reindeer;

@SuppressWarnings("serial")
public class FVBlobDisplay extends FVVerticalLayout implements FocXMLGuiComponent {

	/*
	 * For big files we use: SET GLOBAL max_allowed_packet = 1024*1024*14 (MySQL);
	 */
	private HorizontalLayout settingsLayout     = null;
	private FocXMLGuiComponentDelegate delegate = null;
	private FVUpload_Image uploadImage          = null;
	private Attributes   attributes             = null;
	private IFocData     focData                = null;
	private Button       downloadDocButton      = null;
	private Image image = null;
	
	private final static int MAX_WIDTH  = 500;
	private final static int MAX_HEIGHT = 500;
	
	public FVBlobDisplay() {
		super(null);
	}
	
	public FVBlobDisplay(FocXMLLayout xmlLayout, IFocData focData, Attributes attributes) {
		super(attributes);
		setAttributes(attributes);
		setFocData(focData);
		init();
	}

	private void init(){
		settingsLayout = new HorizontalLayout();
		settingsLayout.setSpacing(true);
		delegate = new FocXMLGuiComponentDelegate(this);
		
		downloadDocButton = new Button("Download Attachment");
		downloadDocButton.setStyleName(Reindeer.BUTTON_LINK);

		downloadDocButton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				downloadAttachedFile();
			}
		});
		settingsLayout.addComponent(downloadDocButton);
		addComponent(getImageField());
		addComponent(settingsLayout);
		displayDocuments();
		String value = getAttributes().getValue(FXML.ATT_WITH_UPLOAD_BUTTON);
		if(value != null && value.equals("true") && getDelegate() != null && getDelegate().isEditable()){
			addComponent(getFvUpload_Image());
//			FVImageReceiver imageReceiver = new FVImageReceiver() {
//				
//				@Override
//				public void imageReceived(SucceededEvent event, final InputStream image) {
//					StreamSource streamSource = new StreamSource() {
//						
//						@Override
//						public InputStream getStream() {
//							return image;
//						}
//					};
//					StreamResource resource = new StreamResource(streamSource, event.getFilename());
//					getImageField().setSource(resource);
//				}
//			};
//			
//			FocObject focObject = getFocData();
//			FVImageField imageField = new FVImageField(property, null);
//			getFvUpload_Image().setImageReceiver(imageReceiver);
		}
	}
	
	private Image getImageField(){
		if(image == null){
			image = new Image();
		}
		return image;
	}
	
	private FVUpload_Image getFvUpload_Image(){
		if(uploadImage == null){
			uploadImage = new FVUpload_Image();
		}
		return uploadImage;
	}
	
	public void setImageReceiver(FVImageReceiver imageReceiver){
		if(getFvUpload_Image() != null){
			getFvUpload_Image().setImageReceiver(imageReceiver);
		}
	}
	
	@Override
	public void dispose() {
		if(delegate != null){
			delegate.dispose();
			delegate = null;
		}
		if(uploadImage != null){
			uploadImage.dispose();
			uploadImage = null;
		}
		attributes = null;
		settingsLayout = null;
	}

	@Override
	public IFocData getFocData() {
		return focData;
	}

	@Override
	public void setFocData(IFocData focData) {
		this.focData = focData;
	}

	@Override
	public String getXMLType() {
		return FXML.TAG_BLOB_DISPLAY;
	}

	@Override
	public Field getFormField() {
		return null;
	}

	@Override
	public boolean copyGuiToMemory() {
		return false;
	}

	@Override
	public void copyMemoryToGui() {
	}

	@Override
	public Attributes getAttributes() {
		return attributes;
	}

	@Override
	public void setAttributes(Attributes attributes) {
		this.attributes = attributes;
    FocXMLGuiComponentStatic.applyAttributes(this, attributes);
	}

	@Override
	public String getValueString() {
		return null;
	}

	@Override
	public void setValueString(String value) {
	}

	@Override
	public void setDelegate(FocXMLGuiComponentDelegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public FocXMLGuiComponentDelegate getDelegate() {
		return delegate;
	}
	
	private BlobResource getBlobResource(){
		BlobResource resource = null;
		Attributes attributes = getAttributes();
		if(attributes != null && getFocData() != null && getFocData() instanceof FCloudStorageProperty){
			FCloudStorageProperty cloudStorageProperty = (FCloudStorageProperty) getFocData();


			FocObject focObject = cloudStorageProperty.getFocObject();
			if(focObject != null){
				resource = BlobResource.newBlobResource(cloudStorageProperty);
				/*
				if(Utils.isStringEmpty(cloudStorageProperty.getKey())){
					cloudStorageProperty.generateKey();
				}

				if(cloudStorageProperty.getDirectory() == null){
					cloudStorageProperty.setDirectory(Globals.getApp().getCloudStorageDirectory(), false);
				}

				InputStream is = (InputStream) cloudStorageProperty.getObject();

				File file = new File(cloudStorageProperty.getFileName());
				resource = new BlobResource(this, file, is, cloudStorageProperty.getFileName());
				*/
			}
		}
		return resource;
	}
	
	public void downloadAttachedFile(String fileName, ByteArrayInputStream bais){
		BlobResource blobResource = new BlobResource(new File(""), bais, fileName);
		blobResource.openDownloadWindow();
	}

	private void downloadAttachedFile(){
		BlobResource blobResource = getBlobResource();
		if(blobResource != null){
			blobResource.openDownloadWindow();
		}else{
			Globals.showNotification("Download Corrupted", "Request failed please try again later.", IFocEnvironment.TYPE_ERROR_MESSAGE);
		}
	}

	/*
	private static void openDownloadWindow(BlobResource blobResource){
		if(blobResource != null){
			blobResource.openDownloadWindow();
		}else{
			Globals.showNotification("Download Corrupted", "Request failed please try again later.", IFocEnvironment.TYPE_ERROR_MESSAGE);
		}
	}
	*/
	
	public void displayPDFFile(){
		BlobResource blobResource = getBlobResource();
		if(blobResource != null){
			String fileName = blobResource.getFilename();
			if(fileName != null){
				String mimeType = blobResource.getMIMEType();

				StreamSource streamSource = new StreamSource() {
					private DownloadStream downloadStream = getBlobResource() != null ? getBlobResource().getStream() : null;
					@Override
					public InputStream getStream() {
						
						return downloadStream != null ? downloadStream.getStream() : null;
					}
				};
				String viewButtonCaption = "View " + fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()) + " File";
				Button viewDocButton = new Button(viewButtonCaption);
				viewDocButton.setStyleName(Reindeer.BUTTON_LINK);
				if(streamSource != null){
					StreamResource streamResource = new StreamResource(streamSource, fileName);
					streamResource.setMIMEType(mimeType);
	
					BrowserWindowOpener opener = new BrowserWindowOpener(streamResource);
					opener.extend(viewDocButton);
					settingsLayout.addComponent(viewDocButton);
					Resource resource = FVIconFactory.getInstance().getFVIcon(FVIconFactory.ICON_NOTE);
					Image image = new Image();
					image.setSource(resource);
					addComponent(image);
				}
			}
		}
	}
	
	private Image displayImage(BlobResource blobResource){
		Image image = new Image();
		image.setSource(blobResource);
		FVImageField imageField = new FVImageField(null, getAttributes());
		image = imageField.resizeImage(image, MAX_WIDTH, MAX_HEIGHT);
		
		setWidth(image.getWidth()+"px");
    setHeight(image.getHeight()+"px");
    
		return image;
	}
	
	private void displayDocuments(){
		BlobResource blobResource = getBlobResource();
		String mimeType = blobResource != null ? blobResource.getMIMEType() : null;
		if(mimeType != null && !mimeType.isEmpty()){
			if(mimeType.contains("application/")){
				displayPDFFile();
			}else if(mimeType.contains("audio/")){
				Globals.showNotification("Audio are not allowed to be uploaded.", "", IFocEnvironment.TYPE_WARNING_MESSAGE);
			}else if(mimeType.contains("image/")){
				Image image = displayImage(blobResource);
				addComponent(image);
			}else if(mimeType.contains("text/")){
				displayPDFFile();
			}else if(mimeType.contains("video/")){
				Globals.showNotification("Videos are not allowed to be uploaded.", "", IFocEnvironment.TYPE_WARNING_MESSAGE);
			}else if(mimeType.contains("x-world/")){
				
			}
		}
	}
}
