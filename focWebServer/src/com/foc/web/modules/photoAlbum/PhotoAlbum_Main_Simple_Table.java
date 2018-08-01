package com.foc.web.modules.photoAlbum;

@SuppressWarnings("serial")
public class PhotoAlbum_Main_Simple_Table extends PhotoAlbum_Table {
	@Override
	protected boolean isWithDocumentTypeFilter() {
		return false;
	}
	
  public boolean isPopupDocumentFormWhenAdd() {
  	return false;
  }
}