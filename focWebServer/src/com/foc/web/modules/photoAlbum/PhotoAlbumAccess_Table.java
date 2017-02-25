package com.foc.web.modules.photoAlbum;

import com.foc.business.photoAlbum.PhotoAlbum;
import com.foc.list.FocList;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;

@SuppressWarnings("serial")
public class PhotoAlbumAccess_Table extends FocXMLLayout {

	@Override
	protected void afterLayoutConstruction() {
		super.afterLayoutConstruction();
	}
	
	public FocList getPhotoAlbumAccessList(){
		return getFocList();
	}
	
	public PhotoAlbum getPhotoAlbum(){
		return (PhotoAlbum) getPhotoAlbumAccessList().getFatherSubject();
	}
}
