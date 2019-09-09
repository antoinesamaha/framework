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
package com.foc.web.modules.photoAlbum;

import java.util.ArrayList;

import com.foc.business.photoAlbum.PhotoAlbum;
import com.foc.business.photoAlbum.PhotoAlbumListWithFilter;
import com.foc.list.FocList;
import com.foc.vaadin.gui.components.TableTreeDelegate;
import com.foc.vaadin.gui.layouts.FVTableWrapperLayout;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;

@SuppressWarnings("serial")
public class PhotoAlbum_UnrelatedAttachments_Table extends FocXMLLayout{

	private PhotoAlbumListWithFilter attachmentsList = null;
	
	@Override
	public void dispose() {
		super.dispose();
		attachmentsList = null;
	}

	public PhotoAlbumListWithFilter getPhotoAlbumListWithFilter() {
		return attachmentsList;
	}

	public void setAttachmentsList(PhotoAlbumListWithFilter attachmentsList) {
		this.attachmentsList = attachmentsList;
	}
	
	private FVTableWrapperLayout getTableWrapperLayout(){
		return (FVTableWrapperLayout) getComponentByName("_PHOTO_ALBUM_UNRELATED_ATTACHMENTS_TABLE");
	}
	
	private TableTreeDelegate getTableTreeDelegate(){
		return getTableWrapperLayout() != null ? getTableWrapperLayout().getTableTreeDelegate() : null;
	}
	
	@Override
	public boolean validationCheckData(FVValidationLayout validationLayout) {
		PhotoAlbumListWithFilter attachmentsListWithFilter = getPhotoAlbumListWithFilter();
		if(attachmentsListWithFilter != null && getUnrelatedAttachmentsList() != null){
			TableTreeDelegate tableTreeDelegate = getTableTreeDelegate();
			if(tableTreeDelegate != null){
				ArrayList<Object> selectedObjectsRefArrayList = tableTreeDelegate.selectionColumn_getSelectedIdArrayList();
				if(selectedObjectsRefArrayList != null){
					for(int i=0; i<selectedObjectsRefArrayList.size(); i++){
						Long objectRef = (Long) selectedObjectsRefArrayList.get(i);
						PhotoAlbum unrelatedAttachment = (PhotoAlbum) getUnrelatedAttachmentsList().searchByReference(objectRef);
						if(unrelatedAttachment != null){
							if(getPhotoAlbumListWithFilter() != null){
								unrelatedAttachment.setObjectRef(attachmentsListWithFilter.getObjectRef());
								unrelatedAttachment.setTableName(attachmentsListWithFilter.getTableName());
							}
							unrelatedAttachment.validate(true);
							attachmentsListWithFilter.add(unrelatedAttachment);
						}
					}
				}
			}
		}
		return false;
	}
	
	private FocList getUnrelatedAttachmentsList(){
		return getFocList();
	}
}
