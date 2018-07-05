package com.foc.vaadin.gui.components;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.imageio.ImageIO;

import org.xml.sax.Attributes;

import com.foc.Globals;
import com.foc.desc.FocObject;
import com.foc.property.FBlobMediumProperty;
import com.foc.property.FCloudStorageProperty;
import com.foc.property.FImageProperty;
import com.foc.property.FProperty;
import com.foc.util.Utils;
import com.foc.vaadin.FocWebApplication;
import com.foc.vaadin.gui.FVIconFactory;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.components.upload.FVImageReceiver;
import com.foc.vaadin.gui.components.upload.FVUpload_Image;
import com.foc.vaadin.gui.layouts.FVVerticalLayout;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.vaadin.server.ConnectorResource;
import com.vaadin.server.DownloadStream;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.util.FileTypeResolver;

@SuppressWarnings("serial")
public class FVImageField extends FVVerticalLayout implements FocXMLGuiComponent, FVImageReceiver {

	private Image                 embedded             = null;
	
	private FImageProperty        imageProperty        = null;
	private FCloudStorageProperty cloudStorageProperty = null;
	private FBlobMediumProperty   blobMediumProperty   = null;
	private GenericFileResource   resource             = null;
	
	private boolean               isImage            = false;
	private boolean               editable           = true;
	private double                originalWidth      =   -1;
  private double                originalHeight     =   -1;
	private HorizontalLayout      imageControlLayout ;//Should not be set =null because the super call is setting them and the init after the super call can put them to null 
	private Attributes            attributes         = null;
	private Button                downloadButton     = null;
	
	public FVImageField(FProperty property, Attributes attributes){
		super();
		setSpacing(true);
		setAttributes(attributes);
		addStyleName("foc-ImageField");

		setProperty(property);
		
//		setSpacing(false);
	}
	
	public void dispose(){
		super.dispose();
		imageProperty = null;
		cloudStorageProperty = null;
		blobMediumProperty = null;
		if(downloadButton != null){
//			downloadButton.dispose();
			downloadButton = null;
		}
		attributes = null;
	}
	
  public void setAttributes(Attributes attributes) {
  	if(attributes != null){
  	  this.attributes = attributes;
  	  String width  = attributes.getValue(FXML.ATT_WIDTH);
  	  String height = attributes.getValue(FXML.ATT_HEIGHT);
  	  if(getEmbedded() != null && width != null && height != null){
        setWidth(width);
        setHeight(height);

  	    try{
    	    width = width.substring(0, width.indexOf("px"));
          height = height.substring(0, height.indexOf("px"));
          int widthInt  = Integer.valueOf(width);
          int heightInt = Integer.valueOf(height);
          
    	    double ratio = getRatio(getEmbedded(), widthInt, heightInt);
    	    
    	    int newWidthInt  = (int)(originalWidth * ratio);
    	    int newHeightInt = (int)(originalHeight * ratio);
    	    
    	    String newWidth  = Integer.toString(newWidthInt);
    	    String newHeight = Integer.toString(newHeightInt);
    	    
      	  getEmbedded().setWidth(newWidth+"px");
      	  getEmbedded().setHeight(newHeight+"px");
      	  
      	  int fieldWidth = newWidthInt;
      	  int fieldHeight = newHeightInt;
      	  if(isEditable()) {
      	  	fieldHeight += 50;//To keep some space for the buttons
      	  }
      	  setWidth(Integer.toString(fieldWidth)+"px");
      	  setHeight(Integer.toString(fieldHeight)+"px");
  	    }catch(Exception e){
  	      Globals.logException(e);
  	    }
  	  }
  	}
  	FocXMLGuiComponentStatic.setCaptionMargin_Zero(this);
  	if(getDelegate() != null){
  		setEditable(getDelegate().isEditable());
  	}
  }
  
