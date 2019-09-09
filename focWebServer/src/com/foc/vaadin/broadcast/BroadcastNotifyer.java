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
package com.foc.vaadin.broadcast;

import com.foc.Globals;
import com.foc.OptionDialog;
import com.foc.access.FocDataMap;
import com.foc.business.notifier.BroadcastMessage;
import com.foc.business.notifier.FNotifTrigger;
import com.foc.business.notifier.FocNotificationConst;
import com.foc.business.notifier.FocNotificationEvent;
import com.foc.business.notifier.FocNotificationManager;
import com.foc.business.notifier.actions.FocNotifAction_Abstract;
import com.foc.shared.dataStore.IFocData;
import com.vaadin.ui.UI;

public class BroadcastNotifyer {
	private FNotifTrigger notifier = null;

	public void init() {
		MyNotification manipulator = new MyNotification(UI.getCurrent());
		notifier = FocNotificationManager.getInstance().addInternalEventNotifier(FocNotificationConst.EVT_TABLE_ADD, BroadcastMessage.getFocDesc(), null, manipulator);
	}

	public void dispose() {
		removeDBTrigger();
		notifier = null;
	}

	public void removeDBTrigger() {
		if(notifier != null && FocNotificationManager.getInstance() != null){
			FocNotificationManager.getInstance().removeInternalEventNotifier(notifier);
		}
	}

	public class MyNotification extends FocNotifAction_Abstract {
		private UI ui = null;

		private String nextMessage = null;

		public MyNotification(UI ui) {
			this.ui = ui;
		}

		public void execute(FNotifTrigger notifier, FocNotificationEvent event) {
			try{
				if(ui != null && ui.isAttached()){

					IFocData focData = event.getEventFocData();
					if(focData instanceof FocDataMap){
						IFocData mainFocData = ((FocDataMap) focData).getMainFocData();

						if(mainFocData instanceof BroadcastMessage){
							BroadcastMessage broadcastMessage = (BroadcastMessage) mainFocData;

							nextMessage = broadcastMessage.getMessage();
						}
					}

					ui.access(new Runnable() {
						@Override
						public void run() {
							IFocData focData = event.getEventFocData();
							if(focData instanceof FocDataMap){
								OptionDialog dlg = new OptionDialog("Admin Message", nextMessage) {
									@Override
									public boolean executeOption(String optionName) {
										return false;
									}
								};
								dlg.addOption("OK", "Ok");
								dlg.popup();
							}
						}
					});
				}
			}catch (Exception e){
				Globals.logException(e);
			}
		}
	}
	
}
