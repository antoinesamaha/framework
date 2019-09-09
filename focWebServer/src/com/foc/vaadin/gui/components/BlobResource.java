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
		
		File file = new File(cloudStorageProperty.getFileName());
		Globals.logString("DOWNLOAD: newBlobResource 5");
		BlobResource resource = new BlobResource(file, is, cloudStorageProperty.getFileName());
		Globals.logString("DOWNLOAD: newBlobResource 6");
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
