package com.foc.business.notifier.actions;

import com.foc.business.notifier.FocNotificationEvent;
import com.foc.business.notifier.FNotifTrigger;

public interface IFocNotifAction {
  public void execute(FNotifTrigger notifier, FocNotificationEvent event);
}
