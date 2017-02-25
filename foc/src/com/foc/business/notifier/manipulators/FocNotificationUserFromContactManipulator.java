package com.foc.business.notifier.manipulators;

import com.foc.business.notifier.FocNotificationEvent;
import com.foc.business.notifier.FocNotificationEventConfigurator;

public class FocNotificationUserFromContactManipulator extends AbstractFocNotificationTableAddManipulator {

  @Override
  public boolean shouldTreatEvent(FocNotificationEventConfigurator notifier, FocNotificationEvent event) {
    return true;
  }
}
