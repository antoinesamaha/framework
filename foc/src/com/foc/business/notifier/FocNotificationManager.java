package com.foc.business.notifier;

import java.util.HashMap;

import com.foc.Globals;
import com.foc.business.notifier.manipulators.IFocNotificationEventManipulator;
import com.foc.desc.FocDesc;
import com.foc.list.FocLinkSimple;
import com.foc.list.FocList;

public class FocNotificationManager {

  private FocList eventNotifierList         = null;
  private FocList internalEventNotifierList = null;
  private HashMap<Long, FocNotificationEventArray> mapOfThreadsSuspendingTheirEvents = null; 

  public FocNotificationManager() {
    setEventNotifierList(FocNotificationEventConfiguratorDesc.getInstance().getFocList());
    
    mapOfThreadsSuspendingTheirEvents = new HashMap<Long, FocNotificationEventArray>();
  }

  public void dispose() {
    if(internalEventNotifierList != null){
      internalEventNotifierList.dispose();
      internalEventNotifierList = null;
    }
    eventNotifierList = null;
  }

  public void threadCumulatingEvents_AddThread(){
  	long id = Thread.currentThread().getId();
  	if(mapOfThreadsSuspendingTheirEvents != null && !mapOfThreadsSuspendingTheirEvents.containsKey(id)){
  		mapOfThreadsSuspendingTheirEvents.put(id, null);
  	}
  }

  public void threadCumulatingEvents_FireEventsCumulated(){
  	long id = Thread.currentThread().getId();
  	if(mapOfThreadsSuspendingTheirEvents != null && mapOfThreadsSuspendingTheirEvents.containsKey(id)){
  		FocNotificationEventArray eventArray = mapOfThreadsSuspendingTheirEvents.get(id);
  		if(eventArray != null){
  			mapOfThreadsSuspendingTheirEvents.remove(id);
  			for(int i=0; i<eventArray.size(); i++){
  				FocNotificationEvent event = eventArray.get(i);
  				fireEvent(event);
  			}
  		}
  	}
  }
  
  private boolean threadCumulatingEvents_AddEvent(FocNotificationEvent eventFired){
  	boolean isSuspending = false;
  	long id = Thread.currentThread().getId();
  	if(mapOfThreadsSuspendingTheirEvents != null && mapOfThreadsSuspendingTheirEvents.containsKey(id)){
  		FocNotificationEventArray eventArray = mapOfThreadsSuspendingTheirEvents.get(id);
  		if(eventArray == null){
  			eventArray = new FocNotificationEventArray();
  			mapOfThreadsSuspendingTheirEvents.put(id, eventArray);
  		}
  		eventArray.add(eventFired);
  		isSuspending = true;
  	}
  	return isSuspending;
  }
  
  public void fireEvent(FocNotificationEvent eventFired) {
    if (getEventNotifierList() != null) {
    	if(!threadCumulatingEvents_AddEvent(eventFired)){
	      FocList eventNotifierList = getInternalEventNotifierList(false);
	      if(eventNotifierList != null){
	        scanListOfEventsAndTreat(eventNotifierList, eventFired);
	      }
	      
	      eventNotifierList = getEventNotifierList();
	      scanListOfEventsAndTreat(eventNotifierList, eventFired);
    	}
    }
  }
  
  private void scanListOfEventsAndTreat(FocList eventNotifierList, FocNotificationEvent eventFired){
    if(eventNotifierList != null){
      for (int i = 0; i < eventNotifierList.size(); i++) {
        FocNotificationEventConfigurator notifier = (FocNotificationEventConfigurator) eventNotifierList.getFocObject(i);
  
        if (notifier.getEvent() == eventFired.getEventKey()) {
          
          IFocNotificationEventManipulator eventManipulator = notifier.getLocalEventManipulator();
          if(eventManipulator == null){
            eventManipulator = FocNotificationEventFactory.getInstance().get(eventFired.getEventKey());
          }
          
          if(eventManipulator != null && eventManipulator.shouldTreatEvent(notifier, eventFired)){
            eventManipulator.treatEvent(notifier, eventFired);
          }
          
        }
      }
    }
  }
  
  private FocList getInternalEventNotifierList(boolean createIfNeeded){
    if(internalEventNotifierList == null && createIfNeeded){
      internalEventNotifierList = new FocList(new FocLinkSimple(FocNotificationEventConfiguratorDesc.getInstance()));
      internalEventNotifierList.setDbResident(false);
    }
    return internalEventNotifierList;
  }

  public FocNotificationEventConfigurator addInternalEventNotifier(int eventID, FocDesc focDesc, String transactionName, IFocNotificationEventManipulator eventManipulator){
    FocList list = getInternalEventNotifierList(true);
    FocNotificationEventConfigurator notifier = (FocNotificationEventConfigurator) list.newEmptyItem();
    notifier.setEvent(eventID);
    notifier.setTableDesc(focDesc);
    notifier.setLocalEventManipulator(eventManipulator);
    list.add(notifier);
    return notifier;
  }
  
  public void removeInternalEventNotifier(FocNotificationEventConfigurator notifier){
  	FocList list = getInternalEventNotifierList(false);
  	if(list != null){
  		list.remove(notifier);
  	}
  }
  
  public FocList getEventNotifierList() {
    return eventNotifierList;
  }

  public void setEventNotifierList(FocList list) {
    eventNotifierList = list;
  }

  public static FocNotificationManager getInstance() {
    FocNotificationManager notificationManager = null;
    if (Globals.getApp() != null) {
      notificationManager = Globals.getApp().getNotificationManager();
    }
    return notificationManager;
  }
}
