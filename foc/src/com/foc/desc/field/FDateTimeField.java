/*
 * Created on Oct 14, 2004
 */
package com.foc.desc.field;

import java.awt.Component;
import java.sql.Types;

import com.foc.Globals;
import com.foc.db.DBManager;
import com.foc.desc.FocObject;
import com.foc.gui.FGDateChooser;
import com.foc.gui.StaticComponent;
import com.foc.property.*;

/**
 * @author Tony
 */
public class FDateTimeField extends FDateField {
	private boolean timeRelevant = true;
	
  public FDateTimeField(String name, String title, int id, boolean key) {
    super(name, title, id, key);
  }

  public boolean isTimeRelevant(){
  	return timeRelevant;
  }
  
  @Override
  public boolean isDateRelevant() {
  	return true;
  }
  
  public void setTimeRelevant(boolean timeRelevant){
  	this.timeRelevant = timeRelevant;
  }

  public int getSqlType() {
    return Types.TIMESTAMP;
  }

  public String getCreationString(String name) {
  	if(getProvider()== DBManager.PROVIDER_ORACLE){
  		return " \"" + name + "\" DATE";
  	}else{
  		return " " + name + " DATETIME";
  	}
  }

  public FProperty newProperty(FocObject masterObj, Object defaultValue){
    return new FDateTime(masterObj, getID(), (java.sql.Date)defaultValue);
  }

  public FProperty newProperty(FocObject masterObj){
    return new FDateTime(masterObj, getID(), new java.sql.Date(0));
  }

  public Component getGuiComponent(FProperty prop){
//  	FGDateField dateField = null;
//  	if(prop != null && ((FDateTime)prop).isTimeRelevant()){
//  		dateField = new FGDateTimeField();
//  		dateField.setColumns(Double.valueOf(17 * Globals.CHAR_SIZE_FACTOR).intValue());
//  	}else{
//      return super.getGuiComponent(prop);
//  	}
//    if(prop != null) dateField.setProperty(prop);
    FGDateChooser dateField = new FGDateChooser("dd/MM/yyyy hh:mm");
    if(getToolTipText() != null){
    	StaticComponent.setComponentToolTipText(dateField, getToolTipText());
    }
    if(prop != null){
      dateField.setProperty(prop);
    }
    return dateField;
  }
     
  public int getFieldDisplaySize() {
    return 17;
  }
}