  public Image resizeImage(Image image, int maxWidth, int maxHeight){
  	if(image != null){
  		
	  	double ratio = getRatio(image, maxWidth, maxHeight);
			String newHeight = Integer.toString((int) (image.getHeight() * ratio));

			String newWidth  = Integer.toString((int) (image.getWidth() * ratio));
	    
	    image.setWidth(newWidth+"px");
	    image.setHeight(newHeight+"px");
	    
	    setWidth(newWidth+"px");
	    setHeight(newHeight+"px");
  	}
    return image;
  }

  private double getRatio(Image embedded, int maxWidth, int maxHeight){
    double ratio = 1;
    double ratioWidth  = 0;
    double ratioHeight = 0;
    
    if(originalWidth != 0){
    	ratioWidth = maxWidth / originalWidth;
    }
    
    if(originalHeight != 0){
    	ratioHeight = maxHeight / originalHeight;
    }
    
    if(ratioWidth != 0 && ratioHeight != 0){
	    ratio = ratioHeight;
	    if(ratioWidth < ratio) ratio = ratioWidth;
	
	    if(ratio > 1) ratio = 1;//Do not expand
    }
    
    return ratio;
  }
  
  public Attributes getAttributes(){
    return attributes;
  }
  
  private void setEmbedded(Image embedded){
    this.embedded = embedded;
  }
  
  public Image getEmbedded(){
    return embedded;
  }
  
	public void setProperty(FProperty property){
		if(property != null && property.getFocField() != null){
			if(property instanceof FCloudStorageProperty){
				setCloudStorageProperty((FCloudStorageProperty) property);
				if(downloadButton != null) downloadButton.setVisible(true);
			}else if(property instanceof FImageProperty){
				this.setImageProperty((FImageProperty) property);
				if(downloadButton != null) downloadButton.setVisible(true);
			}else if(property instanceof FBlobMediumProperty){
				this.setBlobMediumProperty((FBlobMediumProperty) property);
				if(downloadButton != null) downloadButton.setVisible(false);				
			}	
		}
		if(resource != null){
			resource.setProperty(property);
		}
		
		embedded = new Image();
		embedded.setImmediate(false);
		setEmbedded(embedded);
				
		boolean error = refreshImageFromProperty();
		setIsImage(!error);
		
		addComponentAsFirst(embedded);
		setExpandRatio(embedded, 1);
		reactToEditable();
	}
	
	public BufferedImage getBufferedImage(){
		BufferedImage result = null;
		if(getCloudStorageProperty() != null){
			result = getCloudStorageProperty().getBufferedImage();
		}else if(getImageProperty() != null){
			result = getImageProperty().getImageValue();
		}
		return result;
	}
	
	@Override
	public String getXMLType() {
		return FXML.TAG_FIELD;
	}

	@Override
	public Field getFormField() {
		return null;
	}

	@Override
  public FProperty getFocData() {
		FProperty prop = null;
		if(getCloudStorageProperty() != null){
			prop = getCloudStorageProperty();
		}else if(getImageProperty() != null){
			prop = getImageProperty();
		}else if(getBlobMediumProperty() != null){
			prop = getBlobMediumProperty();			
		}
		
		return prop;
  }
	
	@Override
	public void imageReceived(SucceededEvent event, InputStream image) {
		if(cloudStorageProperty != null){
			if(event != null && !Utils.isStringEmpty(event.getFilename())) {
				cloudStorageProperty.setFileNameInProperty(event.getFilename());
				cloudStorageProperty.generateKey();
			}
			cloudStorageProperty.setObject(image);
		}else{
			BufferedImage bufferedImage = null;
			try {
				bufferedImage = ImageIO.read(image);
			} catch (IOException e) {
				e.printStackTrace();
			}
			//This is because 
			if(imageProperty != null){
				FocObject focObject = imageProperty.getFocObject();
				if(focObject != null && focObject.isCreated()){
					focObject.validate(true);
				}
				//BAntoineS-20151110
				imageProperty.setImageValue(bufferedImage);
				//EAntoineS-20151110
			}
		}		
		refreshImageFromProperty();
	}
	
