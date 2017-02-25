package com.foc.gui.table.view;

import java.awt.Color;
import java.awt.Font;

import com.foc.ConfigInfo;
import com.foc.Globals;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;
import com.foc.desc.field.FNumField;
import com.foc.gui.FPanel;
import com.foc.list.FocList;
import com.foc.property.FInt;

//Save column visibility configuration in data base
public class ColumnsConfig extends FocObject{
	
	private Font font = null;
	
  public ColumnsConfig(FocConstructor constr){
    super(constr);
    newFocProperties();
    setNumberOfDecimals(-1);
    setGroupingUsed(FNumField.DEFAULT_GROUPING_USED);
  }
  
  public void dispose(){
  	super.dispose();
  	font = null;
  }

  public static void disposeColumnsConfigFocList(FocList list){
    list.dispose();
    list = null;
  }
  
  @Override
  public FPanel newDetailsPanel(int viewID) {
    return null;
  }
  
  public boolean isShow(){
  	return getPropertyBoolean(ColumnsConfigDesc.FLD_SHOW);
  }

  public void setShow(boolean show){
  	setPropertyBoolean(ColumnsConfigDesc.FLD_SHOW, show);
  }

  public int getColumnID(){
    FInt col = (FInt)getFocProperty(ColumnsConfigDesc.FLD_COLUMN_ID);
    return col.getInteger();
  }
  
  public void setColumnID(int colID){
    FInt colProp = (FInt)getFocProperty(ColumnsConfigDesc.FLD_COLUMN_ID);
    if(colProp != null){
      colProp.setInteger(colID);
    }
  }

  public int getColumnOrder(){
    return getPropertyInteger(ColumnsConfigDesc.FLD_COLUMN_ORDER);
  }
  
  public void setColumnOrder(int order){
  	setPropertyInteger(ColumnsConfigDesc.FLD_COLUMN_ORDER, order);
  }

  public String getColumnTitle(){
    return getPropertyString(ColumnsConfigDesc.FLD_COLUMN_TITLE);
  }
  
  public void setColumnTitle(String str){
  	if(str.length() > ColumnsConfigDesc.LEN_TITLE){
  		str = str.substring(0, ColumnsConfigDesc.LEN_TITLE);
  	}
  	setPropertyString(ColumnsConfigDesc.FLD_COLUMN_TITLE, str);
  }

  public String getColumnDefaultTitle(){
    return getPropertyString(ColumnsConfigDesc.FLD_COLUMN_DEFAULT_TITLE);
  }
  
  public void setColumnDefaultTitle(String str){
  	setPropertyString(ColumnsConfigDesc.FLD_COLUMN_DEFAULT_TITLE, str);
  }

  public String getColumnExplanation(){
    return getPropertyString(ColumnsConfigDesc.FLD_COLUMN_EXPLANATION);
  }
  
  public void setColumnExplanation(String str){
  	setPropertyString(ColumnsConfigDesc.FLD_COLUMN_EXPLANATION, str);
  }
  
  public Color getBackground(){
  	return getPropertyColor(ColumnsConfigDesc.FLD_BACKGROUND);
  }
  
  public Color getForeground(){
  	return getPropertyColor(ColumnsConfigDesc.FLD_FOREGROUND);
  }

  public int getFontSizeAdjusted(){
  	int size = getFontSize();
  	if(size == 0){
  		size = Globals.getApp().getUser().getFontSize();
  		if(size == 0) size = ConfigInfo.getFontSize();
  	}
  	return size;
  }

  public int getFontSize(){
  	return getPropertyInteger(ColumnsConfigDesc.FLD_FONT_SIZE);
  }

  public void setFontSize(int size){
  	setPropertyInteger(ColumnsConfigDesc.FLD_FONT_SIZE, size);
  }

  public int getFontStyle(){
  	return getPropertyInteger(ColumnsConfigDesc.FLD_FONT_STYLE);
  }
  
  public Font getFont(){
  	if(font == null && (getFontStyle() != Font.PLAIN || getFontSizeAdjusted() != Globals.getDisplayManager().getDefaultFont().getSize())){
  		font = Globals.getDisplayManager().getDefaultFont().deriveFont(getFontStyle(), getFontSizeAdjusted());
  	}
  	return font;
  }
  
  public void resetFont(){
  	font = null;
  }
  
  public int getNumberOfDecimals(){
  	return getPropertyMultiChoice(ColumnsConfigDesc.FLD_DECIMALS);
  }

  public void setNumberOfDecimals(int decimals){
  	setPropertyMultiChoice(ColumnsConfigDesc.FLD_DECIMALS, decimals);
  }

  public boolean isGroupingUsed(){
  	return getPropertyBoolean(ColumnsConfigDesc.FLD_GROUPING_USED);
  }
  
  public void setGroupingUsed(boolean groupingUsed){
  	setPropertyBoolean(ColumnsConfigDesc.FLD_GROUPING_USED, groupingUsed);
  }  
}
