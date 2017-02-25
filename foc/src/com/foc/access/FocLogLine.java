package com.foc.access;

import java.sql.Date;

import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;
import com.foc.property.FProperty;

@SuppressWarnings("serial")
public class FocLogLine extends FocObject implements FocLogLineConst {

  public FocLogLine(FocConstructor constr) {
    super(constr);
    newFocProperties();
  }

  public String getMessage(){
  	return getPropertyString(FLD_MESSAGE);
  }

  public void setMessage(String name){
  	String str = name;
  	if(str.length() > LEN_MESSAGE){
  		str = name.substring(0, LEN_MESSAGE);
  	}
  	setPropertyString(FLD_MESSAGE, str);
  }

  public int getType(){
    return getPropertyMultiChoice(FLD_TYPE);
  }

  public void setType(int type){
    setPropertyInteger(FLD_TYPE, type);
  }

  public Date getDateTime(){
    return getPropertyDate(FLD_DATE_TIME);
  }

  public void setDateTime(Date dateTime){
    setPropertyDate(FLD_DATE_TIME, dateTime);
  }
  
  public String getDateTimeString(){
    FProperty prop = getFocProperty(FLD_DATE_TIME);
    String dateTime = null;
    if(prop != null){
      dateTime = prop.getString();
    }
    return dateTime;
  }
  
  public boolean getSuccessful(){
    return getPropertyBoolean(FLD_SUCCESSFUL);
  }
  
  public void setSuccessful(boolean success){
    setPropertyBoolean(FLD_SUCCESSFUL, success);
  }
  
  public String getLogLineString(){
    
    String logString = getDateTimeString();
    
    switch(getType()){
    case FocLogLine.TYPE_ERROR   : {
      logString += " Error: ";
      break;
      }
    case FocLogLine.TYPE_INFO    : {
      logString += " Info: ";
      break;
      }
    case FocLogLine.TYPE_WARNING : {
      logString += " Warning: ";
      break;
      }
    }
      
    logString += getMessage();
    
    return logString;
  }
}
