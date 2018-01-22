package com.foc.web.modules.photoAlbum;

import com.foc.business.photoAlbum.PhotoAlbum;
import com.foc.business.photoAlbum.PhotoAlbumDesc;
import com.foc.business.photoAlbum.PhotoAlbumListWithFilter;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.FocCentralPanel;
import com.foc.vaadin.FocWebApplication;
import com.foc.vaadin.gui.components.FVImageField;
import com.foc.vaadin.gui.components.upload.FVUpload_Image;
import com.foc.vaadin.gui.layouts.FVVerticalLayout;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class PhotoAlbum_Thumb_Table extends PhotoAlbum_Table {

	private static final int MAX_COLS = 3;
	private static final int MAX_ROWS = 3;
	
	@Override
	protected void afterLayoutConstruction() {
		super.afterLayoutConstruction();
		
		redrawPhotos();
	}
	
	public void dispose() {
		super.dispose();
	}

	@Override
	public void refresh() {
		super.refresh();
		redrawPhotos();
	}
	
	@Override
	public String getFormContextAfterUpload() {
		return "Reduced";
	}
	
	private void redrawPhotos() {
		FVVerticalLayout vLay = getMainVerticalLayout();
		if(vLay != null) {
			vLay.removeAllComponents();
			
			HorizontalLayout hLay = null;
			
			int nbrPhotos = 0;
			
			PhotoAlbumListWithFilter list = (PhotoAlbumListWithFilter) getFocList();
			if(list != null && list.getFocListFilter() != null) {
				list.getFocListFilter().setActive(true);
				for(int i=0; i<list.size(); i++) {
					PhotoAlbum photo = (PhotoAlbum) list.getFocObject(i);
					FocXMLAttributes attribs = new FocXMLAttributes();
					attribs.addAttribute(FXML.ATT_WIDTH, "100px");
					attribs.addAttribute(FXML.ATT_HEIGHT, "100px");
					attribs.addAttribute(FXML.ATT_EDITABLE, "false");
					
					FVImageField imgField = new FVImageField(photo.getFocProperty(PhotoAlbumDesc.FLD_IMAGE), attribs);
					
					if(!imgField.isImage()) {
						imgField.dispose();
					} else {
						
						if(nbrPhotos % 4 == 0) {//4 Photos by line
							hLay = null;
						}
						nbrPhotos++;
						
						if(hLay == null) {
							hLay = new HorizontalLayout();
							hLay.setSpacing(true);
							vLay.addComponent(hLay);
						}
						
						if(imgField.getEmbedded() != null) {
							imgField.addStyleName("foc-ImageFieldClickable");
							imgField.getEmbedded().addClickListener(new PhotoClickListener(photo));
						}
						hLay.addComponent(imgField);
					}
				}
			}
			
			FVUpload_Image uploadButton = newUploadButton();
			if(uploadButton != null) {
				uploadButton.setMaxSizeAllowed(8388608);
				vLay.addComponent(uploadButton);
			}
		}
	}
	
	public FVVerticalLayout getMainVerticalLayout() {
		return (FVVerticalLayout) getComponentByName("_MAIN_VERTICAL_LAYOUT");
	}
	
	public class PhotoClickListener implements ClickListener {
		private PhotoAlbum photoAlbum = null;
		
		public PhotoClickListener(PhotoAlbum photoAlbum) {
			this.photoAlbum = photoAlbum;
		}
		
		public void dispose() {
			photoAlbum = null;
		}
		
		@Override
		public void click(ClickEvent event) {
			INavigationWindow navigationWindow = (INavigationWindow) FocWebApplication.getInstanceForThread().getNavigationWindow();
			XMLViewKey    key          = new XMLViewKey(PhotoAlbumDesc.getInstance().getStorageName(), XMLViewKey.TYPE_FORM, getFormContextAfterUpload(), getFormViewAfterUpload());
			PhotoAlbum_Reduced_Form reducedForm = (PhotoAlbum_Reduced_Form) XMLViewDictionary.getInstance().newCentralPanel((INavigationWindow) navigationWindow, key, photoAlbum);
			reducedForm.setThumbForm(PhotoAlbum_Thumb_Table.this);
			boolean dialog = true;
			if(dialog){
				FocCentralPanel focCentralWindow = new FocCentralPanel();
				focCentralWindow.fill();
				focCentralWindow.changeCentralPanelContent(reducedForm, false);

				Window window = focCentralWindow.newWrapperWindow();
				FocWebApplication.getInstanceForThread().addWindow(window);
			}else{
				navigationWindow.changeCentralPanelContent(reducedForm, true);
			}
		}		
	}
}