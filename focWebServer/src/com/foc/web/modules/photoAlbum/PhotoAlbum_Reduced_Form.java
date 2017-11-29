package com.foc.web.modules.photoAlbum;

import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;

@SuppressWarnings("serial")
public class PhotoAlbum_Reduced_Form extends PhotoAlbum_Form {

	@Override
	protected void afterLayoutConstruction() {
		super.afterLayoutConstruction();
	}
	
	@Override
	public void showValidationLayout(boolean showBackButton, int position) {
		super.showValidationLayout(showBackButton, position);
		FVValidationLayout vLay = getValidationLayout();
		if(vLay != null) {
			Component deleteButtton = vLay.valo_GetDeleteEmbedded(true);
			if(deleteButtton != null){
	    	addComponent(deleteButtton);
	    	setComponentAlignment(deleteButtton, Alignment.BOTTOM_LEFT);
			}
		}
	}
}