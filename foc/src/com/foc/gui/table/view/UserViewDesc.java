package com.foc.gui.table.view;

import com.foc.admin.FocUser;
import com.foc.desc.FocDesc;
import com.foc.desc.field.FStringField;
import com.foc.desc.field.FField;
import com.foc.desc.field.FObjectField;

public class UserViewDesc extends FocDesc{

  public static final int FLD_USER         = 1;
  public static final int FLD_VIEW         = 2;
  public static final int FLD_VIEW_KEY     = 3;
  public static final int FLD_VIEW_CONTEXT = 4;
  
	public UserViewDesc(){
    super(UserView.class, DB_RESIDENT, "USER_VIEW", true);
    FField focFld = addReferenceField();

    focFld = new FObjectField("USER", "User", FLD_USER, true, FocUser.getFocDesc(), "USER_");
		addField(focFld);

		FObjectField objFld = new FObjectField("VIEW", "View", FLD_VIEW, false, ViewConfigDesc.getInstance(), "VIEW_", this, ViewConfigDesc.FLD_USER_CONFIG_LIST);
		objFld.setDetailsPanelViewID(ViewConfigGuiDetailsPanel.VIEW_LABEL_ONLY);
		objFld.setComboBoxCellEditor(ViewConfigDesc.FLD_CODE);
		objFld.setNullValueMode(FObjectField.NULL_VALUE_ALLOWED_AND_SHOWN);
		objFld.setNullValueDisplayString("");
		addField(objFld);
		
		focFld = new FStringField("VIEW_ID", "View id", FLD_VIEW_KEY, true, 40);
		addField(focFld);
		
		focFld = new FStringField("VIEW_CONTEXT", "View context", FLD_VIEW_CONTEXT, true, 30);
		addField(focFld);
  }

  private static UserViewDesc focDesc = null;
  public static UserViewDesc getInstance(){
    if(focDesc == null){
      focDesc = new UserViewDesc();
    }
    return focDesc;
  }
}
