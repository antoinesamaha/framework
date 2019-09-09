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

import com.foc.OptionDialog;
import com.foc.business.photoAlbum.PhotoAlbumAppGroup;
import com.foc.desc.FocObject;
import com.foc.vaadin.FocWebApplication;
import com.foc.vaadin.gui.FVIconFactory;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.vaadin.event.MouseEvents;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;

@SuppressWarnings("serial")
public class PhotoAlbum_Reduced_Form extends PhotoAlbum_Form {

	private PhotoAlbum_Thumb_Table thumbForm = null;

	@Override
	public void dispose() {
		super.dispose();
		if (thumbForm != null) {
			thumbForm.refresh();
		}
		thumbForm = null;
	}
	
	public void deleteButtonClickListener() {
  	OptionDialog dialog = new OptionDialog("Confirm Deletion", "Are you sure you want to delete this item", getFocData()) {
			
			@Override
			public boolean executeOption(String optionName) {
				if(optionName.equals("DELETE")){
	    		FocObject focObject = getFocObject();
	    		if(focObject != null){
	    			focObject.delete();
	    			FocXMLLayout previousLayout = getThumbForm();
	    			getValidationLayout().goBack();
						if(previousLayout != null) {
							previousLayout.refresh();
						}
	    		}
				}else if(optionName.equals("CANCEL")){
					
				}
				return false;
			}
		};
  	dialog.addOption("DELETE", "Delete");
  	dialog.addOption("CANCEL", "Cancel");
  	dialog.setWidth("300px");
  	dialog.setHeight("200px");
  	dialog.popup();
	}
	
	@Override
	public void showValidationLayout(boolean showBackButton, int position) {
		super.showValidationLayout(showBackButton, position);
		
		FVValidationLayout vLay = getValidationLayout();
		if(vLay != null && getFocObject() != null && !getFocObject().isCreated()) {
			
			if(			PhotoAlbumAppGroup.getCurrentAppGroup() != null 
					&& 	PhotoAlbumAppGroup.getCurrentAppGroup().isAllowDelete()) {
				Embedded valo_DeleteEmbedded = new Embedded();
	  		valo_DeleteEmbedded.addStyleName(FocXMLGuiComponentStatic.STYLE_HAND_POINTER_ON_HOVER);
				if(FocWebApplication.getInstanceForThread().isMobile()){
					valo_DeleteEmbedded.setSource(FVIconFactory.getInstance().getFVIcon_Big(FVIconFactory.ICON_TRASH_WHITE));
				}else{
					valo_DeleteEmbedded.setSource(FVIconFactory.getInstance().getFVIcon_Big(FVIconFactory.ICON_TRASH_BLACK));
				}
				valo_DeleteEmbedded.addClickListener(new MouseEvents.ClickListener() {
					@Override
					public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
						deleteButtonClickListener();					
					}
				});
				if(valo_DeleteEmbedded != null){
					vLay.addComponent(valo_DeleteEmbedded);
					vLay.setComponentAlignment(valo_DeleteEmbedded, Alignment.BOTTOM_LEFT);
				}
			}
		}
	}
	
	public PhotoAlbum_Thumb_Table getThumbForm() {
		return thumbForm;
	}

	public void setThumbForm(PhotoAlbum_Thumb_Table thumbForm) {
		this.thumbForm = thumbForm;
	}
}
