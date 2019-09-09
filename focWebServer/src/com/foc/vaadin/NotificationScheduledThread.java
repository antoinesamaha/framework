/*******************************************************************************
 * Copyright 2016 Antoine Nicolas SAMAHA
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
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
				
				Globals.logString("NOTIFICATION TRIGGER: Scanning Notification Triggers for scheduled threads every 60000");
				if(FocNotificationManager.getInstance() != null) {
					FocList eventNotifierList = FocNotificationManager.getInstance().getEventNotifierList();
					if(eventNotifierList != null) {
						Globals.logString("NOTIFICATION TRIGGER:   - Scanning "+eventNotifierList.size()+" Notification Triggers");
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
