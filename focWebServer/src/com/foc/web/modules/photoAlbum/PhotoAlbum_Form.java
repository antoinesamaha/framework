package com.foc.web.modules.photoAlbum;

import com.foc.business.photoAlbum.PhotoAlbum;
import com.foc.business.photoAlbum.PhotoAlbumAccess;
import com.foc.business.photoAlbum.PhotoAlbumConfig;
import com.foc.list.FocList;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.layouts.FVVerticalLayout;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLView;
import com.vaadin.ui.Component;

@SuppressWarnings("serial")
public class PhotoAlbum_Form extends FocXMLLayout {
	
	boolean enabled = false;
	
	public PhotoAlbum getPhotoAlbum(){
		return (PhotoAlbum) getFocObject();
	}
	
	@Override
	public void init(INavigationWindow window, XMLView xmlView, IFocData focData) {
		applyAccessRightsToDocument(focData);
		super.init(window, xmlView, focData);
	}
	
	private void applyAccessRightsToDocument(IFocData focData) {
		if(focData != null && focData instanceof PhotoAlbum){
			PhotoAlbum photoAlbum = (PhotoAlbum) focData;
			enabled = photoAlbum.isEditable();
		}
		setEnabled(enabled);
	}

	@Override
	protected void afterLayoutConstruction() {
		super.afterLayoutConstruction();
		
    PhotoAlbumConfig config = PhotoAlbumConfig.getInstance();
    if(config != null && config.isSingleGroup()){		
			FVVerticalLayout verticalLayout = (FVVerticalLayout) getComponentByName("_ACCESS_GROUPS");
			if(verticalLayout != null){
				verticalLayout.setVisible(false);
			}
    }
    if(config != null && !config.isUseKeywords()){		
			Component compKeyword = getComponentByName("KEYWORD");
			if(compKeyword != null){
				compKeyword.setVisible(false);
			}
    }
	}
	
//	private void setDownloadIcon() {
//		Resource iconUploadResource = FVIconFactory.getInstance().getFVIcon_24(FVIconFactory.ICON_DOWNLOAD_24X24);
//		Button downloadButton = (Button) getComponentByName("DOWNLOAD");
//		downloadButton.setStyleName(Runo.BUTTON_LINK);
//		downloadButton.setWidth("-1px");
//		downloadButton.setHeight("-1px");
//		downloadButton.setIcon(iconUploadResource);
//		
//	}
//
//	public void button_DOWNLOAD_Clicked(FVButtonClickEvent evt){
//		PhotoAlbum album = (PhotoAlbum) getPhotoAlbum();
//		if(album != null){
//			FVButton button = (FVButton) getComponentByName("DOWNLOAD");
//			if(button != null){
//				PhotoAlbumFileResource resource = album.new PhotoAlbumFileResource(new File(""), album);
//				FileDownloader downloader = new FileDownloader(resource);
//				downloader.extend(button);
//			}
//		}
//	}
	
	@Override
	public boolean validationCheckData(FVValidationLayout validationLayout) {
		boolean error = false;
		error = super.validationCheckData(validationLayout);
		if(getPhotoAlbum() != null){
			FocList photoAlbumAccessList = getPhotoAlbum().getPhotoAlbumAccessList();
			String allowedGroups="";
			for(int i=0;i<photoAlbumAccessList.size();i++){
				PhotoAlbumAccess photoAlbumAccess = (PhotoAlbumAccess) photoAlbumAccessList.getFocObject(i);
				String docRightsGroupName = photoAlbumAccess.getDocRightsGroup().getName();
				if(i == (photoAlbumAccessList.size()-1)){
					allowedGroups +=docRightsGroupName;
				}else{
					allowedGroups += (docRightsGroupName+", ");
				}
			}
			getPhotoAlbum().setAllowedGroups(allowedGroups);
		}
		return error;
	}
}