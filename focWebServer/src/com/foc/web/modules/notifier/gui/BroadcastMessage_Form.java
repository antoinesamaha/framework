package com.foc.web.modules.notifier.gui;

import java.sql.Date;
import java.sql.Time;

import com.foc.Globals;
import com.foc.business.notifier.BroadcastMessage;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLView;

public class BroadcastMessage_Form extends FocXMLLayout {

	@Override
	public void init(INavigationWindow window, XMLView xmlView, IFocData focData) {
		super.init(window, xmlView, focData);
		BroadcastMessage broadcastMessage = getBroadcastMessage();
		
		if(broadcastMessage != null && broadcastMessage.isCreated()) {
			broadcastMessage.setUser(Globals.getApp().getUser_ForThisSession());
			
	    Date systemDate = Globals.getApp().getSystemDate();
			long timeLong = systemDate.getTime();
			Time time = new Time(timeLong);
			broadcastMessage.setTime(time);
		}
	}
	
	public BroadcastMessage getBroadcastMessage(){
		return (BroadcastMessage) getFocData();
	}
}
