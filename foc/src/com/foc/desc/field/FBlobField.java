package com.foc.desc.field;

import java.awt.Component;
import java.sql.Blob;
import java.sql.Types;

import com.fab.model.table.FieldDefinition;
import com.foc.db.DBManager;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.gui.table.cellControler.AbstractCellControler;
import com.foc.list.filter.FilterCondition;
import com.foc.property.FBlobProperty;
import com.foc.property.FProperty;

public class FBlobField extends FField{
  
  public FBlobField(String name, String title, int id, boolean key) {
    super(name, title, id, key, 0, 0);
    setIncludeInDBRequests(false);
  }

  @Override
 	public int getFabType() {
 	  return FieldDefinition.SQL_TYPE_ID_BLOB_FILE;//The image field will override to make it more specific
 	}

  public static int SqlType() {
    return Types.BLOB;
  }
  
  public int getSqlType() {
    return SqlType();
  }

  public String getCreationString(String name) {
    if (getProvider()== DBManager.PROVIDER_ORACLE){
      return " " + name + " BLOB";
    }else if (getProvider()== DBManager.PROVIDER_MSSQL){
    	return " " + name + " varbinary";
    }else{
      return " " + name + " BLOB";
    }
  }
 
  public Component getGuiComponent(FProperty prop){
    return null;
  }
  
  public AbstractCellControler getTableCellEditor_ToImplement(FProperty prop){
    return null;
  }
  
  public boolean isObjectContainer(){
    return false;
  }

  public FocDesc getFocDesc(){
    return null;
  }
  
  public void addReferenceLocations(FocDesc pointerDesc){
    
  }
  
  @Override
  protected FilterCondition getFilterCondition(FFieldPath fieldPath, String conditionPrefix){
    return null;
  }
  
  @Override
  public FProperty newProperty_ToImplement(FocObject masterObj, Object defaultValue) {
    return new FBlobProperty(masterObj, getID(), (Blob)defaultValue);
  }
  
  @Override
  public FProperty newProperty_ToImplement(FocObject masterObj) {
    return newProperty(masterObj, null);
  }
}
