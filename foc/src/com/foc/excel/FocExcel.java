package com.foc.excel;

import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;

@SuppressWarnings("serial")
public class FocExcel extends FocObject {

  public FocExcel(FocConstructor constr) {
    super(constr);
    newFocProperties();
  }
  
  public String getExcelFileContents(){
    return getPropertyString(FocExcelDesc.FLD_EXCEL_FILE_CONTENTS);
  }
  
  public void setExcelFieldContents(String fileContents){
    setPropertyString(FocExcelDesc.FLD_EXCEL_FILE_CONTENTS, fileContents);
  }
  
  public String getTableName(){
    return getPropertyString(FocExcelDesc.FLD_TABLE_NAME);
  }
  
  public void setTableName(String tableName){
    setPropertyString(FocExcelDesc.FLD_TABLE_NAME, tableName);
  }
}
