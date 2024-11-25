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

		while (true){
			Globals.logString("NOTIFICATION TRIGGER: Starting a new scan cycle.");

			try{
				Thread.sleep(60000);
				Globals.logString("NOTIFICATION TRIGGER: Woke up after 60 seconds.");

				FocNotificationManager manager = FocNotificationManager.getInstance();
				if(manager != null){
					Globals.logString("NOTIFICATION TRIGGER: Retrieved FocNotificationManager.");

					FocList eventNotifierList = manager.getEventNotifierList();
					if(eventNotifierList != null){
						Globals.logString("NOTIFICATION TRIGGER: Found " + eventNotifierList.size() + " notification triggers to scan.");

						for(int i = 0; i < eventNotifierList.size(); i++){
							FNotifTrigger trigger = (FNotifTrigger) eventNotifierList.getFocObject(i);

							if(trigger.getEvent() == FNotifTrigger.EVT_SCHEDULED && trigger.isEventMatch(event) && !trigger.isRunning()){
								Globals.logString("NOTIFICATION TRIGGER: Found matching trigger at index " + i + ", starting thread.");
								trigger.setRunning(true);
								AtomicNotificationTriggerThread atomicThread = new AtomicNotificationTriggerThread(getClassNameFocWebApplication(), getWebServer(), trigger, event);
								atomicThread.start();
								Globals.logString("NOTIFICATION TRIGGER: Thread started for trigger at index " + i + ".");
							}
						}
						Globals.logString("NOTIFICATION TRIGGER: Completed scanning triggers.");
					}else{
						Globals.logString("NOTIFICATION TRIGGER: No notification triggers found.");
					}
				}else{
					Globals.logString("NOTIFICATION TRIGGER: FocNotificationManager not available.");
				}
			}catch(Exception e) {
				Globals.logString("Error while processing notification triggers !!!");
				Globals.logExceptionWithoutPopup(e);
			}
			Globals.logString("NOTIFICATION TRIGGER: Scan cycle completed.");
		}
	}

	public class AtomicNotificationTriggerThread extends FocThreadWithSession {

		private FNotifTrigger trigger = null;

		private FocNotificationEvent event = null;

		public AtomicNotificationTriggerThread(String classNameFocWebApplication, FocWebServer webServer, FNotifTrigger trigger, FocNotificationEvent event) {
			super(classNameFocWebApplication, webServer);
			this.trigger = trigger;
			this.event = event;
			setInitiallSleep(0);
		}

		public void dispose() {
			super.dispose();
			trigger.setRunning(false);
			trigger = null;
			event = null;
		}

		@Override
		public void main() {
			trigger.executeAndReschedule(event);
			dispose();
		}
	}
}
