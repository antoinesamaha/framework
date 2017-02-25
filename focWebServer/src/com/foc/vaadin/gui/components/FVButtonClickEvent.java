package com.foc.vaadin.gui.components;

import com.vaadin.ui.Button.ClickEvent;

public class FVButtonClickEvent {
	public ClickEvent clickEvent = null;
	
	public FVButtonClickEvent(ClickEvent clickEvent){
		this.clickEvent = clickEvent;
	}
	
	public void dispose(){
		clickEvent = null;
	}
}
