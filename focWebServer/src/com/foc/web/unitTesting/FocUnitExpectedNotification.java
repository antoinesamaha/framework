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
