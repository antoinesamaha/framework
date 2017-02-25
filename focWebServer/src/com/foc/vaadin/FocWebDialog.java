package com.foc.vaadin;

import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class FocWebDialog extends Window {

	public FocWebDialog(){
		//setSizeFull();

		setWidth("500px");
		setHeight("500px");
		setModal(true);
	}
	
}