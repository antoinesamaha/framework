package com.foc.business.notifier;

import com.foc.shared.dataStore.IFocData;

public interface IFocNotificationEmailLaunchStatus {

	public void emailSendStatusHandler(IFocData emailFocData, boolean error);
}
