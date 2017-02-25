package com.foc.business.notification;

import java.sql.Date;
import java.sql.Time;

import com.foc.admin.FocUser;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;

@SuppressWarnings("serial")
public class FocNotification extends FocObject{

	public FocNotification(FocConstructor constructor) {
		super(constructor);
		newFocProperties();
	}
	
	public FocUser getUser(){
		return (FocUser) getPropertyObject(FocNotificationDesc.FLD_USER);
	}
	
	public void getUser(FocUser focUser){
		setPropertyObject(FocNotificationDesc.FLD_USER, focUser);
	}
	
	public String getTransactionFocDescName(){
		return getPropertyMultipleChoiceStringBased(FocNotificationDesc.FLD_TRANSACTION_FOC_DESC);
	}
	
	public FocDesc getTransactionFocDesc(){
		return getPropertyDesc(FocNotificationDesc.FLD_TRANSACTION_FOC_DESC);
	}
	
	public int getObjectReference(){
		return getPropertyInteger(FocNotificationDesc.FLD_REFERENCE);
	}
	
	public void getObjectReference(int val){
		setPropertyInteger(FocNotificationDesc.FLD_REFERENCE, val);
	}
	
	public String getMessage(){
		return getPropertyString(FocNotificationDesc.FLD_MESSAGE);
	}
	
	public void getMessage(String val){
		setPropertyString(FocNotificationDesc.FLD_MESSAGE, val);
	}
	
	public Date getDate(){
		return getPropertyDate(FocNotificationDesc.FLD_DATE);
	}
	
	public void getDate(Date val){
		setPropertyDate(FocNotificationDesc.FLD_DATE, val);
	}
	
	public Time getTime(){
		return getPropertyTime(FocNotificationDesc.FLD_TIME);
	}
	
	public void getTime(Time val){
		setPropertyTime(FocNotificationDesc.FLD_TIME, val);
	}
	
	public int getNotificationStatus(){
    return (int) getPropertyInteger(FocNotificationDesc.FLD_NOTIFICATION_STATUS);
  }
  
  public void setNotificationStatus(int val){
    setPropertyInteger(FocNotificationDesc.FLD_NOTIFICATION_STATUS, val);
  }

}
