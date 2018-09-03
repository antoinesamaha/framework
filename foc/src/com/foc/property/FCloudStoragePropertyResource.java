package com.foc.property;

import java.io.InputStream;

import com.foc.Globals;
import com.foc.util.Utils;
import com.vaadin.server.ConnectorResource;
import com.vaadin.server.DownloadStream;

@SuppressWarnings("serial")
public class FCloudStoragePropertyResource implements ConnectorResource {

  private FCloudStorageProperty cloudStorageProperty = null;
  
  public FCloudStoragePropertyResource(FCloudStorageProperty property){
  	this.cloudStorageProperty = property;
  }
  
  public void dispose() {
  	cloudStorageProperty = null;
  }
  
  @Override
  public DownloadStream getStream() {
    DownloadStream downloadStream = null;
    
    if(cloudStorageProperty != null) {
			if(Utils.isStringEmpty(cloudStorageProperty.getKey())){
				Globals.logString("DOWNLOAD: newBlobResource 1 Key Empty, generating it");
				cloudStorageProperty.generateKey();
			}
			
			Globals.logString("DOWNLOAD: newBlobResource 1bis Key="+cloudStorageProperty.getKey());
	
			if(cloudStorageProperty.getDirectory() == null){
				Globals.logString("DOWNLOAD: newBlobResource 2 Directory null computing it");
				cloudStorageProperty.setDirectory(Globals.getApp().getCloudStorageDirectory(), false);
			}
	
			Globals.logString("DOWNLOAD: newBlobResource 2bis Key="+cloudStorageProperty.getDirectory());
			
			InputStream is = (InputStream) cloudStorageProperty.getObject();
	
			if(is == null) Globals.logString("DOWNLOAD: newBlobResource 3 inputStream is null");
			else Globals.logString("DOWNLOAD: newBlobResource 3bis inputStream is Good");
			
			Globals.logString("DOWNLOAD: newBlobResource 4 FileName : "+cloudStorageProperty.getFileName());
	    
	    if(is != null){
	      downloadStream = new DownloadStream(is, "application/x-unknown", cloudStorageProperty.getFileName());
	      downloadStream.setParameter("Content-Disposition", "attachment; filename=" + cloudStorageProperty.getFileName());
	      downloadStream.setCacheTime(0);  
	    }
    }
    
    return downloadStream;
  }
  
	@Override
	public String getMIMEType() {
		return null;
	}

	@Override
	public String getFilename() {
		String fileName = "file";
		if(cloudStorageProperty != null) {
			fileName = cloudStorageProperty.getFileName();
		}
		return fileName;
	}
}