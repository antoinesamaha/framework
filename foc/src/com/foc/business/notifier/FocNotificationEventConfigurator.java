package com.foc.business.notifier;

import com.foc.business.notifier.manipulators.IFocNotificationEventManipulator;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;

@SuppressWarnings("serial")
public class FocNotificationEventConfigurator extends FocObject implements FocNotificationConst {
  
  private IFocNotificationEventManipulator localEventManipulator = null;//For standard non internal event this is usually null
  
  public FocNotificationEventConfigurator(FocConstructor constr){
    super(constr);
    newFocProperties();
  }
  
  public int getEvent(){
    return getPropertyInteger(FLD_EVENT);
  }
  
  public void setEvent(int event){
    setPropertyInteger(FLD_EVENT, event);
  }
  
  
  public String getTransaction(){
    return getPropertyString(FLD_TRANSACTION);
  }
  
  public void setTransaction(String transaction){
    setPropertyString(FLD_TRANSACTION, transaction);
  }
  
  public FocObject getTemplate(){
    return getPropertyObject(FLD_TEMPLATE_OBJECT);
  }
  
  public void setTemplate(FocObject obj){
    setPropertyObject(FLD_TEMPLATE_OBJECT, obj);
  }
  
  public FocDesc getTableDesc(){
    return getPropertyDesc(FLD_TABLE_NAME);
  }
  
  public void setTableDesc(FocDesc focDesc){
    setPropertyDesc(FLD_TABLE_NAME, focDesc);
  }

  public IFocNotificationEventManipulator getLocalEventManipulator() {
    return localEventManipulator;
  }

  public void setLocalEventManipulator(IFocNotificationEventManipulator localEventManipulator) {
    this.localEventManipulator = localEventManipulator;
  }

}
