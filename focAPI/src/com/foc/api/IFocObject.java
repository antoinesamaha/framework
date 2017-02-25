package com.foc.api;

import java.sql.Date;

public interface IFocObject {
  public String     iFocObject_getPropertyString(String fieldName);
  public void       iFocObject_setPropertyString(String fieldName, String value);

  public double     iFocObject_getPropertyDouble(String fieldName);
  public void       iFocObject_setPropertyDouble(String fieldName, double value);

  public int        iFocObject_getPropertyInteger(String fieldName);
  public void       iFocObject_setPropertyInteger(String fieldName, int value);

  public IFocObject iFocObject_getPropertyFocObject(String fieldName);
  public void       iFocObject_setPropertyFocObject(String fieldName, IFocObject value);

  public Date       iFocObject_getPropertyDate(String fieldName);
  public void       iFocObject_setPropertyDate(String fieldName, Date value);

  public IFocList   iFocObject_getPropertyList(String fieldName);

	public IFocObject iFocObject_getFatherObject();
	
	public boolean    iFocObject_validate();
	public void       iFocObject_cancel();
	public void       iFocObject_dispose();
}
