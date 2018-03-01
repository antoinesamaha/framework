package com.foc.business.notifier.manipulators;

import com.foc.business.notifier.FocNotificationEvent;
import com.foc.business.notifier.FNotifTrigger;

public class FocNotificationTableAddManipulator extends AbstractFocNotificationTableAddManipulator {

  @Override
  public boolean shouldTreatEvent(FNotifTrigger notifier, FocNotificationEvent event) {
    return isSameDBTableAsFocObject(notifier, event);
  }

}