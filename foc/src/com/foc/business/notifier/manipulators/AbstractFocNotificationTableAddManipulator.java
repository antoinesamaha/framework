package com.foc.business.notifier.manipulators;

import com.foc.Globals;
import com.foc.business.notifier.FocNotificationEmail;
import com.foc.business.notifier.FocNotificationEmailDesc;
import com.foc.business.notifier.FocNotificationEmailTemplate;
import com.foc.business.notifier.FocNotificationEvent;
import com.foc.business.notifier.FocNotificationEventConfigurator;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocDesc;

public abstract class AbstractFocNotificationTableAddManipulator implements IFocNotificationEventManipulator {

  @Override
  public void treatEvent(FocNotificationEventConfigurator notifier, FocNotificationEvent event) {
    sendEMail(notifier, event);
  }
  
  private void sendEMail(FocNotificationEventConfigurator notifier, FocNotificationEvent event){
    FocNotificationEmailTemplate template = (FocNotificationEmailTemplate) notifier.getTemplate();
    FocNotificationEmail email = new FocNotificationEmail(new FocConstructor(FocNotificationEmailDesc.getInstance(), null), template, event.getEventFocData());
    Globals.popup(email, false);
  }

  protected boolean isSameDBTableAsFocObject(FocNotificationEventConfigurator notifier, FocNotificationEvent event){
    boolean sameTable = false;
//    FocNotificationEmailTemplate template = (FocNotificationEmailTemplate) notifier.getTemplate();
  
    if (notifier != null && event != null && event.getEventFocData() != null && event.getEventFocData().iFocData_getDataByPath("TABLE_NAME") != null && event.getEventFocData().iFocData_getDataByPath("TABLE_NAME").iFocData_getValue() != null  /*&& template != null*/) {
      String eventTableName = (String) event.getEventFocData().iFocData_getDataByPath("TABLE_NAME").iFocData_getValue();
  
      FocDesc existingTableDesc = notifier.getTableDesc();
      if (existingTableDesc != null) {
        String notifierTableName = existingTableDesc.getStorageName();
        if (notifierTableName != null && !notifierTableName.isEmpty()) {
  
          if (eventTableName.equals(notifierTableName)) {
            sameTable = true;
          }
        }
      }
    }
    return sameTable;
  }

}