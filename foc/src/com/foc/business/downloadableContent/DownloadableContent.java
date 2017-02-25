package com.foc.business.downloadableContent;

import java.io.InputStream;
import java.sql.Date;

import com.foc.Globals;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;

@SuppressWarnings("serial")
public class DownloadableContent extends FocObject{

  public DownloadableContent(FocConstructor constr){
    super(constr);
    newFocProperties();
  }
  
  public void setDocument(InputStream inputStream){
  	Globals.getApp().getDataSource().focObject_addBlobFromInputStream(this, DownloadableContentDesc.FLD_DOCUMENT, inputStream);
  }
  
  public InputStream getDocument(){
  	return Globals.getApp().getDataSource().focObject_LoadInputStream(this, DownloadableContentDesc.FLD_DOCUMENT);
  }
  
  public void setDocumentCloud(InputStream inputStream, String directoryName, boolean createDirIfNeeded, String fileName){
  	setPropertyCloudStorage(DownloadableContentDesc.FLD_DOCUMENT, inputStream, directoryName, createDirIfNeeded, fileName);
  }
  
  public InputStream getDocumentCloud(){
  	return getPropertyCloudStorage(DownloadableContentDesc.FLD_DOCUMENT);
  }
  
  public void setDocumentName(String imageName){
    setPropertyString(DownloadableContentDesc.FLD_DOCUMENT_NAME, imageName);
  }
  
  public String getDocumentName(){
    return getPropertyString(DownloadableContentDesc.FLD_DOCUMENT_NAME);
  }
  
  public void setUploadDate(Date uploadDate){
    setPropertyDate(DownloadableContentDesc.FLD_UPLOAD_DATE, uploadDate);
  }
  
  public Date getUploadDate(){
    return getPropertyDate(DownloadableContentDesc.FLD_UPLOAD_DATE);
  }
  
  public void setDescription(String description){
    setPropertyString(DownloadableContentDesc.FLD_DESCRIPTION, description);
  }
  
  public String getDescription(){
    return getPropertyString(DownloadableContentDesc.FLD_DESCRIPTION);
  }
}
