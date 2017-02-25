package com.foc.vaadin.gui.components.popupButton;

import com.foc.vaadin.gui.layouts.FVVerticalLayout;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class FVPopupWindow extends Window implements BlurListener, ClickListener{

	private FVPopupButton popupButton = null;
	private FVVerticalLayout mainVerticalLayout = null;
	
	public FVPopupWindow(FVPopupButton popupButton) {
		this.popupButton = popupButton;
		init();
	}
	
	private void init(){
		FVVerticalLayout rootLayout = new FVVerticalLayout();
		rootLayout.addComponent(getMainVerticalLayout(true));
		rootLayout.setComponentAlignment(getMainVerticalLayout(true), Alignment.TOP_LEFT);
		setContent(rootLayout);
		setClosable(true);
		setResizable(false);
		setSizeUndefined();
//		addStyleName("popupWindow");
		setImmediate(true);
		addBlurListener(this);
		addClickListener(this);
		setModal(true);
		setCaption(popupButton.getCaption());
	}
	
	public void dispose(){
		popupButton = null;
	}

	public void addComponentToVerticalLayout(Component component){
		FVVerticalLayout verticalLayout = getMainVerticalLayout(true);
		verticalLayout.addComponent(component);
		verticalLayout.setComponentAlignment(component, Alignment.TOP_LEFT);
		int height = getComponentCount() * 30;
		
	}
	
	public FVVerticalLayout getMainVerticalLayout(boolean createIfNeeded){
		if(mainVerticalLayout == null && createIfNeeded){
			mainVerticalLayout = new FVVerticalLayout();
			mainVerticalLayout.setHeight("100%");
			mainVerticalLayout.setMargin(true);
			mainVerticalLayout.setSpacing(true);
		}
		return mainVerticalLayout;
	}
	
	public FVPopupButton getPopupButton(){
		return popupButton;
	}

	@Override
	public void blur(BlurEvent event) {
//		close();
	}

	@Override
	public void click(ClickEvent event) {
	}
	
}
