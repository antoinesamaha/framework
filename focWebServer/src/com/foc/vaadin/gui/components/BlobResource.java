package com.foc.vaadin.gui.components;

import java.io.File;
import java.io.InputStream;

import com.foc.Globals;
import com.foc.property.FCloudStorageProperty;
import com.foc.util.Utils;
import com.vaadin.server.ConnectorResource;
import com.vaadin.server.DownloadStream;
import com.vaadin.server.Page;

@SuppressWarnings("serial")
//public class BlobResource extends FileResource{
public class BlobResource implements ConnectorResource {

	private InputStream is       = null;
  private String      fileName = null;
  
  public BlobResource(File sourceFile, InputStream is, String fileName){
//  	super(sourceFile);
  	fileName = fileName.replaceAll(" ", "_");
  	if(fileName == null){
  		fileName = "Everpro";
  	}
  	this.fileName = fileName;
  	this.is = is;
  }
  
  @Override
  public DownloadStream getStream() {
    DownloadStream downloadStream = null;
    
    if(is != null){
      downloadStream = new DownloadStream(is, "application/x-unknown", fileName);
      downloadStream.setParameter("Content-Disposition", "attachment; filename=" + fileName);
      downloadStream.setCacheTime(0);  
    }
    
    return downloadStream;
  }
  
	public void openDownloadWindow(){
//		setCacheTime(0);
		Page.getCurrent().open(this, null, true);
	}
  
  public static BlobResource newBlobResource(FCloudStorageProperty cloudStorageProperty){
		if(Utils.isStringEmpty(cloudStorageProperty.getKey())){
			cloudStorageProperty.generateKey();
		}

		if(cloudStorageProperty.getDirectory() == null){
			cloudStorageProperty.setDirectory(Globals.getApp().getCloudStorageDirectory(), false);
		}

		InputStream is = (InputStream) cloudStorageProperty.getObject();

		File file = new File(cloudStorageProperty.getFileName());
		BlobResource resource = new BlobResource(file, is, cloudStorageProperty.getFileName());
		return resource;
  }

	@Override
	public String getMIMEType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFilename() {
		return this.fileName;
	}
}