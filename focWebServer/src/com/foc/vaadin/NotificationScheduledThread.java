package com.foc.vaadin;

import com.foc.Globals;
import com.foc.business.notifier.FNotifTrigger;
import com.foc.business.notifier.FocNotificationEvent;
import com.foc.business.notifier.FocNotificationManager;
import com.foc.list.FocList;
import com.foc.web.server.FocWebServer;

public class NotificationScheduledThread extends FocThreadWithSession {

	public NotificationScheduledThread(FocWebApplication initialWebApplication, FocWebServer webServer) {
		super(initialWebApplication, webServer);
	}

	@Override
	public void main() {
		FocNotificationEvent event = new FocNotificationEvent(FNotifTrigger.EVT_SCHEDULED, null);
		
		while(true) {
			try {
				Thread.sleep(60000);
				
				if(FocNotificationManager.getInstance() != null) {
					FocList eventNotifierList = FocNotificationManager.getInstance().getEventNotifierList();
					if(eventNotifierList != null) {
			      for (int i = 0; i < eventNotifierList.size(); i++) {
			        FNotifTrigger trigger = (FNotifTrigger) eventNotifierList.getFocObject(i);
			        trigger.executeIfSameEvent(event);
			      }
					}
				}
			}catch(Exception e) {
				Globals.logExceptionWithoutPopup(e);
			}
		}
	}
}