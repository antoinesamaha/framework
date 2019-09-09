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
package com.foc.web.unitTesting;

import com.foc.IFocEnvironment;
import com.foc.vaadin.FocWebEnvironment;

public class FocUnitExpectedNotification {

	private String notificationMessage = null;
	private String description = null;
	private int notificationType = IFocEnvironment.TYPE_WARNING_MESSAGE;
	
	public FocUnitExpectedNotification(String notificationMessage, String description, int notificationType){
		this.notificationMessage = notificationMessage;
		this.notificationType = notificationType;
		this.description = description;
	}
	
	public void assertNotification(String notificationMessage, String description, int notificationType) throws Exception {
		if(isAssertType() && getNotificationType() != notificationType){
			FocUnitDictionary.getInstance().getLogger().addError("Error Expected Notification of Type: "+getNotificationType()+" Received: "+notificationType);
		}else if(isAssertMessage() && !getNotificationMessage().equalsIgnoreCase(notificationMessage)){
			FocUnitDictionary.getInstance().getLogger().addError("Error Expected Notification of Message: "+getNotificationMessage() + " Received: "+notificationMessage);
		}else if(isAssertDescription() && !getDescription().equalsIgnoreCase(description)){
			FocUnitDictionary.getInstance().getLogger().addError("Error Expected Notification of Message: "+getDescription() + " Received: "+description);
		}else{
			FocUnitDictionary.getInstance().getLogger().addInfo("Notification Message: "+notificationMessage+" Asserted");
		}
	}
	
	public String getNotificationMessage() {
		return notificationMessage;
	}

	public void setNotificationMessage(String notificationMessage) {
		this.notificationMessage = notificationMessage;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(int notificationType) {
		this.notificationType = notificationType;
	}

	public boolean isAssertType() {
		return notificationType != FocWebEnvironment.TYPE_NOT_DEFINED;
	}

	public boolean isAssertMessage() {
		return notificationMessage != null;
	}

	public boolean isAssertDescription() {
		return description != null;
	}
}
