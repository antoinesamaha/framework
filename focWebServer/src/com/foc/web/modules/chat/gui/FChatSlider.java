package com.foc.web.modules.chat.gui;

import org.vaadin.sliderpanel.SliderPanel;
import org.vaadin.sliderpanel.SliderPanelBuilder;
import org.vaadin.sliderpanel.SliderPanelStyles;
import org.vaadin.sliderpanel.client.SliderMode;
import org.vaadin.sliderpanel.client.SliderPanelListener;
import org.vaadin.sliderpanel.client.SliderTabPosition;

import com.foc.desc.FocObject;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.modules.chat.FChat;
import com.foc.web.modules.chat.FChatList;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;

public class FChatSlider {
	
	private FocXMLLayout        xmlLayout     = null;
	private SliderPanel         chatSlider    = null;
	private SliderPanelListener listener      = null;
	private FocXMLLayout        chatXmlLayout = null;
	
	public FChatSlider(FocXMLLayout xmlLayout) {
		this.xmlLayout = xmlLayout;
	}
	
	public void dispose() {
		if(chatXmlLayout != null) {
			chatXmlLayout.dispose();
			chatXmlLayout = null;
		}
		if(listener != null) {
			if(chatSlider != null) chatSlider.removeListener(listener);
			listener = null;
		}
		if(xmlLayout != null && chatSlider != null) {
			xmlLayout.removeComponent(chatSlider);
		}
		chatSlider = null;
		xmlLayout = null;
	}

	public FocObject getFocObject() {
		return xmlLayout != null ? xmlLayout.getFocObject() : null;
	}
	
	public FChatList getChatList() {
		FChatList chatList = null;
		if(chatXmlLayout != null) {
			chatList = (FChatList) chatXmlLayout.getFocList();
		}
		return chatList;
	}
	
	public void build() {
		if(getFocObject() != null && xmlLayout != null && xmlLayout.getMainWindow() != null) {
			FChatList chatList = new FChatList(getFocObject());
			chatList.loadIfNotLoadedFromDB();
			chatList.updateUnreadFlag();
		  //XMLViewKey key = new XMLViewKey(FChat.DBNAME, XMLViewKey.TYPE_TABLE);
			XMLViewKey key = new XMLViewKey(FChat.DBNAME, XMLViewKey.TYPE_TABLE, "Banner", XMLViewKey.VIEW_DEFAULT);
			chatXmlLayout = (FocXMLLayout) XMLViewDictionary.getInstance().newCentralPanel(xmlLayout.getMainWindow(), key, chatList);
			if(chatXmlLayout != null) {
				chatXmlLayout.setFocDataOwner(true);
			  
				Panel panel = new Panel();
				panel.setHeight("800px");
				panel.addStyleName("foc-chat-layout");
				panel.setContent(chatXmlLayout);	
				
				SliderPanelBuilder buider = new SliderPanelBuilder(panel, "Chat");
				buider.expanded(false);
				buider.mode(SliderMode.BOTTOM);
				buider.caption("Chat");
				buider.style(SliderPanelStyles.COLOR_GRAY);
				buider.tabPosition(SliderTabPosition.END);
				buider.fixedContentSize(300);
				buider.flowInContent(true);
				
				chatSlider = buider.build();
				chatSlider.setIcon(FontAwesome.COMMENTING);
				listener = new SliderPanelListener() {
					@Override
					public void onToggle(boolean expand) {
						if(expand) {
							FChatList chatList = getChatList();
							chatList.markAsRead();
							chatList.validate(true);
						}
					}
				};
				chatSlider.addListener(listener);
				
				HorizontalLayout horLay = new HorizontalLayout();
				horLay.setHeight("0px");
				horLay.setWidth("100%");
				Label label = new Label(" ");
				horLay.addComponent(label);
				horLay.setExpandRatio(label, (float) 0.75);
				horLay.addComponent(chatSlider);
				horLay.setExpandRatio(chatSlider, (float) 0.25);
				xmlLayout.addComponent(horLay);
			}
		}
	}
}
