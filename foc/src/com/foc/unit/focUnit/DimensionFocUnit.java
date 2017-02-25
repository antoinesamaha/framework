package com.foc.unit.focUnit;

import com.foc.business.units.DimensionDesc;
import com.foc.business.units.UnitDesc;
import com.foc.fUnit.FocTestCase;
import com.foc.fUnit.FocTestSuite;
import com.foc.gui.table.FTable;

public class DimensionFocUnit extends FocTestCase {

  public DimensionFocUnit(FocTestSuite testSuite, String functionName){
    super(testSuite, functionName);
  }

  protected void menuBarOpen_DimensionScreen(){
    menu_ClickByPath(new String[]{"Configuration", "Dimension"});
  }
  
  protected void validate(){
  	setDefaultFocDesc(DimensionDesc.getInstance());
  	button_ClickValidate();
  }
  
  protected void insertDimension(String dimension){
  	setDefaultFocDesc(DimensionDesc.getInstance());
  	
  	table_ClickInsertButton();
  	sleep(1);
  	guiComponent_SetValue(DimensionDesc.FLD_NAME, dimension);
  	
  	button_ClickValidate();
  }

  protected void insertUnit(String dimension, String unitName, String unitSymbol, double factor){
  	//Select the dimension line and wait for panel update
  	setDefaultFocDesc(DimensionDesc.getInstance());
  	FTable table = getTable();
  	requestFocus(table);
  	sleep(1);
  	table_FindAndSelectRow(dimension);
  	sleep(4);
  	table_FindAndSelectRow(DimensionDesc.DIMENSION_LABEL_TIME);
  	sleep(4);
  	table_FindAndSelectRow(dimension);
  	sleep(4);

  	//Insert a Unit in the unit list panel
  	setDefaultFocDesc(UnitDesc.getInstance());
  	table_ClickInsertButton();
  	table_SetValue(table_GetRowCount()-1, UnitDesc.FLD_NAME, unitName);
  	table_SetValue(table_GetRowCount()-1, UnitDesc.FLD_SYMBOL, unitSymbol);
  	table_SetValue(table_GetRowCount()-1, UnitDesc.FLD_FACTOR, String.valueOf(factor));
  }
}
