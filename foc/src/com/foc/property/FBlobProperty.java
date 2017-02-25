package com.foc.property;

import java.io.File;
import java.io.InputStream;
import java.sql.Blob;

import com.foc.Globals;
import com.foc.db.DBManager;
import com.foc.desc.FocObject;

@SuppressWarnings("serial")
public class FBlobProperty extends FProperty{

	private InputStream inputStream = null;
  private File file = null;
  
  public FBlobProperty(FocObject focObj, int fieldID, Blob defaultValue) {
    super(focObj, fieldID);
  }
  
  public void setFile(File file){
    try{
      this.file = file;
    }catch(Exception e){
      Globals.logException(e);
    }
  }
  
  public long getFileLength(){
    return file.length();
  }
  
  @Override
  public String getSqlString() {
  	if(getProvider() == DBManager.PROVIDER_MSSQL){
  		String sqlStr = new String(getString() != null ? getString() : "");
  		sqlStr = "CAST('" + sqlStr + "\' AS VARBINARY(MAX))";//CAST('wahid' AS VARBINARY(MAX))
  		return sqlStr;
  	}else if(			getProvider() == DBManager.PROVIDER_ORACLE
  						||  getProvider() == DBManager.PROVIDER_H2){
  		return "\'" + getString() + "\'";
  	}else{
  		return "\"" + getString() + "\"";
  	}
  }

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
  
}
