package com.foc.vaadin.gui.components;

import java.lang.reflect.Method;

import org.xml.sax.Attributes;

import com.foc.Globals;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;

@SuppressWarnings({ "serial", "unchecked" })
public class FVButton_Reflection extends FVButton {

	private FocXMLLayout xmlLayout = null;
	private Method       method    = null;

	public FVButton_Reflection(Attributes attributes, FocXMLLayout xmlLayout, Method method) {
		super(attributes);
		this.method = method;
		this.xmlLayout = xmlLayout;
		addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				try{
					if(getMethod() != null){
						FVButtonClickEvent fvClickEvent = new FVButtonClickEvent(event); 
						getMethod().invoke(getXmlLayout(), fvClickEvent);
						if(fvClickEvent != null) fvClickEvent.dispose();
					}
				}catch(Exception e){
					Globals.logException(e);
				}
			}
		});
	}
	
	public void dispose(){
		super.dispose();
		xmlLayout = null;
		method = null;
	}

	public FocXMLLayout getXmlLayout() {
		return xmlLayout;
	}

	public Method getMethod() {
		return method;
	}
}
