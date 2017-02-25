package com.foc.business.notifier;

import java.util.ArrayList;

public class FocNotificationEventArray {

	private ArrayList<FocNotificationEvent> arrayOfEvents = null;
	
	public FocNotificationEventArray(){
		arrayOfEvents = new ArrayList<FocNotificationEvent>();
	}
	
	public void dispose(){
		if(arrayOfEvents != null){
			for(int i=0; i<arrayOfEvents.size(); i++){
				arrayOfEvents.get(i).dispose();
			}
			arrayOfEvents.clear();
			arrayOfEvents = null;
		}
	}
	
	public int size(){
		return arrayOfEvents != null ? arrayOfEvents.size() : 0;
	}
	
	public FocNotificationEvent get(int i){
		FocNotificationEvent event = null;
		if(arrayOfEvents != null){
			event = arrayOfEvents.get(i);
		}
		return event;
	}
	
	public void add(FocNotificationEvent event){
		if(arrayOfEvents == null) arrayOfEvents = new ArrayList<FocNotificationEvent>();
		arrayOfEvents.add(event);
	}
	
	public void fireEvents(){
		if(arrayOfEvents != null){
			for(int i=0; i<arrayOfEvents.size(); i++){
				FocNotificationEvent event = arrayOfEvents.get(i);
				FocNotificationManager.getInstance().fireEvent(event);
			}
		}
	}
}
