package com.foc.business.notifier;

import java.util.HashMap;

import com.foc.Globals;
import com.foc.business.notifier.manipulators.FocNotificationTableAddManipulator;
import com.foc.business.notifier.manipulators.FocNotificationTableDeleteManipulator;
import com.foc.business.notifier.manipulators.FocNotificationTableUpdateManipulator;
import com.foc.business.notifier.manipulators.FocNotificationUserFromContactManipulator;
import com.foc.business.notifier.manipulators.IFocNotificationEventManipulator;


@SuppressWarnings("serial")
public class FocNotificationEventFactory extends HashMap<Integer, IFocNotificationEventManipulator>{
  
  public FocNotificationEventFactory(){
    put(FocNotificationConst.EVT_CREATE_USER_FROM_CONTACT, new FocNotificationUserFromContactManipulator());
    put(FocNotificationConst.EVT_TABLE_ADD, new FocNotificationTableAddManipulator());
    put(FocNotificationConst.EVT_TABLE_DELETE, new FocNotificationTableDeleteManipulator());
    put(FocNotificationConst.EVT_TABLE_UPDATE, new FocNotificationTableUpdateManipulator());
  }
  
  
  public static FocNotificationEventFactory getInstance(boolean createIfNeeded) {

    FocNotificationEventFactory focNEF = null;
    if (Globals.getApp() != null) {
      focNEF = Globals.getApp().getNotificationEventFactory(createIfNeeded);
    }
    return focNEF;
  }
  
  public static FocNotificationEventFactory getInstance(){
    return getInstance(true);
  }
}
