package com.foc.business.notifier.actions;

import java.util.HashMap;

import com.foc.Globals;
import com.foc.business.notifier.FNotifTrigger;

@SuppressWarnings("serial")
public class FocNotifActionFactory extends HashMap<Integer, IFocNotifAction>{
  
  public FocNotifActionFactory(){
    put(FNotifTrigger.ACTION_SEND_EMAIL, new FocNotifAction_SendEmail());
  }
  
  public static FocNotifActionFactory getInstance(boolean createIfNeeded) {
    FocNotifActionFactory focNEF = null;
    if (Globals.getApp() != null) {
      focNEF = Globals.getApp().getNotificationActionFactory(createIfNeeded);
    }
    return focNEF;
  }
  
  public static FocNotifActionFactory getInstance(){
    return getInstance(true);
  }
}
