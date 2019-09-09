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
package com.foc.web.modules.downloadableContent;

import java.io.InputStream;

import com.foc.Globals;
import com.foc.business.downloadableContent.DownloadableContent;
import com.foc.business.downloadableContent.DownloadableContentDesc;
import com.foc.business.photoAlbum.PhotoAlbum;
import com.foc.business.photoAlbum.PhotoAlbumDesc;
import com.foc.business.photoAlbum.PhotoAlbumListWithFilter;
import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.property.FCloudStorageProperty;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.FocWebApplication;
import com.foc.vaadin.gui.components.BlobResource;
import com.foc.vaadin.gui.components.FVTablePopupMenu;
import com.foc.vaadin.gui.components.upload.FVImageReceiver;
import com.foc.vaadin.gui.components.upload.FVUpload_Image;
import com.foc.vaadin.gui.layouts.FVTableWrapperLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.modules.photoAlbum.PhotoAlbumWebModule;
import com.foc.web.modules.photoAlbum.PhotoAlbum_UnrelatedAttachments_Table;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Upload.SucceededEvent;

@SuppressWarnings("serial")
public class DownloadableContent_Table extends FocXMLLayout {
	
	@Override
	public void dispose() {
		super.dispose();
	}
	
	@Override
	protected void afterLayoutConstruction() {
		super.afterLayoutConstruction();
    uploadField();
  }
  
  public void uploadField(){
    FVUpload_Image uploadDocument = new FVUpload_Image();
    FVTableWrapperLayout bkdnTreeWrapper = (FVTableWrapperLayout) getComponentByName("DOWNLOADABLE_CONTENT");
    bkdnTreeWrapper.addHeaderComponent(uploadDocument);
    
    FVImageReceiver imageReceiver = new FVImageReceiver() {
      public void imageReceived(SucceededEvent event, InputStream inputStream) {
      	addDocument(event.getFilename(), inputStream);
      }
    };
    
    uploadDocument.setImageReceiver(imageReceiver);
  }
  
  public void addDocument(String fileName, InputStream inputStream){
    if(inputStream != null){
    	FocWebApplication application = FocWebApplication.getInstanceForThread();
      
      if(application != null){
        FocList downloadableContentList = (FocList) getFocData();//PhotoAlbumDesc.getList(FocList.LOAD_IF_NEEDED);
        DownloadableContent downloadableContent = (DownloadableContent) downloadableContentList.newEmptyItem();
        downloadableContent.setUploadDate(Globals.getApp().getSystemDate());
        downloadableContent.setDocumentName(fileName);
        downloadableContent.validate(true);
        downloadableContentList.add(downloadableContent);
        
        downloadableContent.setDocumentCloud(inputStream, Globals.getApp().getCloudStorageDirectory(), true, fileName);
        refresh();
        
        XMLViewKey key = new XMLViewKey(DownloadableContentDesc.getInstance().getStorageName(), XMLViewKey.TYPE_FORM);
        getMainWindow().changeCentralPanelContent(XMLViewDictionary.getInstance().newCentralPanel(getMainWindow(), key, downloadableContent), true);
      }
    }
  }
  
	public void addPopupMenu_DownAttachment(){
		FVTableWrapperLayout tableWrapper = (FVTableWrapperLayout) getComponentByName("DOWNLOADABLE_CONTENT");
		if(tableWrapper != null){
			tableWrapper.getTableTreeDelegate().addPopupMenu(new FVTablePopupMenu("Download Attached File"){
				@Override
				public void actionPerformed(FocObject focObject) {
					DownloadableContent downloadableContent = (DownloadableContent) focObject;
					if(downloadableContent != null){
						FCloudStorageProperty cloudStorageProperty = (FCloudStorageProperty) downloadableContent.getFocProperty(DownloadableContentDesc.FLD_DOCUMENT);
						BlobResource blobResource = BlobResource.newBlobResource(cloudStorageProperty);
						blobResource.openDownloadWindow();
					}
				}	
			});
		}
	}
}
