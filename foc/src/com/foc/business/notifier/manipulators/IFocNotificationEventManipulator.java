package com.foc.business.notifier.manipulators;

import com.foc.business.notifier.FocNotificationEvent;
import com.foc.business.notifier.FocNotificationEventConfigurator;

public interface IFocNotificationEventManipulator {
  public boolean shouldTreatEvent(FocNotificationEventConfigurator notifier, FocNotificationEvent event);
  public void    treatEvent(FocNotificationEventConfigurator notifier, FocNotificationEvent event);
}
