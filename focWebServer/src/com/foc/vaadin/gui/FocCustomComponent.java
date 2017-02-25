package com.foc.vaadin.gui;

import com.foc.vaadin.FocWebVaadinWindow;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;

@SuppressWarnings("serial")
public class FocCustomComponent extends CustomComponent {

	private FocWebVaadinWindow mainWindow = null;
	private AbsoluteLayout     mainLayout = null;
	private int                width      = 200;
	private int                height     = 200;
	
	public FocCustomComponent(FocWebVaadinWindow mainWindow, int width, int height){
		this.mainWindow = mainWindow;
		setDimensions(width, height);
	}
	
	public void dispose(){
		mainWindow = null;
		mainLayout = null;
	}
	
	public void init(){
		setCompositionRoot(buildMainLayout());
	}

	public void setDimensions(int width, int height){
		this.width      = width ;
		this.height     = height;
		if(mainLayout != null){
			mainLayout.setWidth(width+"px");
			mainLayout.setHeight(height+"px");
		}
		// top-level component properties
		setWidth(width+"px");
		setHeight(height+"px");
	}
	
	protected AbsoluteLayout buildMainLayout() {
		mainLayout = new AbsoluteLayout();

		mainLayout = new AbsoluteLayout();
		mainLayout.setImmediate(false);
//		mainLayout.setMargin(false);
		
		setDimensions(width, height);
		
		return mainLayout;
	}

	public AbsoluteLayout getMainLayout() {
		return mainLayout;
	}
	
	public FocWebVaadinWindow getMainWindow(){
		return mainWindow;
	}
	
	public Label addLabel(String value, String style, int left, int top, int width, int heigth){
		return addLabel(value, style, left, top, width, heigth, ContentMode.TEXT);
	}
	
	public Label addLabelXHTML(String value, String style, int left, int top, int width, int heigth){
		return addLabel(value, style, left, top, width, heigth, ContentMode.HTML);
	}
	
	private Label addLabel(String value, String style, int left, int top, int width, int heigth, ContentMode contentMode){
		Label lbl = null;
		if(mainLayout != null){
			lbl = new Label(value);
			lbl.setContentMode(contentMode);
			lbl.addStyleName(style);
			lbl.setWidth(width+"px");
			lbl.setHeight(heigth+"78px");
			mainLayout.addComponent(lbl, "top:"+top+".0px;left:"+left+".0px;");
		}
		return lbl;
	}
}
