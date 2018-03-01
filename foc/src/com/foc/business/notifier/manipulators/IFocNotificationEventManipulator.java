package com.foc.business.notifier.manipulators;

import com.foc.business.notifier.FocNotificationEvent;
import com.foc.business.notifier.FNotifTrigger;

public interface IFocNotificationEventManipulator {
  public boolean shouldTreatEvent(FNotifTrigger notifier, FocNotificationEvent event);
  public void    treatEvent(FNotifTrigger notifier, FocNotificationEvent event);
}
