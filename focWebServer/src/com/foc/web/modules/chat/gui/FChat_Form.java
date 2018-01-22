package com.foc.web.modules.chat.gui;

import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.vaadin.gui.components.FVButtonClickEvent;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.modules.chat.FChat;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;

@SuppressWarnings("serial")
public class FChat_Form extends FocXMLLayout{
	
	@Override
	protected void afterLayoutConstruction() {
		super.afterLayoutConstruction();
	}
	
	public FChat getchat() {
		return (FChat) getFocObject();
	}
	
	public FVButton getRecepientsButton() {
		return (FVButton)getComponentByName("RECEPIENTS");
	}
	
	public void button_RECEPIENTS_Clicked(FVButtonClickEvent event){
		FChat chat = getchat();
		if(chat != null) {
			XMLViewKey key = new XMLViewKey(FChat.DBNAME, XMLViewKey.TYPE_FORM, "Receivers", XMLViewKey.VIEW_DEFAULT);
			FocXMLLayout layout = (FocXMLLayout) XMLViewDictionary.getInstance().newCentralPanel(getMainWindow(), key, chat);
			layout.popupInDialog("Receivers", "400px", "200px");
		}
	}
}
