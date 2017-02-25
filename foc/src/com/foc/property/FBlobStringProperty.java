package com.foc.property;

import com.foc.Globals;
import com.foc.db.DBManager;
import com.foc.desc.FocObject;
import com.foc.desc.field.FBlobStringField;

public class FBlobStringProperty extends FString {

  private boolean modified = false;
  public FBlobStringProperty(FocObject focObj, int fieldID, String str) {
    super(focObj, fieldID, str);
    setModified(false);
  }
  
  private void requestPropertyValue(){
    FocObject focObj = getFocObject();
    int f1 = focObj.getReference().getFocField().getID();
    int f2 = getFocField().getID();
    int fields[] = {f1, f2};//, f2];
  	Globals.getApp().getDataSource().focObject_Load(focObj, fields);
  	
  	/*
    FocDesc focDesc = focObj.getThisFocDesc();
    if( focDesc != null ){
      SQLFilter filter = new SQLFilter(focObj, SQLFilter.FILTER_ON_IDENTIFIER);
      SQLSelect sqlSelect = new SQLSelect(focObj, focObj.getThisFocDesc(), filter);
      sqlSelect.addQueryField(focObj.getReference().getFocField().getID());
      sqlSelect.addQueryField(getFocField().getID());
      if(sqlSelect.execute()){
        focObj.setLoadedFromDB(false);
      }
    }
    */
  }
  
  private boolean duringSelect = false;
  public String getString() {
  	FBlobStringField fld = (FBlobStringField) getFocField();
    if(getFocObject().hasRealReference() && !getFocObject().isCreated() && !fld.isIncludeInDBRequests() && (super.getString() == null || super.getString().equals(""))){
      if(!duringSelect){
        duringSelect = true;
        getFocObject().setDesactivatePropertyListeners(true);
        requestPropertyValue();
        getFocObject().setDesactivatePropertyListeners(false);
        setModified(false);
        duringSelect = false;
      }
    }
    return super.getString();
  }
  
  public void setString(String str) {
    if( doSetString(str)){
      super.setString(str);
      setModified(true);  
    }
  }
  
  public boolean isModified() {
    return modified;
  }

  public void setModified(boolean modified) {
    this.modified = modified;
  }
  
  public String getSqlString() {
  	String sqlStr = null;
  	if(getProvider() == DBManager.PROVIDER_MSSQL){
  		sqlStr = new String(getString() != null ? getString() : "");
  		
  		sqlStr = sqlStr.replace("'", "''");
  		sqlStr = "N\'" + sqlStr + "\'";
  		sqlStr = "CAST(" + sqlStr + " AS nvarchar(MAX))";//CAST('wahid' AS VARBINARY(MAX))
  		
//  		sqlStr = "CAST('" + sqlStr + "\' AS VARBINARY(MAX))";//CAST('wahid' AS VARBINARY(MAX))
  	}else if(getProvider() == DBManager.PROVIDER_ORACLE){
  		sqlStr = new String(getString() != null ? getString() : "");
  		sqlStr = sqlStr.replaceAll("\"", "''");
  		sqlStr = "utl_raw.cast_to_raw(\'" + sqlStr + "\')";
  	}else{
  		sqlStr = super.getSqlString();
  	}
    return sqlStr;
  }
}

