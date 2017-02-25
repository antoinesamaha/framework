package com.foc.business.notifier.manipulators;

import com.foc.business.notifier.FocNotificationEvent;
import com.foc.business.notifier.FocNotificationEventConfigurator;

public class FocNotificationTableDeleteManipulator extends AbstractFocNotificationTableAddManipulator {

  @Override
  public boolean shouldTreatEvent(FocNotificationEventConfigurator notifier, FocNotificationEvent event) {
    return isSameDBTableAsFocObject(notifier, event);
  }

}
