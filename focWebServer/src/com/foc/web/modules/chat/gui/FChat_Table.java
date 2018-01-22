package com.foc.web.modules.chat.gui;

import com.foc.util.Utils;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.vaadin.gui.components.FVButtonClickEvent;
import com.foc.vaadin.gui.components.FVTextArea;
import com.foc.vaadin.gui.layouts.FVForEachLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.modules.chat.FChat;
import com.foc.web.modules.chat.FChatList;
import com.vaadin.ui.themes.BaseTheme;

@SuppressWarnings("serial")
public class FChat_Table extends FocXMLLayout{

	@Override
	protected void afterLayoutConstruction() {
		super.afterLayoutConstruction();
		FVButton button = (FVButton) getComponentByName("SEND");
		button.setStyleName(BaseTheme.BUTTON_LINK);
		button.addStyleName("foc-chat-layout");
		button.addStyleName("foc-f22");
	}
	
	public FChatList getChatList() {
		return (FChatList) getFocList();
	}
	
	public FVTextArea getTextArea() {
		return (FVTextArea) getComponentByName("MY_CHAT");
	}
	
	public void button_SEND_Clicked(FVButtonClickEvent evt){
		FChatList list = getChatList();
		
		FVTextArea textArea = getTextArea();
		if(list != null && textArea != null) {
			String message = textArea.getValue();
			if(!Utils.isStringEmpty(message)) {
				FChat chat = (FChat) list.newEmptyItem();
				chat.setMessage(message);
				list.add(chat);
				list.validate(true);
				
        FVForEachLayout forEachLayout = (FVForEachLayout) getComponentByName("_ChatTable");
        if(forEachLayout != null){
        	forEachLayout.addBannerForFocObject(chat);
        	textArea.setValue("");
				}
			}
		}
	}

}
