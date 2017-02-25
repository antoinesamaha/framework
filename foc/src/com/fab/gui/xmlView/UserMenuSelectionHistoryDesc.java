package com.fab.gui.xmlView;

import com.foc.admin.FocUserDesc;
import com.foc.desc.FocDesc;
import com.foc.desc.field.FStringField;
import com.foc.desc.field.FIntField;
import com.foc.desc.field.FObjectField;
import com.foc.list.FocList;

public class UserMenuSelectionHistoryDesc extends FocDesc {

  public static final int FLD_USER       = 1;
  public static final int FLD_MENU_CODE  = 2;
  public static final int FLD_MENU_ORDER = 3;
  
  public static final String DB_TABLE_NAME = "USER_MENU_SELECTION_HISTORY";
  
  public UserMenuSelectionHistoryDesc() {
    super(UserMenuSelectionHistory.class, FocDesc.DB_RESIDENT, DB_TABLE_NAME, true);
    addReferenceField();

    FObjectField usefFld = new FObjectField("USER", "User", FLD_USER, FocUserDesc.getInstance());
    usefFld.setSelectionList(FocUserDesc.getList(FocList.NONE));
    usefFld.setComboBoxCellEditor(FocUserDesc.FLD_NAME);
    usefFld.setDisplayField(FocUserDesc.FLD_NAME);
    addField(usefFld);
    
    FStringField menuCodeFld = new FStringField("MENU_CODE", "Menu Code", FLD_MENU_CODE, true, 50);
    addField(menuCodeFld);
    
    FIntField orderFld = new FIntField("MENU_ORDER", "Menu Order", FLD_MENU_ORDER, true, 50);
    addField(orderFld);
  }
  
  public static FocList getList(int mode){
  	return getInstance().getFocList(mode);
  }
  
  public FocList newFocList(){
    FocList list = super.newFocList();
    list.setDirectlyEditable(false);
    list.setDirectImpactOnDatabase(true);
    return list;
  }
  
  public static FocDesc getInstance() {
    return getInstance(DB_TABLE_NAME, UserMenuSelectionHistoryDesc.class);
  }
}