	public boolean refreshImageFromProperty(){
		BufferedImage photo = getBufferedImage();
		boolean error = photo == null;

		if(!error){
			originalHeight = photo.getHeight();
			originalWidth  = photo.getWidth();
			setAttributes(getAttributes());
			StreamResource.StreamSource streamSource = new StreamResource.StreamSource(){

				public InputStream getStream() {
					try {
						/* Write the image to a buffer. */
						ByteArrayOutputStream imagebuffer = new ByteArrayOutputStream();
						ImageIO.write(getBufferedImage()/*Globals.getApp().getCompany().getLogo()*/, "png", imagebuffer);
						/* Return a stream from the buffer. */
						return new ByteArrayInputStream(imagebuffer.toByteArray());
					} catch (IOException e) {
						Globals.logException(e);
						return null;
					}
				}

			};
			UI application = FocWebApplication.getInstanceForThread();
			if(application != null){
				StreamResource streamResource = new StreamResource(streamSource, "");
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
				String timestamp = df.format(new Date(System.currentTimeMillis()));
				streamResource.setFilename("FileName-"+timestamp+".jpg");

				embedded.setSource(streamResource);
			}
		}
		return error;
	}
	
	public Button getDownloadButton() {
		return downloadButton;
	}
	
	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
		reactToEditable();
	}

  @Override
  public boolean copyGuiToMemory() {
  	return false;
  }

  @Override
  public void copyMemoryToGui() {
  }

  public void reactToEditable(){
		if(isEditable()){
			if(imageControlLayout != null){
				imageControlLayout.setVisible(true);
			}else{
			  imageControlLayout = new HorizontalLayout();
			  imageControlLayout.setSpacing(true);
			  imageControlLayout.setWidth("100%");
			  imageControlLayout.setHeight("-1px");
		    addComponent(imageControlLayout);
		    setComponentAlignment(imageControlLayout, Alignment.TOP_RIGHT);

			  if(isEditable()){
			    FVUpload_Image uploader = new FVUpload_Image();
			    uploader.setWidth("100px");
			    imageControlLayout.addComponent(uploader);
			    imageControlLayout.setComponentAlignment(uploader, Alignment.TOP_RIGHT);
			    
			    uploader.setImageReceiver(this);
			    
			    downloadButton = new Button("Download");//, FVIconFactory.getInstance().getFVIcon_24(FVIconFactory.ICON_DOWNLOAD));
			    downloadButton.setWidth("100px");
//			    downloadButton.setCaption(null);
//			    downloadButton.setStyleName(BaseTheme.BUTTON_LINK);
			    downloadButton.addStyleName(FocXMLGuiComponentStatic.STYLE_NO_PRINT);
			    downloadButton.addStyleName(FocXMLGuiComponentStatic.STYLE_HAND_POINTER_ON_HOVER);
			    imageControlLayout.addComponent(downloadButton);
			    imageControlLayout.setComponentAlignment(downloadButton, Alignment.TOP_LEFT);
		  							
					resource = new GenericFileResource(getFocData());
					FileDownloader downloader = new FileDownloader(resource);
					downloader.extend(downloadButton);
				}
			}
		}else{
			if(imageControlLayout != null) imageControlLayout.setVisible(false);
		}
  }

	public FCloudStorageProperty getCloudStorageProperty() {
		return cloudStorageProperty;
	}

	public void setCloudStorageProperty(FCloudStorageProperty cloudStorageProperty) {
		this.cloudStorageProperty = cloudStorageProperty;
	}
	
	public FImageProperty getImageProperty() {
		return imageProperty;
	}

	public void setImageProperty(FImageProperty imageProperty) {
		this.imageProperty = imageProperty;
	}

	public FBlobMediumProperty getBlobMediumProperty() {
		return blobMediumProperty;
	}
	
	public void setBlobMediumProperty(FBlobMediumProperty blobMediumProperty) {
		this.blobMediumProperty = blobMediumProperty;
	}
	
	public boolean isImage() {
		return isImage;
	}

	public void setIsImage(boolean isImage) {
		this.isImage = isImage;
	}
	
	public Resource getResourceAndSetIcon() {
		Resource resource = null;
		if(getCloudStorageProperty().getFileName().endsWith(".xls") || getCloudStorageProperty().getFileName().endsWith(".xlsx") || getCloudStorageProperty().getFileName().endsWith(".csv")){
			resource = FVIconFactory.getInstance().getFVIcon_24(FVIconFactory.ICON_EXCEL);
		}else if(getCloudStorageProperty().getFileName().endsWith(".doc") || getCloudStorageProperty().getFileName().endsWith(".docx")){
			resource = FVIconFactory.getInstance().getFVIcon_24(FVIconFactory.ICON_WORD);
		}else if(getCloudStorageProperty().getFileName().endsWith(".ppt") || getCloudStorageProperty().getFileName().endsWith(".pptx")){
			resource = FVIconFactory.getInstance().getFVIcon_24(FVIconFactory.ICON_PPT);
		}else{
			resource = FVIconFactory.getInstance().getFVIcon(FVIconFactory.ICON_NOTE);
		}
		return resource;
	}
	
	public class GenericFileResource implements ConnectorResource {//FileResource {

		private FProperty property = null;
		
//    private ByteArrayInputStream bais         = null;
//    private InputStream          inputStream  = null;
//    private String               fileName     = null;
    
//    public GenericFileResource(InputStream inputStream, String fileName){
//    	this.inputStream = inputStream;
//    	this.fileName = fileName;
//    }
    
    public GenericFileResource(FProperty property){
    	this.property = property;
    }
    
    public void dispose(){
    	property = null;
//    	if(bais != null){
//    		try{
//					bais.close();
//				}catch (IOException e){
//					Globals.logException(e);
//				}
//    		bais =  null;	
//    	}
    }

    @Override
    public DownloadStream getStream() {
      DownloadStream downloadStream = null;
      
      ByteArrayInputStream bais = null;
      
			Object value = property.getValue();
			
			if(value instanceof InputStream){
				InputStream inputStream = (InputStream) value;
	      if(inputStream instanceof FileInputStream){
		      try{
			      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			      int byt = 0;		      
			      while((byt = inputStream.read()) != -1){
			      	byteArrayOutputStream.write(byt);
			      }
			      inputStream.close();
			      bais = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
		      }catch(Exception ex){
		      	ex.printStackTrace();
		      }
	      }else{
	      	bais = (ByteArrayInputStream) inputStream;
	      }
				
			}else if(value instanceof BufferedImage){
				
				ByteArrayOutputStream imagebuffer = new ByteArrayOutputStream();
				try{
					ImageIO.write((BufferedImage) value, "png", imagebuffer);
				}catch (IOException e){
					Globals.logExceptionWithoutPopup(e);
				}
				/* Return a stream from the buffer. */
				bais = new ByteArrayInputStream(imagebuffer.toByteArray());
				
//			}else if(getBlobMediumProperty() != null){
//				inputStream = (InputStream) getBlobMediumProperty().getValue();
			}
      
      if(bais != null){
      	String fileName = getFilename();
        downloadStream = new DownloadStream(bais, "application/x-unknown", fileName);
        downloadStream.setParameter("Content-Disposition", "attachment; filename=" + fileName);
        downloadStream.setCacheTime(0);
      }
      return downloadStream;
    }

		@Override
		public String getMIMEType() {
			return FileTypeResolver.getMIMEType(getFilename());
		}

		@Override
		public String getFilename() {
			String fileName = "";
			if(property != null){
				if(property instanceof FCloudStorageProperty){
					fileName = ((FCloudStorageProperty) property).getFileName();
				}else if(property instanceof FImageProperty){
					fileName = "Image.png";
				}
			}
			return fileName;
		}

		public FProperty getProperty() {
			return property;
		}

		public void setProperty(FProperty property) {
			this.property = property;
		}
  }
}
