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
						Integer objectRef = (Integer) selectedObjectsRefArrayList.get(i);
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
